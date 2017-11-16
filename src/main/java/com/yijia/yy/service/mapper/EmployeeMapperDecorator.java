package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.converter.BooleanEnumConverter;
import com.yijia.yy.domain.converter.EducationConverter;
import com.yijia.yy.domain.converter.GenderConverter;
import com.yijia.yy.domain.converter.JobPositionStatusConverter;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.service.dto.PictureInfoDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeMapperDecorator implements EmployeeMapper {

    @Autowired
    @Qualifier("delegate")
    private EmployeeMapper delegate;

    @Override
    public EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDTO dto = delegate.employeeToEmployeeDTO(employee);
        toDto(employee, dto);
        return dto;
    }

    @Override
    public List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees) {
        if (employees == null) {
            return new ArrayList<>();
        }

        return employees.stream()
            .map(e -> employeeToEmployeeDTO(e))
            .collect(Collectors.toList());
    }

    @Override
    public Set<EmployeeDTO> employeesToEmployeeDTOs(Set<Employee> employees) {
        if (employees == null) {
            return new HashSet<>();
        }

        return employees.stream()
            .map(e -> employeeToEmployeeDTO(e))
            .collect(Collectors.toSet());
    }

    @Override
    public Employee employeeDTOToEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        Employee employee = delegate.employeeDTOToEmployee(employeeDTO);
        toEmp(employee, employeeDTO);
        return employee;
    }

    @Override
    public List<Employee> employeeDTOsToEmployees(List<EmployeeDTO> employeeDTOs) {
        if (employeeDTOs == null) {
            return new ArrayList<>();
        }

        return employeeDTOs.stream()
            .map(d -> employeeDTOToEmployee(d))
            .collect(Collectors.toList());
    }

    @Override
    public Set<Employee> employeeDTOsToEmployees(Set<EmployeeDTO> employeeDTOs) {
        if (employeeDTOs == null) {
            return new HashSet<>();
        }

        return employeeDTOs.stream()
            .map(d -> employeeDTOToEmployee(d))
            .collect(Collectors.toSet());
    }

    private void toDto(Employee employee, EmployeeDTO employeeDTO) {
        if (employee == null || employeeDTO == null) {
            return;
        }
        if (employee.getGender() != null) {
            employeeDTO.setGender(new DictionaryDTO().withId(Long.valueOf(employee.getGender().ordinal())).withName(employee.getGender().toString()));
        }

        if (employee.getJobPositionStatus() != null) {
            employeeDTO.setJobPositionStatus(new DictionaryDTO().withId(Long.valueOf(employee.getJobPositionStatus().ordinal())).withName(employee.getJobPositionStatus().toString()));
        }

        if (employee.getEducation() != null) {
            employeeDTO.setEducation(new DictionaryDTO().withId(Long.valueOf(employee.getEducation().ordinal())).withName(employee.getEducation().toString()));
        }

        if (employee.getMarriage() != null) {
            employeeDTO.setMarriage(new DictionaryDTO().withId(Long.valueOf(employee.getMarriage().ordinal())).withName(employee.getMarriage().toString()));
        }

        if (employee.getChildbearing() != null) {
            employeeDTO.setChildbearing(new DictionaryDTO().withId(Long.valueOf(employee.getChildbearing().ordinal())).withName(employee.getChildbearing().toString()));
        }

        if (employee.getHasDriverLicense() != null) {
            employeeDTO.setHasDriverLicense(new DictionaryDTO().withId(Long.valueOf(employee.getHasDriverLicense().ordinal())).withName(employee.getChildbearing().toString()));
            employeeDTO.setChildbearing(new DictionaryDTO().withId(Long.valueOf(employee.getChildbearing().ordinal())).withName(employee.getChildbearing().toString()));
        }

        if (employeeDTO.getPhoto() == null) {
            employeeDTO.setPhoto(new PictureInfoDTO().id(-1L));
        }

    }

    private void toEmp(Employee employee, EmployeeDTO dto) {
        if (employee == null || dto == null) {
            return;
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(dto.getGender())) {
            employee.setGender(new GenderConverter().convertToEntityAttribute(Math.toIntExact(dto.getGender().getId())));
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(dto.getEducation())) {
            employee.setEducation(new EducationConverter().convertToEntityAttribute(Math.toIntExact(dto.getEducation().getId())));
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(dto.getJobPositionStatus())) {
            employee.setJobPositionStatus(new JobPositionStatusConverter().convertToEntityAttribute(Math.toIntExact(dto.getJobPositionStatus().getId())));
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(dto.getChildbearing())) {
            employee.setChildbearing(new BooleanEnumConverter().convertToEntityAttribute(Math.toIntExact(dto.getChildbearing().getId())));
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(dto.getHasDriverLicense())) {
            employee.setHasDriverLicense(new BooleanEnumConverter().convertToEntityAttribute(Math.toIntExact(dto.getHasDriverLicense().getId())));
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(dto.getMarriage())) {
            employee.setMarriage(new BooleanEnumConverter().convertToEntityAttribute(Math.toIntExact(dto.getMarriage().getId())));
        }

//        String idNumber = dto.getIdNumber();
//        if (StringUtils.isNotBlank(idNumber)) {
//            Pattern p = Pattern.compile("(\\d{6})(\\d{4})(\\d{2})(\\d{2}).*");
//            Matcher matcher = p.matcher(idNumber);
//            if (matcher.groupCount() > 3) {
//                String year = matcher.group(1);
//                String month = matcher.group(2);
//                String day = matcher.group(2);
//
//                String pattern = "yyyy-MM-dd";
//                DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
//                DateTime dateTime = dtf.parseDateTime(year+"-"+month+"-"+day);
//                ZonedDateTime zdt = dateTime.toGregorianCalendar().toZonedDateTime();
//                employee.setBirthDate(zdt);
//                employee.setAge(ZonedDateTime.now().getYear() - Integer.parseInt(year));
//            }
//        }
    }
}
