package com.twittersearch.cleartax;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates how to use a twitter application keys to search
 */
public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "OSIxyphG2iFhhm4wnR0FaxTRh";
    private static final String TWITTER_SECRET = "VrZsBoyYisJCcFZPP2mtu1Q7qx9n2Ch2VSMpBRzqenW6L2gBNc";
    TwitterLoginButton loginButton;
    Map<String, Integer> map = new HashMap<>();
    ListView mTweetListView;
    TweetListAdapter mTweetListAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        mTweetListView = (ListView) findViewById(R.id.tweet_list);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                getTweets();
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }


            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
        loginButton.setVisibility(View.GONE);
    }

    private void getTweets() {

        Twitter.getApiClient().getSearchService().tweets("cleartax", null, null, null, null, 300, null, null, null, null, new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                for (Tweet tweet : result.data.tweets) {
                    if (!tweet.text.startsWith("I just e-Filed via ClearTax")) {
                        Log.i("RESULT", tweet.possiblySensitive + "");
                        String[] tweets = tweet.text.split(" ");
                        for (String word : tweets) {
                            if (map.containsKey(word))
                                map.put(word, map.get(word) + 1);
                            else
                                map.put(word, 1);
                        }
                    }
                }
                analyseTweets();

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }


    private void analyseTweets() {

        // sort the map containing key - value pairs to get the max three used keywords
        Map<String, Integer> tempMap = new HashMap<>(map);
        LinkedHashMap<String, Integer> sortedOutputMap = new LinkedHashMap<>();

        for (int i = 0; i < map.size(); i++) {
            Map.Entry<String, Integer> maxEntry = null;
            Integer maxValue = -1;
            for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    maxEntry = entry;
                }
            }
            tempMap.remove(maxEntry.getKey());
            sortedOutputMap.put(maxEntry.getKey(), maxEntry.getValue());
        }
        int i = 0;
        List<Keyword> keywordList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedOutputMap.entrySet()) {
            if (!entry.getKey().contentEquals("ClearTax")) {
                Log.i("MAPS", i + " : " + entry.getKey() + " : " + entry.getValue());
                keywordList.add(new Keyword(entry.getKey(),entry.getValue()));
            }
            i++;
            if (i == 4)
                break;
        }
        mTweetListAdapter = new TweetListAdapter(keywordList,this);
        mTweetListView.setAdapter(mTweetListAdapter);
    }


}
