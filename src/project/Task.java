package project;

import project.operations.ToDoVisitor;
import project.filtering.FilteringStrategy;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class Task extends ToDo {
    private Collection<ToDo> toDos = new ArrayList<ToDo>();
    private int priority = 0;
    private FilteringStrategy filteringStrategy;

    public Task(String title) {
        super(title);
    }

    public Task priority(int priority) {
        if (priority <= 0)
            throw new IllegalArgumentException("Priority can not be a negative number or equal to zero");
        this.priority = priority;
        return this;
    }

    public Task filteringStrategy(FilteringStrategy filteringStrategy) {
        if (filteringStrategy == null)
            throw new IllegalArgumentException("Filtering Strategy can not be null");
        this.filteringStrategy = filteringStrategy;
        return this;
    }

    public Stream<ToDo> getFilteredStream() {
        if (filteringStrategy == null)
            throw new IllegalArgumentException("Filtering Strategy can not be null");
        return filteringStrategy.filterToDo(this);
    }

    public String getFilteredDescription() {
        if (filteringStrategy == null)
            throw new IllegalArgumentException("Filtering Strategy can not be null");
        return filteringStrategy.getFilteredDescription(this);
    }

    public Task removeToDo(ToDo toDo) {
        toDos.remove(toDo);
        return this;
    }

    public Task removeByTitle(String title) {
        toDos.removeIf(toDo -> toDo.getTitle().equals(title));
        return this;
    }

    public Task addToDo(ToDo toDo) {
        toDos.add(toDo);
        return this;
    }

    public Task addGamingSession(String title, int duration) {
        toDos.add(new GamingSession(title, duration));
        return this;
    }

    public Task addExerciseSession(String title, Map<String, Integer> exercises) {
        toDos.add(new ExerciseSession(title, exercises));
        return this;
    }

    public Task addStudySession(String title, int duration, Collection<String> subjects) {
        toDos.add(new StudySession(title, duration, subjects));
        return this;
    }

    public Task addShoppingSession(String title, int duration, int budget, Map<String, Integer> items) {
        toDos.add(new ShoppingSession(title, duration, budget, items));
        return this;
    }

    public boolean isToDoInside(ToDo toDo) {
        return toDos.contains(toDo);
    }

    public boolean isToDoInside(String title) {
        return toDos.stream()
                .anyMatch(toDo -> title.equals(toDo.getTitle()));
    }
    public Stream<ToDo> stream() {
        return toDos.stream();
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void accept(ToDoVisitor visitor) {
        visitor.visitTask(this);
    }
}

