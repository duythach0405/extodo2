package com.example.extodo2.addTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.extodo2.R;
import com.example.extodo2.activity.TaskContracts;
import com.example.extodo2.data.TaskRespository;
import com.example.extodo2.data.source.TaskLocalDataSource;
import com.example.extodo2.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class AddTaskActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_TASK = 1;

    private ActionBar actionBar;
    private AddTaskPresenter mAddEditTaskPresenter;

    @BindView(R.id.tb) Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        setSupportActionBar(tb);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);  //  tên TO-DO-MVP
        actionBar.setDisplayShowHomeEnabled(true);  //  dấu back

        String taskId = getIntent().getStringExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID);
        setToolbarTitle(taskId);

        AddTaskFragment addstaskFragment = (AddTaskFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);

        if (addstaskFragment == null){
            addstaskFragment = AddTaskFragment.newInstant();
            ActivityUtils.addFragmentActivity(getSupportFragmentManager(), addstaskFragment, R.id.flContent);
        }

        // tạo presenter
        mAddEditTaskPresenter = new AddTaskPresenter(addstaskFragment,
                TaskRespository.getInstance(TaskLocalDataSource.getInstance(getApplicationContext())),taskId);
    }

    private void setToolbarTitle(String staskId){
        if (staskId == null){
            actionBar.setTitle(R.string.add_task);
        }
        else
            actionBar.setTitle(R.string.edit_task);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
