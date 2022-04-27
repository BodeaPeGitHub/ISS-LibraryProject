package model;

import java.io.Serializable;

public class Entity implements Serializable {
    private Integer id;

    public Entity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}