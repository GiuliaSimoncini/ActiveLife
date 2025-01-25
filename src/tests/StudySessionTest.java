package tests;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import project.StudySession;

public class StudySessionTest {
    private StudySession studySession;

    @Before
    public void setUp() {
        studySession = new StudySession("title", 10);
    }

    @Test
    public void addSubject() {
        studySession.addSubject("subject")
                .addSubject("subject2")
                .addSubject("subject3");
        assertEquals(3, studySession.streamSubjects().count());
    }

    @Test
    public void removeSubject() {
        studySession.addSubject("subject")
                .addSubject("subject2")
                .addSubject("subject3");
        studySession.removeSubject("subject2");
        assertEquals(2, studySession.streamSubjects().count());
        studySession.removeSubject("subject not present");
        assertEquals(2, studySession.streamSubjects().count());
    }
}
