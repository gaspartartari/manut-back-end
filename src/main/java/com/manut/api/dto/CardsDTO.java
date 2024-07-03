package com.manut.api.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardsDTO {
    
    private Integer total;
    private Double onTimePercentage;
    private Double latePercentage;
    private Double inProgressPercentage;
    
}
