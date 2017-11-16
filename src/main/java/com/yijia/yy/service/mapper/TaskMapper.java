package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.TaskDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, EmployeeMapper.class, FileInfoMapper.class,})
@DecoratedWith(TaskMapperDecorator.class)
public interface TaskMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    TaskDTO taskToTaskDTO(Task task);

    List<TaskDTO> tasksToTaskDTOs(List<Task> tasks);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "projectId", target = "project")
    Task taskDTOToTask(TaskDTO taskDTO);

    List<Task> taskDTOsToTasks(List<TaskDTO> taskDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default FileInfo fileInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(id);
        return fileInfo;
    }

    default PictureInfo pictureInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        pictureInfo.setId(id);
        return pictureInfo;
    }

    default Dictionary dictionaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
