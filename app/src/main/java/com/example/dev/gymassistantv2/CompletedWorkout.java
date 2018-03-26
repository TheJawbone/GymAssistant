package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 07.02.2018.
 */

public class CompletedWorkout {

    private long ID;
    private String description;
    private String createdAt;


    public CompletedWorkout() {}

    public CompletedWorkout(String description) {
        this.description = description;
    }

    public CompletedWorkout(int id, String description) {
        this.ID = id;
        this.description = description;
    }

    public long getID() {
        return ID;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
