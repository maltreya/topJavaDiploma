package com.topjava.restaurantvoiting.to;

import com.fasterxml.jackson.annotation.JsonView;
import com.topjava.restaurantvoiting.HasId;
import com.topjava.restaurantvoiting.View;

public abstract class BaseTo implements HasId {
    @JsonView(View.JsonREST.class)
    protected Integer id;

    public BaseTo() {

    }

    public BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
