package com.example.username.mytodo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user.name on 2017/01/22.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private long id;
    private String category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
