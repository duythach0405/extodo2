package com.example.extodo2.editTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.extodo2.R;
import com.example.extodo2.data.TaskRespository;
import com.example.extodo2.data.source.TaskLocalDataSource;
import com.example.extodo2.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";
    @BindView(R.id.tb) Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        EditTaskFragment editTaskFragment = (EditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);

        if (editTaskFragment == null){
            editTaskFragment = EditTaskFragment.newInstance(taskId);
            ActivityUtils.addFragmentActivity(getSupportFragmentManager(),editTaskFragment,R.id.flContent);
        }

        new EditTaskPresenter(taskId,
                editTaskFragment,
                TaskRespository.getInstance(TaskLocalDataSource.getInstance(getApplicationContext())));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
