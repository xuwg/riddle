package com.children.peter.riddle;

/**
 * Created by Administrator on 2017/8/4.
 */

public class Riddle {

    private String content;
    private String key;

    public Riddle(String content, String key) {
        this.content = content;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }
}
