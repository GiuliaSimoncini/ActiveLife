package project.operations;

import project.*;

public class ShortDescriptionVisitor implements ToDoVisitor {
    private final StringBuilder shortDescription = new StringBuilder();
    private int tabs = 0;

    @Override
    public void visitTask(Task task) {
        for (int i = 0; i < tabs; i++)
            shortDescription.append("\t");
        shortDescription.append(task.getTitle());
        long subTasksCount = task.stream().count();
        if (subTasksCount > 0) {
            task.stream()
                    .forEach(subTask -> {
                        shortDescription.append("\n");
                        tabs++;
                        subTask.accept(this);
                        tabs--;
                    });
        }
    }

    @Override
    public void visitGamingSession(GamingSession gamingSession) {
        for (int i = 0; i < tabs; i++)
            shortDescription.append("\t");
        shortDescription.append(gamingSession.getTitle());
    }

    @Override
    public void visitExerciseSession(ExerciseSession exerciseSession) {
        for (int i = 0; i < tabs; i++)
            shortDescription.append("\t");
        shortDescription.append(exerciseSession.getTitle());
    }

    @Override
    public void visitStudySession(StudySession studySession) {
        for (int i = 0; i < tabs; i++)
            shortDescription.append("\t");
        shortDescription.append(studySession.getTitle());
    }

    @Override
    public void visitShoppingSession(ShoppingSession shoppingSession) {
        for (int i = 0; i < tabs; i++)
            shortDescription.append("\t");
        shortDescription.append(shoppingSession.getTitle());
    }

    public String getShortDescription() {
        return this.shortDescription.toString();
    }
}
