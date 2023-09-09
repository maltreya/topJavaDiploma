package com.topjava.restaurantvoiting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {
    @NotBlank
    @Size(min = 2, max = 128)
    @Column(name="name", nullable = false)
    protected String name;

    protected AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + name + ")";
    }
}
