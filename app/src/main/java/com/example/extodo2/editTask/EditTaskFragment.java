package com.example.extodo2.editTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.extodo2.R;
import com.example.extodo2.addTask.AddTaskActivity;
import com.example.extodo2.addTask.AddTaskFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class EditTaskFragment extends Fragment implements EditTaskContract.View{
    private static final String ARGUMENT_TASK_ID = "TASK_ID";

    private static final int REQUEST_EDIT_TASK = 1;

    private  EditTaskContract.Presenter mPresenter;

    private  TextView mTitle;
    private  TextView mDescription;
    private  CheckBox mComplete;

    public static EditTaskFragment newInstance(String taskId){
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        mTitle       = view.findViewById(R.id.tvDetailTittle);
        mDescription = view.findViewById(R.id.tvDetailDescription);
        mComplete    = view.findViewById(R.id.cbDetailComplete);

        FloatingActionButton fab = getActivity().findViewById(R.id.fabEditStask);
        fab.setOnClickListener(__ -> mPresenter.editTask());

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.staskdetail_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:
                mPresenter.deleteTask();
                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showMissingTask() {
        mTitle.setText("");
        mDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void showEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void hideTitle() {
        mTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        mDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mDescription.setVisibility(View.VISIBLE);
        mDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        mComplete.setChecked(complete);
        mComplete.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        mPresenter.completeTask();
                    } else {
                        mPresenter.activateTask();
                    }
                });
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mTitle.setText("");
            mDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void setPresenter(EditTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
