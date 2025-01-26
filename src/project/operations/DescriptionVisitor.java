package project.operations;

import project.*;

public class DescriptionVisitor implements ToDoVisitor {
    private StringBuilder description = new StringBuilder();
    private int tabs = 0;

    @Override
    public void visitTask(Task task) {
        for (int i = 0; i < tabs; i++)
            description.append("\t");
        description.append(task.getTitle());
        if (task.getPriority() > 0)
            description.append(" - Priority: ").append(task.getPriority());
        long subTasksCount = task.stream().count();
        if (subTasksCount > 0) {
            description.append("\n");
            for (int i = 0; i < tabs; i++)
                description.append("\t");
            description.append("SubTasks:");
            task.stream()
                    .forEach(subTask -> {
                        description.append("\n");
                        tabs++;
                        subTask.accept(this);
                        tabs--;
                    });
        }
    }

    @Override
    public void visitGamingSession(GamingSession gamingSession) {
        for (int i = 0; i < tabs; i++)
            description.append("\t");
        description.append(gamingSession.getTitle())
                .append(" - ")
                .append(gamingSession.getDuration())
                .append(" mins");
        if (gamingSession.getPlatform() != null) {
            description.append("\n");
            for (int i = 0; i < tabs+1; i++) {
                description.append("\t");
            }
            description.append("Playing on: " + gamingSession.getPlatform());
        }
        if (gamingSession.getMultiplayer() != null) {
            description.append("\n");
            for (int i = 0; i < tabs+1; i++)
                description.append("\t");
            description.append("Multiplayer: " + gamingSession.getMultiplayer());
        }
    }

    @Override
    public void visitExerciseSession(ExerciseSession exerciseSession) {
        for (int i = 0; i < tabs; i++)
            description.append("\t");
        description.append(exerciseSession.getTitle());
        exerciseSession.streamExercises()
                .forEach(entry -> {
                    description.append("\n");
                    for (int i = 0; i < tabs + 1; i++)
                        description.append("\t");
                    description.append(entry.getKey() + " - " + entry.getValue() + " mins");
                });
    }

    @Override
    public void visitStudySession(StudySession studySession) {
        for (int i = 0; i < tabs; i++)
            description.append("\t");
        description.append(studySession.getTitle()).append(" - ").append(studySession.getDuration()).append(" mins");
        studySession.streamSubjects()
                .forEach(subject -> {
                    description.append("\n");
                    for (int i = 0; i < tabs + 1; i++)
                        description.append("\t");
                    description.append(subject);
                });
    }

    @Override
    public void visitShoppingSession(ShoppingSession shoppingSession) {
        for (int i = 0; i < tabs; i++)
            description.append("\t");
        description.append(shoppingSession.getTitle())
                .append(" - ")
                .append(shoppingSession.getDuration())
                .append(" mins");
        if (shoppingSession.getPlace() != null) {
            description.append("\n");
            for (int i = 0; i < tabs + 1; i++)
                description.append("\t");
            description.append("Shopping at: " + shoppingSession.getPlace());
        }
        description.append("\n");
        for (int i = 0; i < tabs + 1; i++)
            description.append("\t");
        description.append("Max budget: " + shoppingSession.getMaxBudget());
        shoppingSession.streamShopping()
                .forEach(entry -> {
                    description.append("\n");
                    for (int i = 0; i < tabs + 2; i++)
                        description.append("\t");
                    description.append(entry.getKey() + " - " + entry.getValue());
                });
    }

    public String getDescription() {
        return description.toString();
    }
}
