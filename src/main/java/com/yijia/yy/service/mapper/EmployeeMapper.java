package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.City;
import com.yijia.yy.domain.Dept;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.service.dto.EmployeeDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {PictureInfoMapper.class, JobTitleMapper.class})
@DecoratedWith(EmployeeMapperDecorator.class)
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper( EmployeeMapper.class );

    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "jobPositionStatus", ignore = true)
    @Mapping(target = "marriage", ignore = true)
    @Mapping(target = "hasDriverLicense", ignore = true)
    @Mapping(target = "childbearing", ignore = true)
    @Mapping(target = "login", source = "user.login")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees);
    Set<EmployeeDTO> employeesToEmployeeDTOs(Set<Employee> employees);

    @Mapping(target = "aedProjects", ignore = true)
    @Mapping(target = "participatedProjects", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "jobPositionStatus", ignore = true)
    @Mapping(target = "marriage", ignore = true)
    @Mapping(target = "hasDriverLicense", ignore = true)
    @Mapping(target = "childbearing", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    List<Employee> employeeDTOsToEmployees(List<EmployeeDTO> employeeDTOs);
    Set<Employee> employeeDTOsToEmployees(Set<EmployeeDTO> employeeDTOs);

    default JobTitle jobTitleFromId(Long id) {
        if (id == null) {
            return null;
        }
        JobTitle jobTitle = new JobTitle();
        jobTitle.setId(id);
        return jobTitle;
    }

    default City cityFromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }

    default Dept deptFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dept dept = new Dept();
        dept.setId(id);
        return dept;
    }
}
