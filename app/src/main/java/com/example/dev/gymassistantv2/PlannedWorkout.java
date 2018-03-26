package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 07.02.2018.
 */

public class PlannedWorkout {

    private int ID;
    private String workoutDate;
    private String createdAt;
    private String description;

    public PlannedWorkout() {}

    public PlannedWorkout(String workoutDate, String description) {
        this.workoutDate = workoutDate;
        this.description = description;
    }

    public PlannedWorkout(int ID, String workoutDate, String description) {
        this.ID = ID;
        this.workoutDate = workoutDate;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public String getWorkoutDate() {
        return workoutDate;
    }

    public String getCreatedAt() { return createdAt; }

    public String getDescription() {
        return description;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setWorkoutDate(String workoutDate) {
        this.workoutDate = workoutDate;
    }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public void setDescription(String description) {
        this.description = description;
    }
}
