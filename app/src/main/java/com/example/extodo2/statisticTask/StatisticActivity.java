package com.example.extodo2.statisticTask;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.extodo2.R;
import com.example.extodo2.data.TaskRespository;
import com.example.extodo2.data.source.TaskLocalDataSource;
import com.example.extodo2.util.ActivityUtils;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatisticActivity extends AppCompatActivity {
    @BindView(R.id.dl)
    DrawerLayout dl;

    @BindView(R.id.tb)
    Toolbar tb;

    @BindView(R.id.nv)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        ButterKnife.bind(this);

        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.statistics_title);

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dl.setStatusBarBackground(R.color.colorPrimaryDark);

        if (navigationView != null){
            setupDrawerContent(navigationView);
        }

        StatisticTaskFragment taskFragment = (StatisticTaskFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);

        if (taskFragment== null){

            taskFragment = StatisticTaskFragment.newInstance();
            ActivityUtils.addFragmentActivity(getSupportFragmentManager(),taskFragment, R.id.flContent);
        }

        new  StatisticPresenter(taskFragment, TaskRespository.getInstance(TaskLocalDataSource.getInstance(getApplicationContext())));
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.list_nv:
                    onBackPressed();
                    break;

                case R.id.statistics_nv:
                    break;

                default:
                    break;
            }
            item.setChecked(true);
            dl.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
