package com.example.username.mytodo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user.name on 2017/01/25.
 */

public class Todo extends RealmObject {
    @PrimaryKey
    private long id;
    private long categoryId;
    private String title;
    private boolean done;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
