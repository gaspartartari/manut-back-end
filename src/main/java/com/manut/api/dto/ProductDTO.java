package com.manut.api.dto;

import java.util.ArrayList;
import java.util.List;
import com.manut.api.entities.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String name;
    private Double price;

    private List<Long> tasksId = new ArrayList<>();

    public ProductDTO(){

    }

    public ProductDTO(Product entity){
        id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        if(entity.getTasks() != null)
            entity.getTasks().forEach(task -> this.tasksId.add(task.getId()));
    }

}
