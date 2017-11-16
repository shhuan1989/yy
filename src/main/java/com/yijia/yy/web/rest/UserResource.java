package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.config.Constants;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.User;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.repository.UserRepository;
import com.yijia.yy.service.MailService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.UserSearchDTO;
import com.yijia.yy.service.mapper.EmployeeMapper;
import com.yijia.yy.service.mapper.RoleMapper;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private EmployeeRepository employeeRepository;

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.ADMIN)
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        Employee employee = employeeMapper.employeeDTOToEmployee(managedUserVM.getEmployee());
        if (employee == null || employee.getId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "employeerequired", "Account must bind to an employee"))
                .body(null);
        } else if(userRepository.findOneByEmployeeId(employee.getId())
            .isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "employeeaccountexists", "Employee already has an account"))
                .body(null);
        }

        employee = employeeRepository.findOne(employee.getId());
        if (employee == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "employeenoneexist", "Employee doesn't exist"))
                .body(null);
        }

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else{
            User newUser = userService.createUser(managedUserVM);
//            newUser.setEmployee(null); // temporary work around
            String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context
            mailService.sendCreationEmail(newUser, baseUrl);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(null);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.ADMIN)
    @Transactional
    public ResponseEntity<ManagedUserVM> updateUser(@RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use")).body(null);
        }
        userService.updateUser(managedUserVM.getId(), managedUserVM.getLogin(), managedUserVM.getPassword(), managedUserVM.getFirstName(),
            managedUserVM.getLastName(), managedUserVM.getEmail(), managedUserVM.isActivated(),
            managedUserVM.getLangKey(), managedUserVM.getAuthorities(), managedUserVM.getRoles());

        User user = userService.getUserWithAuthorities(managedUserVM.getId());

        ManagedUserVM result = user2ManagedUserVM(user);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()))
            .body(result);
    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ManagedUserVM>> getAllUsers(Pageable pageable, UserSearchDTO searchDTO)
        throws URISyntaxException {
        Page<User> page = userRepository.findAllWithAuthorities(pageable);
        List<ManagedUserVM> managedUserVMs = page.getContent().stream()
            .filter(u -> { //workaround
                if (searchDTO == null) {
                    return true;
                }
                if (StringUtils.isNotBlank(searchDTO.getAccountName())) {
                    return u.getLogin().matches(".*"+searchDTO.getAccountName()+".*");
                }
                if (StringUtils.isNotBlank(searchDTO.getName())) {
                    return u.getEmployee() != null
                        && u.getEmployee().getName() != null
                        && u.getEmployee().getName().matches(".*"+searchDTO.getName()+".*");
                }
                if (StringUtils.isNotBlank(searchDTO.getDept())) {
                    return u.getEmployee() != null
                        && u.getEmployee().getDept() != null
                        && u.getEmployee().getDept().getName() != null
                        && u.getEmployee().getDept().getName().matches(".*"+searchDTO.getDept()+".*");
                }

                return true;
            })
            .map(user -> user2ManagedUserVM(user))
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserVMs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<ManagedUserVM> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
                .map(user -> user2ManagedUserVM(user))
                .map(managedUserVM -> new ResponseEntity<>(managedUserVM, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }

    private ManagedUserVM user2ManagedUserVM(User user) {
        ManagedUserVM managedUserVM = new ManagedUserVM(user);
        managedUserVM.setRoles(roleMapper.rolesToRoleDTOs(user.getRoles()));
        updateAuths(managedUserVM);
        managedUserVM.setEmployee(employeeMapper.employeeToEmployeeDTO(user.getEmployee()));
        return managedUserVM;
    }

    private void updateAuths(ManagedUserVM managedUserVM) {
        if (managedUserVM == null) {
            return;
        }

        Set<String> auths = managedUserVM.getAuthorities() != null ? managedUserVM.getAuthorities() : new HashSet<>();
        auths.addAll(userService.roles2Auths(managedUserVM.getRoles()));

        managedUserVM.setAuthorities(auths);
    }
}
