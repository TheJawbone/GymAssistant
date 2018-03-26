package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 07.02.2018.
 */

public class ExerciseSet {

    private long ID;
    private long segmentID;
    private int repCount;
    private int weight;
    private String createdAt;

    public ExerciseSet() {}

    public ExerciseSet(long segmentID, int repCount, int weight) {
        this.segmentID = segmentID;
        this.repCount = repCount;
        this.weight = weight;
    }

    public ExerciseSet(int ID, int segmentID, int repCount, int weight) {
        this.ID = ID;
        this.segmentID = segmentID;
        this.repCount = repCount;
        this.weight = weight;
    }

    public long getID() {
        return ID;
    }

    public long getSegmentID() {
        return segmentID;
    }

    public int getRepCount() {
        return repCount;
    }

    public int getWeight() {
        return weight;
    }

    public String getCreatedAt() { return createdAt; }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSegmentID(int segmentID) {
        this.segmentID = segmentID;
    }

    public void setRepCount(int repCount) {
        this.repCount = repCount;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
