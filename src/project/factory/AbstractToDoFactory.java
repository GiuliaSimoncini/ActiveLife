package project.factory;

import project.ToDo;

public interface AbstractToDoFactory {
    ToDo createTask(String title);
    ToDo createGamingSession(String title, int duration);
    ToDo createExerciseSession(String title);
    ToDo createStudySession(String title, int duration);
    ToDo createShoppingSession(String title, int duration, int maxBudget);
}
