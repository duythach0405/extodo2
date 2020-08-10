package com.example.extodo2.activity;

import com.example.extodo2.BasePresenter;
import com.example.extodo2.BaseView;
import com.example.extodo2.data.Task;

import java.util.List;

public interface TaskContracts {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showAddTask();

        void showSuccessfullySaveMessage();

        void showLoadingTasksError();

        void showTask(List<Task> tasks);

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void addNewTask();

        void loadTasks(boolean forceUpdate);

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();
    }
}
