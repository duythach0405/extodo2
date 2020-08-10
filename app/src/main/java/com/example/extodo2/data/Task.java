package com.example.extodo2.data;

import com.google.common.base.Strings;

import java.util.UUID;

public final class Task {
    private String mId;
    private String mTitle;
    private String mDescription;
    private boolean mCompleted;

    public Task( String mTitle, String mDescription,String mId, boolean mCompleted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mCompleted = mCompleted;
    }

    public Task(String mTitle, String mDescription,String mId ) {
        this(mTitle, mDescription, mId, false);
    }

    public Task(String mTitle, String mDescription) {
        this(mTitle, mDescription, UUID.randomUUID().toString(), false);
    }

    public Task(String mTitle, String mDescription, boolean mCompleted) {
        this(mTitle, mDescription, UUID.randomUUID().toString(), mCompleted);
    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public boolean ismCompleted() {
        return mCompleted;
    }

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(mTitle) && Strings.isNullOrEmpty(mDescription);
    }

    public boolean isActive() {
        return !mCompleted;
    }

    // hiển thị chủ để
    public String getTitleForList(){
        if (!Strings.isNullOrEmpty(mTitle)){
            return mTitle;
        }
        return mDescription;
    }

}
