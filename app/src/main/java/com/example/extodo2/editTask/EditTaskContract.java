package com.example.extodo2.editTask;

import com.example.extodo2.BasePresenter;
import com.example.extodo2.BaseView;
import com.example.extodo2.activity.TaskContracts;
import com.google.common.base.Strings;

public interface EditTaskContract {
    interface View extends BaseView<Presenter>{

        void showMissingTask();

        void showEditTask(String taskId);

        void showTaskDeleted();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();
    }
}
