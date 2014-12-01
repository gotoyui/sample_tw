package com.example.sample_tw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

// スレッドを継承したGetHomeTimelineクラス
public class GetHomeTimeline extends Thread {

	// GetHomeTimelineメソッドはArrayList型の_items, MainActivity型の_activity,
	// TwitterAdapter型の_adapterを引数にもつ
	public GetHomeTimeline(ArrayList _items, MainActivity _activity,
			TwitterAdapter _adapter) {
		items = _items;
		activity = _activity;
		adapter = _adapter;
	}

	// items, activity, adapterの宣言
	private ArrayList items;
	private MainActivity activity;
	private TwitterAdapter adapter;

	public void run() {
		try {
			// gets Twitter instance with default credentials
			Twitter twitter = new TwitterFactory().getInstance();
			User user = twitter.verifyCredentials();
			// saishinはitemsの中のいちばん新しいやつ！！
			List<Status> statuses;
			if(items.size() > 0) {
				TwitterStatus saishin = (TwitterStatus)(items.get(0));
				// 最新のIDから20こもらってくる！！
				Paging paging = new Paging(saishin.getId());
				statuses = twitter.getHomeTimeline(paging);
			} else {
				statuses = twitter.getHomeTimeline();
			}
			
			
			for (int l = statuses.size()-1; 0<=l; l--) {
				TwitterStatus tweet = new TwitterStatus();
				Status status = statuses.get(l);
				tweet.setScreenName(status .getUser().getScreenName());
				tweet.setText(status.getText());
				items.add(0, tweet);
				tweet.setId(status.getId());
			}

			// スレッド(Actionクラス)で処理
			// UlThreadはMainActivityをしてる人のこと。actionメソッドを動かしてもらう！
			activity.runOnUiThread(new Action(adapter));

			// 例外処理
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("読み込みに失敗しました〜！" + te.getMessage());
			System.exit(-1);
		}
	}
}
