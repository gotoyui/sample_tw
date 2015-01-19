package com.example.sample_tw;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
	private ArrayList items; // ListViewの1行のデータのこと
	// ListViewとArrayListをくっつけるために、TwitterAdapter型のadapterをつくる
	TwitterAdapter adapter;
	MainActivity activity;
	private Button tweet_before;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("", "ツイッターはじめるよ〜〜！");

		// ActionBarにmenuを表示するときに忘れずつける！
		setHasOptionsMenu(true);

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

		// getHomeTimelineのメソッドを呼び出す
		getHomeTimeline();

		// MainActivityをactivityという名前のメンバ変数にしました
		activity = this;

	}

	// みんなが使いたいからonCreateの外に書いたやつ
	public void getHomeTimeline() {
		// future task を利用してスレッドをつくる。タイムラインを取得する！！
		ExecutorService exec = Executors.newSingleThreadExecutor();
		List<Status> statuses = null; // 初期化
		try {
			statuses = exec.submit(new Callable<List<Status>>() { // statusesに代入する
						@Override
						public List<Status> call() throws Exception {
							List<Status> results = null; // 初期化
							try {
								// gets Twitter instance with default
								// credentials
								Twitter twitter = new TwitterFactory()
										.getInstance();
								User user = twitter.verifyCredentials();
								// saishinはitemsの中のいちばん新しいやつ！！
								if (items.size() > 0) {
									TwitterStatus saishin = (TwitterStatus) (items
											.get(0));
									// 最新のIDから20こもらってくる！！
									Paging paging = new Paging(saishin.getId());
									results = twitter.getHomeTimeline(paging);
								} else {
									results = twitter.getHomeTimeline();
								}
								// 例外処理
							} catch (TwitterException te) {
								te.printStackTrace();
								System.out.println("読み込みに失敗しました〜！無念無念！理由は"
										+ te.getMessage());
								System.exit(-1);
							}
							return results;
						}
					}).get(); // をgetする（そして一番上に代入する）
		} catch (InterruptedException e) { // 例外処理
			e.printStackTrace();
		} catch (ExecutionException e) { // 例外処理
			e.printStackTrace();
		}

		for (int l = statuses.size() - 1; 0 <= l; l--) { // sizeはリストの数のこと。後ろから0になるまで1個ずつTwitterStatusにいれる。
			TwitterStatus tweet = new TwitterStatus();
			Status status = statuses.get(l);
			tweet.setScreenName(status.getUser().getScreenName());
			tweet.setText(status.getText());
			tweet.setName(status.getUser().getName());
			tweet.setUrl(status.getUser().getProfileImageURL());
			items.add(0, tweet); // itemsに追加する。0は一番新しいところ
			tweet.setId(status.getId()); // ツイートのID
		}
		// notifyDataSetChangedは、ListViewの更新を促す
		adapter.notifyDataSetChanged();
	}

	// くるくる更新のこまかい動きについてのやつ
	private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			// ツイートを新しく取得。さっき上で使ったローカル変数を宣言！！
			getHomeTimeline();
			Log.d("", "くるくる更新ちゅう〜");

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

	// ActionBarについての設定！！
	private void setHasOptionsMenu(boolean b) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list, menu);
		return true;
	}

	// ActionBarのイベントのハンドリング！
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			Log.d("", "ツイートが押されたよ〜");
			// NweTweetに遷移
			Intent intent = new Intent(MainActivity.this, NewTweet.class);
			startActivity(intent);

		case R.id.item2:
			Log.d("", "認証が押されたよ〜");
			// PINコード認証のあとコールバック まだ未設定　むずかしい・・・！
			final int REQUEST_ACCESS_TOKEN = 0;
			Intent intent2 = new Intent(MainActivity.this,
					TwitterLoginOAuth.class);
			startActivity(intent2);

		case R.id.item3:
			Log.d("", "とじるが押されたよ〜");
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}