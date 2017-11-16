package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.City;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Province;
import com.yijia.yy.domain.enumeration.*;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.service.mapper.EmployeeMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EmployeeResource REST controller.
 *
 * @see EmployeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class EmployeeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Education DEFAULT_EDUCATION = Education.UNDER_HIGH_SCHOOL;
    private static final Education UPDATED_EDUCATION = Education.JUNIOR_COLLEGE;
    private static final String DEFAULT_EMP_NUMBER = "AAAAA";
    private static final String UPDATED_EMP_NUMBER = "BBBBB";
    private static final String DEFAULT_NATIVE_PLACE = "AAAAA";
    private static final String UPDATED_NATIVE_PLACE = "BBBBB";
    private static final String DEFAULT_NATIONALITY = "AAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBB";
    private static final String DEFAULT_ID_NUMBER = "AAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBB";
    private static final String DEFAULT_BASIC_INFO = "AAAAA";
    private static final String UPDATED_BASIC_INFO = "BBBBB";

    private static final Long DEFAULT_HIRE_DATE = 1475379531131L;
    private static final Long UPDATED_HIRE_DATE = 1475389531131L;

    private static final Double DEFAULT_PROBATION_SALARY = 1D;
    private static final Double UPDATED_PROBATION_SALARY = 2D;

    private static final Double DEFAULT_SALARY = 1D;
    private static final Double UPDATED_SALARY = 2D;

    private static final JobPositionStatus DEFAULT_JOB_POSITION_STATUS = JobPositionStatus.PROBATION;
    private static final JobPositionStatus UPDATED_JOB_POSITION_STATUS = JobPositionStatus.STAFF;
    private static final String DEFAULT_JOB_POSITION_INFO = "AAAAA";
    private static final String UPDATED_JOB_POSITION_INFO = "BBBBB";

    private static final Long DEFAULT_BIRTH_DATE = 1475389531131L;
    private static final Long UPDATED_BIRTH_DATE = 1475389531121L;

    private static final BooleanEnum DEFAULT_MARRIAGE = BooleanEnum.NO;
    private static final BooleanEnum UPDATED_MARRIAGE = BooleanEnum.YES;

    private static final BooleanEnum DEFAULT_CHILDBEARING = BooleanEnum.NO;
    private static final BooleanEnum UPDATED_CHILDBEARING = BooleanEnum.YES;
    private static final String DEFAULT_HUKOU = "AAAAA";
    private static final String UPDATED_HUKOU = "BBBBB";
    private static final String DEFAULT_PENSION_ACCOUNT = "AAAAA";
    private static final String UPDATED_PENSION_ACCOUNT = "BBBBB";
    private static final String DEFAULT_HOUSE_FOUND_ACCOUNT = "AAAAA";
    private static final String UPDATED_HOUSE_FOUND_ACCOUNT = "BBBBB";

    private static final BooleanEnum DEFAULT_HAS_DRIVER_LICENSE = BooleanEnum.NO;
    private static final BooleanEnum UPDATED_HAS_DRIVER_LICENSE = BooleanEnum.YES;
    private static final String DEFAULT_CELLPHONE = "AAAAA";
    private static final String UPDATED_CELLPHONE = "BBBBB";
    private static final String DEFAULT_MAIL = "AAAAA";
    private static final String UPDATED_MAIL = "BBBBB";
    private static final String DEFAULT_QQ = "AAAAA";
    private static final String UPDATED_QQ = "BBBBB";
    private static final String DEFAULT_WORK_QQ = "AAAAA";
    private static final String UPDATED_WORK_QQ = "BBBBB";
    private static final String DEFAULT_WORK_QQ_PASSWORD = "AAAAA";
    private static final String UPDATED_WORK_QQ_PASSWORD = "BBBBB";
    private static final String DEFAULT_WECHART = "AAAAA";
    private static final String UPDATED_WECHART = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_TEL = "AAAAA";
    private static final String UPDATED_TEL = "BBBBB";
    private static final String DEFAULT_EMERGENCY_TEL = "AAAAA";
    private static final String UPDATED_EMERGENCY_TEL = "BBBBB";
    private static final String DEFAULT_EMERGENCY_CONTACT = "AAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT = "BBBBB";
    private static final String DEFAULT_RELATIONSHIP_WITH_EMERGENCY_CONTACT = "AAAAA";
    private static final String UPDATED_RELATIONSHIP_WITH_EMERGENCY_CONTACT = "BBBBB";
    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBB";
    private static final String DEFAULT_COMPUTER_LEVEL = "AAAAA";
    private static final String UPDATED_COMPUTER_LEVEL = "BBBBB";
    private static final String DEFAULT_FOREIGN_LANGUAGE_LEVEL = "AAAAA";
    private static final String UPDATED_FOREIGN_LANGUAGE_LEVEL = "BBBBB";

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeResource employeeResource = new EmployeeResource();
        ReflectionTestUtils.setField(employeeResource, "employeeService", employeeService);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
                .name(DEFAULT_NAME)
                .gender(DEFAULT_GENDER)
                .age(DEFAULT_AGE)
                .education(DEFAULT_EDUCATION)
                .empNumber(DEFAULT_EMP_NUMBER)
                .nativePlace(new Province().name(DEFAULT_NATIVE_PLACE))
                .nationality(DEFAULT_NATIONALITY)
                .idNumber(DEFAULT_ID_NUMBER)
                .basicInfo(DEFAULT_BASIC_INFO)
                .hireDate(DEFAULT_HIRE_DATE)
                .probationSalary(DEFAULT_PROBATION_SALARY)
                .salary(DEFAULT_SALARY)
                .jobPositionStatus(DEFAULT_JOB_POSITION_STATUS)
                .jobPositionInfo(DEFAULT_JOB_POSITION_INFO)
                .birthDate(DEFAULT_BIRTH_DATE)
                .marriage(DEFAULT_MARRIAGE)
                .childbearing(DEFAULT_CHILDBEARING)
                .hukou(DEFAULT_HUKOU)
                .pensionAccount(DEFAULT_PENSION_ACCOUNT)
                .houseFoundAccount(DEFAULT_HOUSE_FOUND_ACCOUNT)
                .hasDriverLicense(DEFAULT_HAS_DRIVER_LICENSE)
                .cellphone(DEFAULT_CELLPHONE)
                .mail(DEFAULT_MAIL)
                .qq(DEFAULT_QQ)
                .workQq(DEFAULT_WORK_QQ)
                .workQqPassword(DEFAULT_WORK_QQ_PASSWORD)
                .wechart(DEFAULT_WECHART)
                .address(DEFAULT_ADDRESS)
                .tel(DEFAULT_TEL)
                .emergencyTel(DEFAULT_EMERGENCY_TEL)
                .emergencyContact(DEFAULT_EMERGENCY_CONTACT)
                .relationshipWithEmergencyContact(DEFAULT_RELATIONSHIP_WITH_EMERGENCY_CONTACT)
                .additionalInfo(DEFAULT_ADDITIONAL_INFO)
                .computerLevel(DEFAULT_COMPUTER_LEVEL)
                .foreignLanguageLevel(DEFAULT_FOREIGN_LANGUAGE_LEVEL);
        return employee;
    }

    @Before
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);

        restEmployeeMockMvc.perform(post("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testEmployee.getEducation()).isEqualTo(DEFAULT_EDUCATION);
        assertThat(testEmployee.getEmpNumber()).isEqualTo(DEFAULT_EMP_NUMBER);
        assertThat(testEmployee.getNativePlace()).isEqualTo(DEFAULT_NATIVE_PLACE);
        assertThat(testEmployee.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testEmployee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testEmployee.getBasicInfo()).isEqualTo(DEFAULT_BASIC_INFO);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getProbationSalary()).isEqualTo(DEFAULT_PROBATION_SALARY);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getJobPositionStatus()).isEqualTo(DEFAULT_JOB_POSITION_STATUS);
        assertThat(testEmployee.getJobPositionInfo()).isEqualTo(DEFAULT_JOB_POSITION_INFO);
        assertThat(testEmployee.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testEmployee.getMarriage()).isEqualTo(DEFAULT_MARRIAGE);
        assertThat(testEmployee.getChildbearing()).isEqualTo(DEFAULT_CHILDBEARING);
        assertThat(testEmployee.getHukou()).isEqualTo(DEFAULT_HUKOU);
        assertThat(testEmployee.getPensionAccount()).isEqualTo(DEFAULT_PENSION_ACCOUNT);
        assertThat(testEmployee.getHouseFoundAccount()).isEqualTo(DEFAULT_HOUSE_FOUND_ACCOUNT);
        assertThat(testEmployee.getHasDriverLicense()).isEqualTo(DEFAULT_HAS_DRIVER_LICENSE);
        assertThat(testEmployee.getCellphone()).isEqualTo(DEFAULT_CELLPHONE);
        assertThat(testEmployee.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testEmployee.getQq()).isEqualTo(DEFAULT_QQ);
        assertThat(testEmployee.getWorkQq()).isEqualTo(DEFAULT_WORK_QQ);
        assertThat(testEmployee.getWorkQqPassword()).isEqualTo(DEFAULT_WORK_QQ_PASSWORD);
        assertThat(testEmployee.getWechart()).isEqualTo(DEFAULT_WECHART);
        assertThat(testEmployee.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployee.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testEmployee.getEmergencyTel()).isEqualTo(DEFAULT_EMERGENCY_TEL);
        assertThat(testEmployee.getEmergencyContact()).isEqualTo(DEFAULT_EMERGENCY_CONTACT);
        assertThat(testEmployee.getRelationshipWithEmergencyContact()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_EMERGENCY_CONTACT);
        assertThat(testEmployee.getAdditionalInfo()).isEqualTo(DEFAULT_ADDITIONAL_INFO);
        assertThat(testEmployee.getComputerLevel()).isEqualTo(DEFAULT_COMPUTER_LEVEL);
        assertThat(testEmployee.getForeignLanguageLevel()).isEqualTo(DEFAULT_FOREIGN_LANGUAGE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employees
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION.toString())))
                .andExpect(jsonPath("$.[*].empNumber").value(hasItem(DEFAULT_EMP_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].nativePlace").value(hasItem(DEFAULT_NATIVE_PLACE.toString())))
                .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
                .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].basicInfo").value(hasItem(DEFAULT_BASIC_INFO.toString())))
                .andExpect(jsonPath("$.[*].probationSalary").value(hasItem(DEFAULT_PROBATION_SALARY.doubleValue())))
                .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
                .andExpect(jsonPath("$.[*].jobPositionStatus").value(hasItem(DEFAULT_JOB_POSITION_STATUS.toString())))
                .andExpect(jsonPath("$.[*].jobPositionInfo").value(hasItem(DEFAULT_JOB_POSITION_INFO.toString())))
                .andExpect(jsonPath("$.[*].marriage").value(hasItem(DEFAULT_MARRIAGE)))
                .andExpect(jsonPath("$.[*].childbearing").value(hasItem(DEFAULT_CHILDBEARING)))
                .andExpect(jsonPath("$.[*].hukou").value(hasItem(DEFAULT_HUKOU.toString())))
                .andExpect(jsonPath("$.[*].pensionAccount").value(hasItem(DEFAULT_PENSION_ACCOUNT.toString())))
                .andExpect(jsonPath("$.[*].houseFoundAccount").value(hasItem(DEFAULT_HOUSE_FOUND_ACCOUNT.toString())))
                .andExpect(jsonPath("$.[*].hasDriverLicense").value(hasItem(DEFAULT_HAS_DRIVER_LICENSE.toString())))
                .andExpect(jsonPath("$.[*].cellphone").value(hasItem(DEFAULT_CELLPHONE.toString())))
                .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL.toString())))
                .andExpect(jsonPath("$.[*].qq").value(hasItem(DEFAULT_QQ.toString())))
                .andExpect(jsonPath("$.[*].workQq").value(hasItem(DEFAULT_WORK_QQ.toString())))
                .andExpect(jsonPath("$.[*].workQqPassword").value(hasItem(DEFAULT_WORK_QQ_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].wechart").value(hasItem(DEFAULT_WECHART.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].emergencyTel").value(hasItem(DEFAULT_EMERGENCY_TEL.toString())))
                .andExpect(jsonPath("$.[*].emergencyContact").value(hasItem(DEFAULT_EMERGENCY_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].relationshipWithEmergencyContact").value(hasItem(DEFAULT_RELATIONSHIP_WITH_EMERGENCY_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO.toString())))
                .andExpect(jsonPath("$.[*].computerLevel").value(hasItem(DEFAULT_COMPUTER_LEVEL.toString())))
                .andExpect(jsonPath("$.[*].foreignLanguageLevel").value(hasItem(DEFAULT_FOREIGN_LANGUAGE_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.education").value(DEFAULT_EDUCATION.toString()))
            .andExpect(jsonPath("$.empNumber").value(DEFAULT_EMP_NUMBER.toString()))
            .andExpect(jsonPath("$.nativePlace").value(DEFAULT_NATIVE_PLACE.toString()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER.toString()))
            .andExpect(jsonPath("$.basicInfo").value(DEFAULT_BASIC_INFO.toString()))
            .andExpect(jsonPath("$.probationSalary").value(DEFAULT_PROBATION_SALARY.doubleValue()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.jobPositionStatus").value(DEFAULT_JOB_POSITION_STATUS.toString()))
            .andExpect(jsonPath("$.jobPositionInfo").value(DEFAULT_JOB_POSITION_INFO.toString()))
            .andExpect(jsonPath("$.marriage").value(DEFAULT_MARRIAGE))
            .andExpect(jsonPath("$.childbearing").value(DEFAULT_CHILDBEARING))
            .andExpect(jsonPath("$.hukou").value(DEFAULT_HUKOU.toString()))
            .andExpect(jsonPath("$.pensionAccount").value(DEFAULT_PENSION_ACCOUNT.toString()))
            .andExpect(jsonPath("$.houseFoundAccount").value(DEFAULT_HOUSE_FOUND_ACCOUNT.toString()))
            .andExpect(jsonPath("$.hasDriverLicense").value(DEFAULT_HAS_DRIVER_LICENSE.toString()))
            .andExpect(jsonPath("$.cellphone").value(DEFAULT_CELLPHONE.toString()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL.toString()))
            .andExpect(jsonPath("$.qq").value(DEFAULT_QQ.toString()))
            .andExpect(jsonPath("$.workQq").value(DEFAULT_WORK_QQ.toString()))
            .andExpect(jsonPath("$.workQqPassword").value(DEFAULT_WORK_QQ_PASSWORD.toString()))
            .andExpect(jsonPath("$.wechart").value(DEFAULT_WECHART.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.emergencyTel").value(DEFAULT_EMERGENCY_TEL.toString()))
            .andExpect(jsonPath("$.emergencyContact").value(DEFAULT_EMERGENCY_CONTACT.toString()))
            .andExpect(jsonPath("$.relationshipWithEmergencyContact").value(DEFAULT_RELATIONSHIP_WITH_EMERGENCY_CONTACT.toString()))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO.toString()))
            .andExpect(jsonPath("$.computerLevel").value(DEFAULT_COMPUTER_LEVEL.toString()))
            .andExpect(jsonPath("$.foreignLanguageLevel").value(DEFAULT_FOREIGN_LANGUAGE_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findOne(employee.getId());
        updatedEmployee
                .name(UPDATED_NAME)
                .gender(UPDATED_GENDER)
                .age(UPDATED_AGE)
                .education(UPDATED_EDUCATION)
                .empNumber(UPDATED_EMP_NUMBER)
                .nativePlace(new Province().name(UPDATED_NATIVE_PLACE))
                .nationality(UPDATED_NATIONALITY)
                .idNumber(UPDATED_ID_NUMBER)
                .basicInfo(UPDATED_BASIC_INFO)
                .hireDate(UPDATED_HIRE_DATE)
                .probationSalary(UPDATED_PROBATION_SALARY)
                .salary(UPDATED_SALARY)
                .jobPositionStatus(UPDATED_JOB_POSITION_STATUS)
                .jobPositionInfo(UPDATED_JOB_POSITION_INFO)
                .birthDate(UPDATED_BIRTH_DATE)
                .marriage(UPDATED_MARRIAGE)
                .childbearing(UPDATED_CHILDBEARING)
                .hukou(UPDATED_HUKOU)
                .pensionAccount(UPDATED_PENSION_ACCOUNT)
                .houseFoundAccount(UPDATED_HOUSE_FOUND_ACCOUNT)
                .hasDriverLicense(UPDATED_HAS_DRIVER_LICENSE)
                .cellphone(UPDATED_CELLPHONE)
                .mail(UPDATED_MAIL)
                .qq(UPDATED_QQ)
                .workQq(UPDATED_WORK_QQ)
                .workQqPassword(UPDATED_WORK_QQ_PASSWORD)
                .wechart(UPDATED_WECHART)
                .address(UPDATED_ADDRESS)
                .tel(UPDATED_TEL)
                .emergencyTel(UPDATED_EMERGENCY_TEL)
                .emergencyContact(UPDATED_EMERGENCY_CONTACT)
                .relationshipWithEmergencyContact(UPDATED_RELATIONSHIP_WITH_EMERGENCY_CONTACT)
                .additionalInfo(UPDATED_ADDITIONAL_INFO)
                .computerLevel(UPDATED_COMPUTER_LEVEL)
                .foreignLanguageLevel(UPDATED_FOREIGN_LANGUAGE_LEVEL);
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(updatedEmployee);

        restEmployeeMockMvc.perform(put("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testEmployee.getEducation()).isEqualTo(UPDATED_EDUCATION);
        assertThat(testEmployee.getEmpNumber()).isEqualTo(UPDATED_EMP_NUMBER);
        assertThat(testEmployee.getNativePlace()).isEqualTo(UPDATED_NATIVE_PLACE);
        assertThat(testEmployee.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testEmployee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testEmployee.getBasicInfo()).isEqualTo(UPDATED_BASIC_INFO);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.getProbationSalary()).isEqualTo(UPDATED_PROBATION_SALARY);
        assertThat(testEmployee.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployee.getJobPositionStatus()).isEqualTo(UPDATED_JOB_POSITION_STATUS);
        assertThat(testEmployee.getJobPositionInfo()).isEqualTo(UPDATED_JOB_POSITION_INFO);
        assertThat(testEmployee.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testEmployee.getMarriage()).isEqualTo(UPDATED_MARRIAGE);
        assertThat(testEmployee.getChildbearing()).isEqualTo(UPDATED_CHILDBEARING);
        assertThat(testEmployee.getHukou()).isEqualTo(UPDATED_HUKOU);
        assertThat(testEmployee.getPensionAccount()).isEqualTo(UPDATED_PENSION_ACCOUNT);
        assertThat(testEmployee.getHouseFoundAccount()).isEqualTo(UPDATED_HOUSE_FOUND_ACCOUNT);
        assertThat(testEmployee.getHasDriverLicense()).isEqualTo(UPDATED_HAS_DRIVER_LICENSE);
        assertThat(testEmployee.getCellphone()).isEqualTo(UPDATED_CELLPHONE);
        assertThat(testEmployee.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testEmployee.getQq()).isEqualTo(UPDATED_QQ);
        assertThat(testEmployee.getWorkQq()).isEqualTo(UPDATED_WORK_QQ);
        assertThat(testEmployee.getWorkQqPassword()).isEqualTo(UPDATED_WORK_QQ_PASSWORD);
        assertThat(testEmployee.getWechart()).isEqualTo(UPDATED_WECHART);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testEmployee.getEmergencyTel()).isEqualTo(UPDATED_EMERGENCY_TEL);
        assertThat(testEmployee.getEmergencyContact()).isEqualTo(UPDATED_EMERGENCY_CONTACT);
        assertThat(testEmployee.getRelationshipWithEmergencyContact()).isEqualTo(UPDATED_RELATIONSHIP_WITH_EMERGENCY_CONTACT);
        assertThat(testEmployee.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
        assertThat(testEmployee.getComputerLevel()).isEqualTo(UPDATED_COMPUTER_LEVEL);
        assertThat(testEmployee.getForeignLanguageLevel()).isEqualTo(UPDATED_FOREIGN_LANGUAGE_LEVEL);
    }

    @Test
    @Transactional
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Get the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
