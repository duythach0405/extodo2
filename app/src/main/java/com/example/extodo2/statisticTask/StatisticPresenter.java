package com.example.extodo2.statisticTask;

import android.util.Log;
import android.util.Pair;

import com.example.extodo2.data.Task;
import com.example.extodo2.data.TaskRespository;
import com.google.common.primitives.Ints;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StatisticPresenter implements StatisticContract.Presenter {

    private StatisticContract.View mStaticsView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TaskRespository mTaskRespository;

    public StatisticPresenter (StatisticContract.View mStaticsView, TaskRespository mTaskRespository) {
        this.mStaticsView = mStaticsView;
        this.mTaskRespository = mTaskRespository;

        mStaticsView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void subscribe() {
        loadStatistics();
    }

    private void loadStatistics() {

        Flowable<Task> tasks = mTaskRespository
                .getTasks()
                .take(1)
                .flatMap(Flowable::fromIterable);

        Flowable<Long> completedTasks = tasks.filter(Task::ismCompleted).count().toFlowable();
        Flowable<Long> activeTasks = tasks.filter(Task::isActive).count().toFlowable();

        Disposable disposable = Flowable
                .zip(completedTasks, activeTasks, (completed, active) -> Pair.create(active, completed))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        stats ->{
                            Log.d("STIDTICLOg", "size: " + stats.first);
                            mStaticsView.showStatistics(Ints.saturatedCast(stats.first), Ints.saturatedCast(stats.second));
                        } ,
                        // onError
                        throwable -> mStaticsView.showLoadingStatisticsError()
                       );
        mCompositeDisposable.add(disposable);
    }
}
