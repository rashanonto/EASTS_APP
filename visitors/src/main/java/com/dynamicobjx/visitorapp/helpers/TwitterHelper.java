package com.dynamicobjx.visitorapp.helpers;

import android.content.res.Configuration;

import com.parse.twitter.Twitter;

import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class TwitterHelper {

    private static final String CONSUMER_KEY = "LpU2k05SdzMOBdJdp8zVNRAc5";
    private static final String CONSUMER_SECRET_KEY = "niLXABwk2OUSQGPkM3N46RTXp3nLaplRgPZshlMk2U9rC92Z0S";
    private static final String ACCESS_TOKEN = "2964954822-R1fixZO0taFN221yFG00bEnObe29snv8E8JObzr";
    private static final String ACCESS_TOKEN_SECRET = "wDdLStAk6cu6i73V5dFWxCJgeEYd5NUPZcrvRBOsMdPQw";
    private twitter4j.Twitter twitter;
    private twitter4j.conf.Configuration config;

    public void initTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET_KEY)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        config = cb.build();
        TwitterFactory factory = new TwitterFactory(config);
        twitter = factory.getInstance();
    }

    public twitter4j.Twitter getTwitter() {
        return twitter;
    }

    public twitter4j.conf.Configuration getConfig() {
        return config;
    }


}
