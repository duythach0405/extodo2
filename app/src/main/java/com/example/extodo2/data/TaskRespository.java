package com.example.extodo2.data;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskRespository implements TaskDataSource {
    private static TaskRespository INSTANCE = null;
    private final TaskDataSource mTasksLocalDataSource;

    public TaskRespository(TaskDataSource tasksLocalDataSource) {
        this.mTasksLocalDataSource = tasksLocalDataSource;
    }

    // update on thach
    public static TaskRespository getInstance(TaskDataSource tasksLocalDataSource) {
        if (INSTANCE == null){
            INSTANCE = new TaskRespository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Task>> getTasks() {

        return mTasksLocalDataSource.getTasks();
    }

    @Override
    public Flowable<Optional<Task>> getTask(String taskId) {
         return mTasksLocalDataSource
                .getTask(taskId)
                .firstElement().toFlowable();
    }

    @Override
    public void saveTask(Task task) {
        mTasksLocalDataSource.saveTask(task);
    }

    @Override
    public void completeTask(Task task) {
        mTasksLocalDataSource.completeTask(task);
    }

    @Override
    public void completeTask(String taskId) {
        mTasksLocalDataSource.completeTask(taskId);
    }

    @Override
    public void activateTask(Task task) {
        mTasksLocalDataSource.activateTask(task);
    }

    @Override
    public void activateTask(String taskId) {
        mTasksLocalDataSource.activateTask(taskId);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksLocalDataSource.clearCompletedTasks();
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();
    }

    @Override
    public void deleteTask(String taskId) {
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));
    }
}
