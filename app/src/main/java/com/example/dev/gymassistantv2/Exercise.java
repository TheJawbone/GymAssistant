package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 07.02.2018.
 */

public class Exercise implements Comparable {

    private long ID;
    private boolean isAvailable;
    private String name;
    private String createdAt;

    public Exercise() {}

    public Exercise(String name) {
        this.name = name;
    }

    public Exercise(long ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public long getID() {
        return ID;
    }

    public boolean getIsAvailable() { return isAvailable; }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setIsAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int compareTo(Object exerciseObj) throws ClassCastException {
        if (!(exerciseObj instanceof Exercise))
            throw new ClassCastException("Cast exception");
        Exercise exercise = (Exercise)exerciseObj;
        return this.name.compareTo(exercise.getName());
    }
}
