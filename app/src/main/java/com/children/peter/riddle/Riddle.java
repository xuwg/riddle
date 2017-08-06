package com.children.peter.riddle;

/**
 * Created by Administrator on 2017/8/4.
 */

public class Riddle {


    private int id;
    private int category;
    private int type;
    private String content;
    private String key;

    public Riddle(String content, String key) {
        this.content = content;
        this.key = key;
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
