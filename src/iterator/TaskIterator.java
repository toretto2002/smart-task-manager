package iterator;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import model.Task;

public class TaskIterator implements Iterator<Task> {

    private final List<Task> tasks;
    private final Predicate<Task> filter;
    private int currentIndex = 0;

    public TaskIterator(List<Task> tasks, Predicate<Task> filter) {
        this.tasks = tasks;
        this.filter = filter;
        moveToNextValid();
    }

    @Override
    public boolean hasNext() {
        return currentIndex < tasks.size();
    }

    @Override
    public Task next() {
        Task current = tasks.get(currentIndex);
        currentIndex++;
        moveToNextValid();
        return current;
    }

    private void moveToNextValid() {
        while (currentIndex < tasks.size() && !filter.test(tasks.get(currentIndex))) {
            currentIndex++;
        }
    }

}
