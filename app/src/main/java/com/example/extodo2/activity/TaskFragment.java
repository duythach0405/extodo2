package com.example.extodo2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.extodo2.R;
import com.example.extodo2.addTask.AddTaskActivity;
import com.example.extodo2.data.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mPresenter.result(requestCode, resultCode);
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

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


    TaskItemListener taskItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {

        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {

        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {

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
                Context context;
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
