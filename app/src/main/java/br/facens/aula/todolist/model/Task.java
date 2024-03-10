package br.facens.aula.todolist.model;

public class Task {
    private String title;
    private String description;
    private String priority;
    private String creationDate;
    private boolean isCompleted;

    public Task(String title, String description, String priority, String creationDate, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    public Task() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
