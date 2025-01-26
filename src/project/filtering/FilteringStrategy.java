package project.filtering;

import java.util.stream.Stream;
import project.ToDo;
import project.Task;

public interface FilteringStrategy {
    Stream<ToDo> filterToDo(Task task);
    String getFilteredDescription(Task task);
}
