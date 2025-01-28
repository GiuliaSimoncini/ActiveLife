package project.filtering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
import project.Task;
import project.ToDo;

public class TitleKeywordFilteringStrategy implements FilteringStrategy {
    private Collection<String> keywords = new ArrayList<String>();

    public TitleKeywordFilteringStrategy(String keyword) {
        if (keyword == null)
            throw new IllegalArgumentException("Keyword can not be null");
        if (keyword.isEmpty())
            throw new IllegalArgumentException("Keyword can not be empty");
        keywords.add(keyword);
    }

    public TitleKeywordFilteringStrategy addKeyword(String keyword) {
        if (keyword == null)
            throw new IllegalArgumentException("Keyword can not be null");
        if (keyword.isEmpty())
            throw new IllegalArgumentException("Keyword can not be empty");
        keywords.add(keyword);
        return this;
    }

    @Override
    public Stream<ToDo> filterToDo(Task task) {
        return task.stream()
                .filter(toDo -> keywords.stream()
                        .anyMatch(keyword -> toDo.getTitle().contains(keyword)));
    }

    @Override
    public String getFilteredDescription(Task task) {
        StringBuilder builder = new StringBuilder();
        this.filterToDo(task).forEach(toDo -> builder.append(toDo.getTitle())
                .append("\n"));
        if (builder.isEmpty())
            builder.append("No task contains any of the keywords\n");
        builder.append("Keywords:\n");
        keywords.forEach(str -> builder.append("\t").append(str).append("\n"));
        return builder.toString();
    }
}
