package com.example.extodo2.activity;

import android.util.Log;

import com.example.extodo2.addTask.AddTaskActivity;
import com.example.extodo2.data.Task;
import com.example.extodo2.data.TaskRespository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TasksPresenter implements TaskContracts.Presenter {

    private final TaskRespository mTaskRespository;

    private final TaskContracts.View mTaskView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    public TasksPresenter( TaskContracts.View mTaskView,TaskRespository mTaskRespository) {
        this.mTaskRespository = mTaskRespository;
        this.mTaskView = mTaskView;
        mTaskView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddTaskActivity.REQUEST_ADD_TASK == requestCode && MainActivity.RESULT_OK == resultCode){
            mTaskView.showSuccessfullySaveMessage();
        }
    }

    @Override
    public void addNewTask() {
        mTaskView.showAddTask();
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks();
    }

    private void loadTasks() {
        mCompositeDisposable.add(mTaskRespository.getTasks()
                            .take(1)
                            .flatMap(Flowable::fromIterable)
                            .filter(task -> {
                                switch (mCurrentFiltering) {
                                    case ACTIVE_TASKS:
                                        return task.isActive();
                                    case COMPLETED_TASKS:
                                        return task.ismCompleted();
                                    case ALL_TASKS:
                                    default:
                                        return true;
                                }
                            })
                                .toList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this:: handleReponse, this:: handleError));
    }

    // onNext
    private void handleReponse(List<Task> tasks) {
        processTasks(tasks);
    }

    // onError
    private void handleError(Throwable throwable) {
        Log.d("TASKLOG", "error: " + throwable.getMessage());
        mTaskView.showLoadingTasksError();
    }

    private void processTasks(List<Task> tasks) {

        if (tasks.isEmpty()){
            processEmptyTasks();
        }
        else {
            mTaskView.showTask(tasks);
            showFilterLabel();
        }
    }

    private void showFilterLabel() {

        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTaskView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTaskView.showCompletedFilterLabel();
                break;
            default:
                mTaskView.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {

        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTaskView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTaskView.showNoCompletedTasks();
                break;
            default:
                mTaskView.showNoTasks();
                break;
        }
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void clearCompletedTasks() {
        mTaskRespository.clearCompletedTasks();
        mTaskView.showCompletedTasksCleared();
        loadTasks();
    }

    @Override
    public void completeTask(Task completedTask) {
        mTaskRespository.completeTask(completedTask);
        mTaskView.showTaskMarkedComplete();
        loadTasks();
    }

    @Override
    public void activateTask(Task activeTask) {
        mTaskRespository.activateTask(activeTask);
        mTaskView.showTaskMarkedActive();
        loadTasks();
    }

    @Override
    public void openTaskDetails(Task requestedTask) {
        mTaskView.showTaskDetailsUi(requestedTask.getmId());
    }
}
