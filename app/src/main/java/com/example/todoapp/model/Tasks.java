package com.example.todoapp.model;

public class Tasks {
    private int id;
    private boolean status;
    private String task;
    private String date;

    public Tasks(int id, boolean status, String task, String date) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "id=" + id +
                ", status=" + status +
                ", task='" + task + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
