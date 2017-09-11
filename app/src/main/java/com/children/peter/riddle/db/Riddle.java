package com.children.peter.riddle.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/4.
 */

public class Riddle extends DataSupport {


    private int id;
    private int category;
    private int type;
    private String content;
    private String key;

    public Riddle(int type, int category, String content, String key) {
        this.content = content;
        this.key = key;
        this.type = type;
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
