package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Task;
import com.yijia.yy.domain.converter.TaskStatusConverter;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.TaskDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapperDecorator implements TaskMapper{
    @Inject
    @Qualifier("delegate")
    private TaskMapper delegate;

    @Override
    public TaskDTO taskToTaskDTO(Task task) {
        if (task == null) {
            return null;
        }
        TaskDTO dto = delegate.taskToTaskDTO(task);
        if (task.getStatus() != null) {
            dto.setStatus(
                new DictionaryDTO()
                    .withId(Long.valueOf(task.getStatus().value()))
                    .withName(task.getStatus().toString())
            );
        }
        return dto;
    }

    @Override
    public List<TaskDTO> tasksToTaskDTOs(List<Task> tasks) {
        if (tasks == null) {
            return null;
        }

        return tasks.stream()
            .map(t -> taskToTaskDTO(t))
            .collect(Collectors.toList());
    }

    @Override
    public Task taskDTOToTask(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return  null;
        }
        Task task = delegate.taskDTOToTask(taskDTO);
        if (DomainObjectUtils.dictionaryDtoIsNotNull(taskDTO.getStatus())) {
            task.setStatus(
                new TaskStatusConverter().convertToEntityAttribute(
                    Math.toIntExact(taskDTO.getStatus().getId())
                )
            );
        }
        return task;
    }

    @Override
    public List<Task> taskDTOsToTasks(List<TaskDTO> taskDTOs) {
        if (taskDTOs == null) {
            return null;
        }

        return taskDTOs.stream()
            .map(t -> taskDTOToTask(t))
            .collect(Collectors.toList());
    }
}
