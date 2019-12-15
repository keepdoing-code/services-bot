package ru.keepdoing.model;

public class State{
    private UserState state;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public State(UserState state) {
        this.state = state;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public enum UserState{
        NEW,
        REGISTRATION,
        FREE,
        BUSY,
        BLOCKED,
    }
}
