package org.example.Item;

public class TodoItem {
    private String text;
    private boolean completed;

    public TodoItem(String text) {
        this.text = text;
        this.completed = false;
    }

    public String getText() {
        return text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}