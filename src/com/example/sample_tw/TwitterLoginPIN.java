package com.example.sample_tw;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class TwitterLoginPIN extends Activity implements OnClickListener {
	public final static String EXTRA_CONSUMER_KEY = "SjaTsufLGmFsVyFCwfA28eekb";
	public final static String EXTRA_CONSUMER_SECRET = "5fF7B1eQSBxnfw7gVbLM33swhaJ6AXkDrs8pUYE4qboDK3K4i1";
	private OAuthAuthorization sOauth;
	private RequestToken sRequestToken;

	// public final static String EXTRA_ACCESS_TOKEN =
	// "601867423-g9Xh2jV8ipU0MLBrm8H0UurpTewLJnDMOWbPs34t";
	// public final static String EXTRA_ACCESS_TOKEN_SECRET =
	// "7FD6q9dKjr909WGV0EMdq7rOdalgFBlrlbrd11eFBs5zn";

	/**
	 * Twitterへ接続する！
	 */
	private void connectTwitter() {
		// Twitetr4jの設定を読み込む
		Configuration conf = ConfigurationContext.getInstance();
		// Oauth認証オブジェクト作成
		sOauth = new OAuthAuthorization(conf);
		// Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
		sOauth.setOAuthConsumer("consumerKey", "consumerSecret");

		// リクエストトークンの作成
		try {
			sRequestToken = sOauth.getOAuthRequestToken();
		} catch (TwitterException e) {
			String mTag = null;
			Log.e(mTag, e.toString());
			return;
		}
		String url = sRequestToken.getAuthorizationURL();
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}