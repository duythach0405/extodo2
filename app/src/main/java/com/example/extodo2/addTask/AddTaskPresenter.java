package com.example.extodo2.addTask;

import android.view.View;

import com.example.extodo2.data.Task;
import com.example.extodo2.data.TaskDataSource;
import com.google.common.base.Optional;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class AddTaskPresenter implements AddTaskContract.Presenter {
    private AddTaskContract.View mAddTaskView;
    private TaskDataSource mTasksRepository;
    private CompositeDisposable compositeDisposable;
    private String mTaskId;

    public AddTaskPresenter(AddTaskContract.View mAddTaskView, TaskDataSource mTasksRepository, String mTaskId) {
        this.mAddTaskView = mAddTaskView;
        this.mTasksRepository = mTasksRepository;
        this.mTaskId = mTaskId;

        compositeDisposable = new CompositeDisposable();
        mAddTaskView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void saveTask(String tittle, String description) {
        if (isNewTask()){
            createTask(tittle, description);
        } else {
            updateTask(tittle, description);
        }
    }

    @Override
    public void populateTask() {

    }

    private void updateTask(String tittle, String description) {
        compositeDisposable.add(mTasksRepository.getTask(mTaskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this:: handleReponse, this:: handleError));
    }

    private void handleReponse(Optional<Task> taskOptional) {
        Task task = taskOptional.get();

        mAddTaskView.setTitle(task.getmTitle());

        mAddTaskView.setDesc(task.getmDescription());
    }

    private void handleError(Throwable throwable) {

    }

    private void createTask(String tittle, String description) {
        Task newtask = new Task(tittle, description);

        if (newtask.isEmpty()){
            mAddTaskView.showEmptyTaskError();
        }
        else {
            mTasksRepository.saveTask(newtask);
            mAddTaskView.showTasksList();  // sau khi edit, quay lại tới List

        }
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }
}
