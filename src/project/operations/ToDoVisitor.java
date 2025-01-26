package project.operations;

import project.*;

public interface ToDoVisitor {
    void visitTask(Task task);
    void visitGamingSession(GamingSession gamingSession);
    void visitExerciseSession(ExerciseSession exerciseSession);
    void visitStudySession(StudySession studySession);
    void visitShoppingSession(ShoppingSession shoppingSession);
}