package project.operations;

import project.*;

public class DurationVisitor implements ToDoVisitor {
    private int duration = 0;

    @Override
    public void visitTask(Task task) {
        task.stream()
                .forEach(ToDo -> ToDo.accept(this));
    }

    @Override
    public void visitGamingSession(GamingSession gamingSession) {
        duration += gamingSession.getDuration();
    }

    @Override
    public void visitExerciseSession(ExerciseSession exerciseSession) {
        duration += exerciseSession.getDuration();
    }

    @Override
    public void visitStudySession(StudySession studySession) {
        duration += studySession.getDuration();
    }

    @Override
    public void visitShoppingSession(ShoppingSession shoppingSession) {
        duration += shoppingSession.getDuration();
    }

    public int getDuration() {
        return this.duration;
    }
}

