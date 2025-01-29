package project;

import project.operations.ToDoVisitor;

public abstract class ToDo {
    private final String title;

    protected ToDo(String title) {
        if (title == null)
            throw new IllegalArgumentException("Title string can not be null");
        if (title.isEmpty())
            throw new IllegalArgumentException("Title string can not be empty");
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public abstract int getPriority();

    public abstract int getDuration();

    public abstract void accept(ToDoVisitor visitor);
}
