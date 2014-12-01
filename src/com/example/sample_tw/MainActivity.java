package com.example.sample_tw;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twitter4j.*;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.Build;

// 基本となるリスト
public class MainActivity extends Activity {

	// メンバ変数を宣言！！
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ArrayList items;
	// ListViewとArrayListをくっつけるために、TwitterAdapter型のadapterをつくる
	TwitterAdapter adapter;
	MainActivity activity;
	private Button tweet_before;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// fragment_mainをよみこむ
		setContentView(R.layout.fragment_main);

		// ArrayList型のitems(ListViewの1行のデータのこと)をつくる。配列はデータをいれる箱！！！
		// 配列は箱の数を指定しなくてはならないが、ArrayListは無限に入れられる！！！！！
		items = new ArrayList(); // まだからっぽ

		// SwipeRefreshLayoutの設定
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
		// 色設定
		mSwipeRefreshLayout.setColorScheme(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3,
				R.color.swipe_color_4);
		// リスナーをセット
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

		// itemとadapterを接続！
		adapter = new TwitterAdapter(this, R.layout.twitter_row, items);

		// fragmentからlistviewを探してくる。
		// id(int型)で要求するとView型で返ってくるので、listview型にキャストする
		ListView listview = (ListView) findViewById(R.id.listview);
		// adapterに接続！
		listview.setAdapter(adapter);

		
		
		
		
		// コレはインスタンス。クラスはインスタンスにしないと使えない！！！ローカル変数を宣言！！
	//	GetHomeTimeline gethometimeline = new GetHomeTimeline(items, this,
	//			adapter);
	//	gethometimeline.start();
		
		
		
		
		// future task を利用してスレッドをつくる
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.submit(new Callable<Status>() {

			@Override
			public Status call() throws Exception {

				// gets Twitter instance with default credentials
				Twitter twitter = new TwitterFactory().getInstance();
				User user = twitter.verifyCredentials();
				// saishinはitemsの中のいちばん新しいやつ
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
				
				return status;

			}

		});

		
		
		
		
		
		
		
		
		
		

		// MainActivityをactivityという名前のメンバ変数にしました
		activity = this;

		// fragment_mainのbuttonの遷移先を指定！！
		Button btnMove = (Button) findViewById(R.id.tweet_before);
		btnMove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NewTweet.class);
				startActivity(intent);
			}
		});
	}

	private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			// ツイートを新しく取得。さっき上で使ったローカル変数を宣言！！
			GetHomeTimeline gethometimeline = new GetHomeTimeline(items,
					activity, adapter);
			gethometimeline.start();

			// 更新処理を実装
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// 更新が終了したらインジケータ非表示
					mSwipeRefreshLayout.setRefreshing(false);
				}
			}, 1000); // 1秒待機
		}
	};

}