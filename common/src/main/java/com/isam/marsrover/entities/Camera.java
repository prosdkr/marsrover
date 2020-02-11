package com.isam.marsrover.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Camera {

    @JsonInclude(NON_NULL)
    private Integer id;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    @JsonInclude(NON_NULL)
    @JsonProperty("rover_id")
    private String roverId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoverId() {
        return roverId;
    }

    public void setRoverId(String roverId) {
        this.roverId = roverId;
    }
}
