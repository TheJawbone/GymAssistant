package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 07.02.2018.
 */

public class Segment {

    private long ID;
    private long exerciseID;
    private long workoutID;
    private String createdAt;

    public Segment() {}

    public Segment(long exerciseID, long workoutID) {
        this.exerciseID = exerciseID;
        this.workoutID = workoutID;
    }

    public Segment(int ID, long exerciseID, long workoutID) {
        this.ID = ID;
        this.exerciseID = exerciseID;
        this.workoutID = workoutID;
    }

    public long getID() {
        return ID;
    }

    public long getExerciseID() {
        return exerciseID;
    }

    public long getWorkoutID() { return workoutID; }

    public String getCreatedAt() { return createdAt; }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setExerciseID(long exerciseID) {
        this.exerciseID = exerciseID;
    }

    public void setWorkoutID(long workoutID) { this.workoutID = workoutID; }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
