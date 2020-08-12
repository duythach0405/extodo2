package com.example.extodo2.statisticTask;

import com.example.extodo2.BasePresenter;
import com.example.extodo2.BaseView;

public interface StatisticContract {

    interface View extends BaseView<Presenter> {

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        void showLoadingStatisticsError();


    }

    interface Presenter extends BasePresenter {

    }
}
