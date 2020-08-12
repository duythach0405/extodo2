package com.example.extodo2.statisticTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.extodo2.R;

public class StatisticTaskFragment extends Fragment implements StatisticContract.View {

    private static StatisticTaskFragment statisticTaskFragment = null;

    private TextView tvStatistics;

    private StatisticContract.Presenter mStatisticPresenter;


    public static StatisticTaskFragment newInstance() {
        if (statisticTaskFragment == null){

            return new StatisticTaskFragment();
        }
        return statisticTaskFragment;
    }

    @Override
    public void onResume() {
        mStatisticPresenter.subscribe();
        super.onResume();
    }

    @Override
    public void onPause() {
        mStatisticPresenter.unsubscribe();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistic_task, container, false);

        tvStatistics = view.findViewById(R.id.tvStatistics);

        return view;
    }

    @Override
    public void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks) {
        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {

            tvStatistics.setText(getResources().getString(R.string.statistics_no_tasks));

        } else {

            String displayString = getResources().getString(R.string.statistics_active_tasks) + " "
                    + numberOfIncompleteTasks + "\n" + getResources().getString(
                    R.string.statistics_completed_tasks) + " " + numberOfCompletedTasks;
            tvStatistics.setText(displayString);
        }
    }

    @Override
    public void showLoadingStatisticsError() {
        tvStatistics.setText(getResources().getString(R.string.statistics_error));
    }

    @Override
    public void setPresenter(StatisticContract.Presenter presenter) {
        mStatisticPresenter = presenter;
    }
}
