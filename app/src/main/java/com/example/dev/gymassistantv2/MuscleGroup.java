package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 07.02.2018.
 */

public class MuscleGroup {

    private long ID;
    private String name;
    private String createdAt;

    public MuscleGroup() {}

    public MuscleGroup(String name) {
        this.name = name;
    }

    public MuscleGroup(long ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
