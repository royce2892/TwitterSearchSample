package com.twittersearch.cleartax;

/**
 * Created by royce on 15-06-2016.
 */
public class Keyword {

    String key;
    int count;

    public Keyword(String key, int count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
