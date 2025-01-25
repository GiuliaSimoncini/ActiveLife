package project.factory;

import project.*;

public class ConcreteToDoFactory implements AbstractToDoFactory {
    @Override
    public Task createTask(String title) {
        return new Task(title);
    }

    @Override
    public GamingSession createGamingSession(String title, int duration) {
        return new GamingSession(title, duration);
    }

    @Override
    public ExerciseSession createExerciseSession(String title) {
        return new ExerciseSession(title);
    }

    @Override
    public StudySession createStudySession(String title, int duration) {
        return new StudySession(title, duration);
    }

    @Override
    public ShoppingSession createShoppingSession(String title, int duration, int maxBudget) {
        return new ShoppingSession(title, duration, maxBudget);
    }
}