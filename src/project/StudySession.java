package project;

import project.operations.ToDoVisitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class StudySession extends ToDo {
    private final int duration;
    private Collection<String> subjects = new ArrayList<String>();

    public StudySession(String title, int duration) {
        super(title);
        if (duration <= 0)
            throw new IllegalArgumentException("Duration can not be a negative number or 0");
        this.duration = duration;
    }

    public StudySession(String title, int duration, Collection<String> subjects) {
        this(title, duration);
        if (subjects == null)
            throw new IllegalArgumentException("Subjects can not be null");
        this.subjects = subjects;
    }

    public StudySession addSubject(String subject) {
        subjects.add(subject);
        return this;
    }

    public StudySession removeSubject(String subject) {
        subjects.remove(subject);
        return this;
    }

    public Stream<String> streamSubjects() {
        return subjects.stream();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void accept(ToDoVisitor visitor) {
        visitor.visitStudySession(this);
    }
}
