package com.example.extodo2.addTask;

import com.example.extodo2.BasePresenter;
import com.example.extodo2.BaseView;
import com.example.extodo2.activity.TaskContracts;

public interface AddTaskContract {
    interface View extends BaseView<Presenter> {

        void setPresenter(Presenter presenter);

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDesc(String desc);
    }

    interface Presenter extends BasePresenter {

        void saveTask(String tittle, String description);

        void populateTask();
    }
}
