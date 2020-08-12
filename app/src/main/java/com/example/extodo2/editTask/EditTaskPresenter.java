package com.example.extodo2.editTask;

import com.example.extodo2.data.Task;
import com.example.extodo2.data.TaskRespository;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EditTaskPresenter implements EditTaskContract.Presenter {

    private String taskId;

    private TaskRespository mTaskRespository;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private EditTaskContract.View mEditTaskView;

    public EditTaskPresenter(String taskId, EditTaskContract.View mTaskView, TaskRespository mTaskRespository) {
        this.taskId = taskId;
        this.mEditTaskView = mTaskView;
        this.mTaskRespository = mTaskRespository;

        mEditTaskView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void subscribe() {
        openTask();
    }

    private void openTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            mEditTaskView.showMissingTask();
            return;
        }
        mCompositeDisposable.add(mTaskRespository
                .getTask(taskId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        this::showTask,
                        // onError
                        throwable -> {
                        }, () -> mEditTaskView.setLoadingIndicator(false)));
    }

    private void showTask(Task task) {
        String title = task.getmTitle();
        String description = task.getmDescription();

        if (Strings.isNullOrEmpty(title)) {
            mEditTaskView.hideTitle();
        } else {
            mEditTaskView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            mEditTaskView.hideDescription();
        } else {
            mEditTaskView.showDescription(description);
        }
        mEditTaskView.showCompletionStatus(task.ismCompleted());
    }


    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(taskId)){
            mEditTaskView.showMissingTask();
        }
        mEditTaskView.showEditTask(taskId);
    }

    @Override
    public void deleteTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            mEditTaskView.showMissingTask();
            return;
        }

        mTaskRespository.deleteTask(taskId);
        mEditTaskView.showTaskDeleted();
    }

    @Override
    public void completeTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            mEditTaskView.showMissingTask();
            return;
        }
        mTaskRespository.completeTask(taskId);
        mEditTaskView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            mEditTaskView.showMissingTask();
            return;
        }
        mTaskRespository.activateTask(taskId);
        mEditTaskView.showTaskMarkedActive();
    }
}
