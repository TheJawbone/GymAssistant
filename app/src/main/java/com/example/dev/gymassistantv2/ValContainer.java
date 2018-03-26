package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 19.02.2018.
 */

public class ValContainer<T> {

    private T val;

    public ValContainer() {
    }

    public ValContainer(T v) {
        this.val = v;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}
