package com.ptandon.wonderlist;

public class List {
    private int id;
    private String todoText;
    private int dueDate;
    private String priority;
    private String status;
    private String taskNote;

    public List(int id,String todoText, int dueDate,String priority, String status,String taskNote) {
        this.id=id;
        this.todoText=todoText;
        this.priority=priority;
        this.dueDate=dueDate;
        this.status=status;
        this.taskNote=taskNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodoText() {
        return todoText;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }
}


