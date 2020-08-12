package com.example.extodo2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.extodo2.R;
import com.example.extodo2.addTask.AddTaskActivity;
import com.example.extodo2.data.Task;
import com.example.extodo2.editTask.EditTaskActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskFragment extends Fragment implements TaskContracts.View {

    private TaskContracts.Presenter mPresenter; // truyền view của đối tượng vào activity

    private TasksAdapter mListAdapter;

    private LinearLayout mNoTasksView;

    private ImageView mNoTaskIcon;

    private TextView mNoTaskMainView;

    private TextView mNoTaskAddView;

    private LinearLayout mTasksView;

    private TextView mFilteringLabelView;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(){
        return new TaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TasksAdapter(new ArrayList<>(0), taskItemListener);
    }

    @Override
    public void onResume() {
        mPresenter.loadTasks(true);
        super.onResume();
    }

    @Override
    public void onPause() {
        mPresenter.unsubscribe();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.item_clear:
                mPresenter.clearCompletedTasks();
                break;
            case R.id.item_refresh:
                mPresenter.loadTasks(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_task, container, false);

        FloatingActionButton fabAdd = getActivity().findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(__ -> mPresenter.addNewTask());

        mNoTasksView = view.findViewById(R.id.noTasks);
        mTasksView = view.findViewById(R.id.tasksLL);
        mNoTaskIcon = view.findViewById(R.id.ivTaskIcon);
        mNoTaskMainView = view.findViewById(R.id.tvTaskMain);
        mNoTaskAddView = view.findViewById(R.id.tvAdd);

        mNoTaskAddView.setOnClickListener(__ -> showAddTask());

        mFilteringLabelView = view.findViewById(R.id.filteringLabel);
        ListView listView = view.findViewById(R.id.lvList);
        listView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void setPresenter(TaskContracts.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        startActivityForResult(intent,AddTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showSuccessfullySaveMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showTask(List<Task> tasks) {

        mListAdapter.replaceData(tasks);

        mTasksView.setVisibility(View.VISIBLE);
        mNoTasksView.setVisibility(View.GONE);
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(getResources().getString(R.string.no_tasks_active), R.drawable.ic_check_circle_24dp, false);

    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(getResources().getString(R.string.no_tasks_completed), R.drawable.ic_verified_user_24dp, false);
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(getResources().getString(R.string.no_tasks_all), R.drawable.ic_assignment_turned_in_24dp, false);
    }

    @Override
    public void showActiveFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_all));

    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), getActivity().findViewById(R.id.item_filter));

        popupMenu.getMenuInflater().inflate(R.menu.filter_task, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_active:
                    mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                    break;
                case R.id.item_completed:
                    mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                    break;
                default:
                    mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                    break;
            }
            mPresenter.loadTasks(false);
            return true;
        });

        popupMenu.show();
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra(EditTaskActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mTasksView.setVisibility(View.GONE);
        mNoTasksView.setVisibility(View.VISIBLE);

        mNoTaskMainView.setText(mainText);
        mNoTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoTaskAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


    TaskItemListener taskItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };

    private static class TasksAdapter extends BaseAdapter {

        private List<Task> mTasks;
        private TaskItemListener mItemListener;

        public TasksAdapter(List<Task> mTasks, TaskItemListener mItemListener) {
            setList(mTasks);
            this.mItemListener = mItemListener;
        }
        public void replaceData(List<Task> tasks) {
            setList(tasks);
            notifyDataSetChanged();
        }

        private void setList(List<Task> tasks) {
            mTasks = checkNotNull(tasks);
        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Task getItem(int position) {
            return mTasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null){
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.item_task, parent, false);
            }

            Task task = mTasks.get(position);
            TextView titleTV = view.findViewById(R.id.tvTitle);
            titleTV.setText(task.getTitleForList());

            CheckBox completeCB = view.findViewById(R.id.cbCompleted);
            completeCB.setChecked(task.ismCompleted());

            if (task.ismCompleted()) {
                view.setBackgroundDrawable(parent.getContext()
                        .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
            } else {
                view.setBackgroundDrawable(parent.getContext()
                        .getResources().getDrawable(R.drawable.touch_feedback));
            }

            completeCB.setOnClickListener(__ -> {
                if (!task.ismCompleted()) {
                    mItemListener.onCompleteTaskClick(task);
                } else {
                    mItemListener.onActivateTaskClick(task);
                }
            });

            view.setOnClickListener(__-> mItemListener.onTaskClick(task));

            return view;
        }

    }

    public interface TaskItemListener{

        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);
    }
}
