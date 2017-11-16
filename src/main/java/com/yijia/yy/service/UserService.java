package com.yijia.yy.service;

import com.yijia.yy.domain.Authority;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Role;
import com.yijia.yy.domain.User;
import com.yijia.yy.repository.AuthorityRepository;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.repository.UserRepository;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.security.SecurityUtils;
import com.yijia.yy.service.dto.RoleDTO;
import com.yijia.yy.service.mapper.EmployeeMapper;
import com.yijia.yy.service.mapper.RoleMapper;
import com.yijia.yy.service.util.RandomUtil;
import com.yijia.yy.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private EmployeeRepository employeeRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUser(String login, String password, String firstName, String lastName, String email,
        String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(ManagedUserVM managedUserVM) {
        User user = new User();
        user.setLogin(managedUserVM.getLogin());
        user.setFirstName(managedUserVM.getFirstName());
        user.setLastName(managedUserVM.getLastName());
        user.setEmail(managedUserVM.getEmail());
        if (managedUserVM.getLangKey() == null) {
            user.setLangKey("zh-cn"); // default language
        } else {
            user.setLangKey(managedUserVM.getLangKey());
        }
        if (managedUserVM.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserVM.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
//        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        String psd = managedUserVM.getPassword();
        String encryptedPassword = passwordEncoder.encode(StringUtils.isBlank(psd) ? "" : psd);
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);

        user.setEmployee(employeeRepository.findOne(managedUserVM.getEmployee().getId()));
        user.getEmployee().setUser(user);

        userRepository.save(user);
        userRepository.flush();

        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUser(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void updateUser(Long id, String login, String password, String firstName, String lastName, String email,
                           boolean activated, String langKey, Set<String> authorities, Set<RoleDTO> roles) {

        userRepository
            .findOneById(id)
            .ifPresent(u -> {
                u.setLogin(login);
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setEmail(email);
                u.setActivated(activated);
                u.setLangKey(langKey);
                u.setRoles(
                    roles.stream()
                    .map(o -> roleMapper.roleDTOToRole(o))
                    .collect(Collectors.toSet())
                );

                if (StringUtils.isNotBlank(password)) {
                    u.setPassword(passwordEncoder.encode(password));
                }
                Set<Authority> managedAuthorities = u.getAuthorities();
                managedAuthorities.clear();
                authorities.stream().forEach(
                    authority -> {
                        Authority auth = authorityRepository.findOne(authority);
                        if (auth != null && StringUtils.isNotBlank(auth.getName())) {
                            managedAuthorities.add(auth);
                        } else {
                            log.error("Error while get auth: {}", authority);
                        }
                    }
                );
                log.debug("Changed Information for User: {}", u);
            });
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            employeeRepository.findOneByUser(u)
                .ifPresent(e -> {
                    e.setUser(null);
                    employeeRepository.saveAndFlush(e);
                });
            u.setEmployee(null);
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            u.getRoles().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        user.getRoles().size();
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User user = null;
        if (optionalUser.isPresent()) {
          user = optionalUser.get();
            user.getAuthorities().size(); // eagerly load the association
            user.getRoles().size();
        }
         return user;
    }

    @Transactional(readOnly = true)
    public Optional<User>  currentLoginUser() {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
    }

    @Transactional(readOnly = true)
    public Employee currentLoginEmployee() {
        Optional<User> user = currentLoginUser();
        if (user.isPresent()) {
            return user.get().getEmployee();
        }
        return  null;
    }

    @Transactional(readOnly = true)
    public User findOneById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Transactional
    public User deactivateUser(Long userId) {
        if (userId != null) {
            User user = userRepository.findOne(userId);
            if (user != null) {
                user.setActivated(false);
                return userRepository.save(user);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<String> roles2Auths(Set<RoleDTO> roles) {
        if (roles == null) {
            return new ArrayList<>();
        }

        Set<String> authNames = roles.stream()
            .map(r -> r.getAuths().split(","))
            .flatMap(x -> Arrays.stream(x))
            .collect(Collectors.toSet());

        return authNames.stream().collect(Collectors.toList());
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
}
