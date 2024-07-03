package com.manut.api.dto;


import java.util.HashSet;
import java.util.Set;

import com.manut.api.services.validations.TaskInsertValid;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@TaskInsertValid
public class MapTasksDTO {
    
    @NotNull(message = "Tasks canot be blank")
    private Set<Long> taskIds = new HashSet<>();
}
