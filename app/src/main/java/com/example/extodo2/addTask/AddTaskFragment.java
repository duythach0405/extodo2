package com.example.extodo2.addTask;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.extodo2.R;
import com.example.extodo2.activity.TaskContracts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskFragment extends Fragment implements AddTaskContract.View {
    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    AddTaskContract.Presenter mPresenter;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtDescription) EditText edtDescription;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    public static  AddTaskFragment newInstant(){
        return new AddTaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fabDone = getActivity().findViewById(R.id.fab_edit_task_done);
        fabDone.setImageResource(R.drawable.ic_done);
        fabDone.setOnClickListener(__ -> mPresenter.saveTask(edtTitle.getText().toString(), edtDescription.getText().toString()));
    }

    @Override
    public void setPresenter(AddTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(edtTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        edtTitle.setText(title);
    }

    @Override
    public void setDesc(String desc) {
        edtDescription.setText(desc);
    }
}
