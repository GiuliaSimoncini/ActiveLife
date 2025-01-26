package project.filtering;

import java.util.stream.Stream;
import project.ToDo;
import project.Task;

public class PriorityFilteringStrategy implements FilteringStrategy {
    private int minPriority = 0;

    public PriorityFilteringStrategy(int minPriority) {
        if (minPriority < 0)
            throw new IllegalArgumentException("The minimum priority can not be a negative number");
        this.minPriority = minPriority;
    }

    @Override
    public Stream<ToDo> filterToDo(Task task) {
        return task.stream()
                .filter(toDo -> toDo.getPriority() >= minPriority);
    }

    @Override
    public String getFilteredDescription(Task task) {
        StringBuilder builder = new StringBuilder();
        if (task.getPriority() >= minPriority)
            builder.append(task.getTitle() + "\n");
        this.filterToDo(task).forEach(toDo -> builder.append(toDo.getTitle())
                .append("\n"));
        if (builder.length() == 0)
            builder.append("No task has a high enough priority");
        return builder.toString();
    }
}
