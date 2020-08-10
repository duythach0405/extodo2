package com.example.extodo2.data;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;

public interface TaskDataSource {

    Flowable<List<Task>> getTasks();

    Flowable<Optional<Task>> getTask(String taskId);

    void completeTask(Task task);

    void saveTask(Task task);

    void completeTask( String taskId);

    void activateTask( Task task);

    void activateTask( String taskId);

    void clearCompletedTasks();

    void deleteAllTasks();

    void deleteTask( String taskId);
}
