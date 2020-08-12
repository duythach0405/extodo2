package com.example.extodo2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.extodo2.R;
import com.example.extodo2.data.TaskRespository;
import com.example.extodo2.data.source.TaskLocalDataSource;
import com.example.extodo2.statisticTask.StatisticActivity;
import com.example.extodo2.util.ActivityUtils;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private TasksPresenter mTasksPresenter;
    @BindView(R.id.tb)
    Toolbar tb;
    @BindView(R.id.dl)
    DrawerLayout dl;
    @BindView(R.id.nv)
    NavigationView nv;
    @BindView(R.id.ivHome) ImageView ivHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // set up ToolBar on ActionBar
        setSupportActionBar(tb);

        //set up navigation drawer
        dl.setStatusBarBackground(R.color.colorPrimaryDark);

        // set item in navigation
        if(nv != null){
            setItemNavigation(nv);
        }

        // add fragment vào activity
        TaskFragment taskFragment = (TaskFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (taskFragment== null){
            taskFragment = TaskFragment.newInstance();
            ActivityUtils.addFragmentActivity(getSupportFragmentManager(),taskFragment,R.id.flContent);
        }

        // khởi tạo instance presenter
        mTasksPresenter = new TasksPresenter(taskFragment,
                TaskRespository.getInstance(TaskLocalDataSource.getInstance(getApplicationContext())));

        if (savedInstanceState != null) { TasksFilterType currentFiltering =
                (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
        }
    }

    // lưu trạng thái hiện tại
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }


    private void setItemNavigation(NavigationView nv) {
        nv.setNavigationItemSelectedListener(MenuItem ->{
            switch (MenuItem.getItemId()) {
                case R.id.list_nv:
                    break;
                case R.id.statistics_nv:
                    Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                    startActivity(intent);
                    break;
            }
            dl.closeDrawers();
            return true;
        });
    }

    @OnClick(R.id.ivHome)
    public void HomeClick(){
        dl.openDrawer(GravityCompat.START);
    }
}
