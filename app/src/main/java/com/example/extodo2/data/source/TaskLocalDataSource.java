package com.example.extodo2.data.source;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.extodo2.data.Task;
import com.example.extodo2.data.TaskDataSource;
import com.google.common.base.Optional;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TaskLocalDataSource implements TaskDataSource {
    private static TaskLocalDataSource INSTANCE;

    private final BriteDatabase mDatabaseHelper;

    private Function<Cursor, Task> mTaskMapperFunction;

    private TaskLocalDataSource(Context context) {

        // khởi tạo database
        TaskDbHelper dbHelper = new TaskDbHelper(context);

        // khởi tạo sqlbrite
        SqlBrite sqlBrite = new SqlBrite.Builder().build();

        // thao tác vào sqlbrite database
        this.mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());

        mTaskMapperFunction = this::getTask;

    }

    private Task getTask(Cursor cursor){
        String title = cursor.getString(cursor.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE));
        String decscription = cursor.getString(cursor.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
        String id = cursor.getString(cursor.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID));
        boolean complete = cursor.getInt(cursor.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED))==1;

        return new Task(title, decscription, id, complete);
    }

    public static TaskLocalDataSource getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = new TaskLocalDataSource(context);
        }

        return INSTANCE;
    }

    @Override
    public Flowable<List<Task>> getTasks() {

        String[] p = {TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED};

        String sql = String.format("SELECT %s FROM %s", TextUtils.join("",p),
                TasksPersistenceContract.TaskEntry.TABLE_NAME);

        return mDatabaseHelper.createQuery(TasksPersistenceContract.TaskEntry.TABLE_NAME,sql)
                .mapToList(mTaskMapperFunction)
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Optional<Task>> getTask(String taskId) {

        String[] p = {TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED};

        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE %s", TextUtils.join("",p),
                TasksPersistenceContract.TaskEntry.TABLE_NAME,taskId);

        return mDatabaseHelper.createQuery(TasksPersistenceContract.TaskEntry.TABLE_NAME, sql, taskId)
                .mapToOneOrDefault(cursor -> Optional.of(mTaskMapperFunction.apply(cursor)), Optional.<Task>absent())
                .toFlowable(BackpressureStrategy.BUFFER);
    }


    @Override
    public void saveTask(Task task) {
        ContentValues values = new ContentValues();

        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE, task.getmTitle());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getmDescription());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID, task.getmId());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED, task.ismCompleted());

        mDatabaseHelper.insert(TasksPersistenceContract.TaskEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void completeTask(Task task) {
        completeTask(task.getmId());
    }

    @Override
    public void completeTask(String taskId) {
        ContentValues values = new ContentValues();

        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + "LIKE ? ";

        String[] selectionArgs = {taskId};

        mDatabaseHelper.update(TasksPersistenceContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public void activateTask(Task task) {
        activateTask(task.getmId());
    }

    @Override
    public void activateTask(String taskId) {
        ContentValues values = new ContentValues();

        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        String[] selectionArgs = {taskId};

        mDatabaseHelper.update(TasksPersistenceContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public void clearCompletedTasks() {
        String selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + "LIKE ?";

        String[] selectionArgs = {"1"};

        mDatabaseHelper.delete(TasksPersistenceContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void deleteAllTasks() {
        mDatabaseHelper.delete(TasksPersistenceContract.TaskEntry.TABLE_NAME, null);
    }

    @Override
    public void deleteTask(String taskId) {
        String selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + "LIKE ?";
        String[] selectionArgs = {taskId};
        mDatabaseHelper.delete(TasksPersistenceContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }
}
