package com.example.extodo2.activity;

import com.example.extodo2.BasePresenter;
import com.example.extodo2.BaseView;
import com.example.extodo2.data.Task;

import java.util.List;

public interface TaskContracts {

    interface View extends BaseView<Presenter> {

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

       void showFilteringPopUpMenu();

        void showCompletedTasksCleared();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showTaskDetailsUi(String taskId);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void addNewTask();

        void loadTasks(boolean forceUpdate);

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();

        void clearCompletedTasks();

        void completeTask( Task completedTask);

        void activateTask( Task activeTask);

        void openTaskDetails(Task requestedTask);
    }
}
