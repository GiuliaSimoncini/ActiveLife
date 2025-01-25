package project;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class Task extends ToDo {
    private Collection<ToDo> toDos = new ArrayList<ToDo>();
    private int priority = 0;

    public Task(String title) {
        super(title);
    }

    public Task priority(int priority) {
        if (priority <= 0)
            throw new IllegalArgumentException("Priority can not be a negative number or equal to zero");
        this.priority = priority;
        return this;
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
}

