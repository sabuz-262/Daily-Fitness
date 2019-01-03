package com.wsu.dailyfitness;

public class Statistics {
    private String date;
    private int steps;

    public Statistics(String date, int steps){
        this.date = date;
        this.steps = steps;
    }

    public String getDate() {
        return date;
    }

    public int getSteps() {
        return steps;
    }
}
