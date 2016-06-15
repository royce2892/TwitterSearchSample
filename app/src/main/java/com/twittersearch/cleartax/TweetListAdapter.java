package com.twittersearch.cleartax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by royce on 15-06-2016.
 */
public class TweetListAdapter extends BaseAdapter {

    List<Keyword> keywords = new ArrayList<>();
    LayoutInflater mLayoutInflater;

    public TweetListAdapter(List<Keyword> keywords, Context context) {
        this.keywords = keywords;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return keywords.size();
    }

    @Override
    public Object getItem(int position) {
        return keywords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Keyword keyword = (Keyword) getItem(position);
        convertView = mLayoutInflater.inflate(R.layout.row_tweet, parent, false);
        TextView mTweet = (TextView) convertView.findViewById(R.id.tweet);
        mTweet.setText(keyword.getKey()+" : "+ keyword.getCount()+" ");
        return convertView;
    }
}
