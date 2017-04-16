package com.michaelpalmer.lab9sqldata;

public class TuringAward {

    private String name;
    private int year;
    private String school;
    private boolean alive;
    private String citation;

    public TuringAward(String name, int year, String school, boolean alive, String citation) {
        this.name = name;
        this.year = year;
        this.school = school;
        this.alive = alive;
        this.citation = citation;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getSchool() {
        return school;
    }

    public boolean isAlive() {
        return alive;
    }

    public String getCitation() {
        return citation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }
}
