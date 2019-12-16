package ru.keepdoing.model;

public class Task {
    private final String brief;
    private final String full;
    private String report;
    private long executorChatId = 0;
    private TaskState taskState = TaskState.NEW;

    public Task(String brief, String full) {
        this.brief = brief;
        this.full = full;
    }

    public void changeState(TaskState newState) {
        taskState = newState;
    }

    public long getExecutorChatId() {
        return executorChatId;
    }

    public void setExecutorChatId(long executorChatId) {
        if (this.executorChatId == 0) {
            this.executorChatId = executorChatId;
        }
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getBrief() {
        return brief;
    }

    public String getFull() {
        return full;
    }

    public String getReport() {
        return report;
    }

    public enum TaskState {
        NEW,
        IN_PROGRESS,
        DONE,
        REJECTED
    }
}
