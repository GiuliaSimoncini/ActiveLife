package project;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ExerciseSession extends ToDo {
    private Map<String, Integer> exercises = new LinkedHashMap<String, Integer>();

    public ExerciseSession(String title) {
        super(title);
    }

    public ExerciseSession(String title, Map<String, Integer> exercises) {
        super(title);
        this.exercises = exercises;
    }

    public boolean isExerciseInside(String name, int minsSpent) {
        return exercises.containsKey(name) && exercises.get(name) == minsSpent;
    }

    public ExerciseSession addExercise(String name, int minsSpent) {
        if (minsSpent <= 0)
            throw new IllegalArgumentException("The minutes spent on an exercise can not be 0 or less than 0");
        if (name == null)
            throw new IllegalArgumentException("The name of an exercises can not be null");
        if (name.isEmpty())
            throw new IllegalArgumentException("The name of an exercises can not be empty");
        exercises.put(name, minsSpent);
        return this;
    }

    public ExerciseSession modifyMinsExercise(String name, int minsSpent) {
        if (!exercises.containsKey(name))
            throw new IllegalArgumentException("The exercise in input has not been inserted yet");
        if (minsSpent <= 0)
            throw new IllegalArgumentException("The minutes spent on an exercise can not be 0 or less than 0");
        exercises.replace(name, minsSpent);
        return this;
    }

    public ExerciseSession removeExercise(String name) {
        exercises.remove(name);
        return this;
    }

    public Stream<Map.Entry<String, Integer>> streamExercises() {
        return exercises.entrySet()
                .stream();
    }

    @Override
    public int getDuration() {
        return exercises.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
