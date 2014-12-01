//package com.example.sample_tw;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.widget.ArrayAdapter;
//
//public class SwipeRefreshLayoutBasicFragment {
//
//	private SwipeRefreshLayout mSwipeRefreshLayout;
//	private ListView ListView;
//	private ArrayAdapter<String> adapter;
//
//
//
//public void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//
//	// SwipeRefreshLayoutの設定
//	mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
//	// 色設定
//	mSwipeRefreshLayout.setColorScheme(R.color.swipe_color_1, R.color.swipe_color_2,
//			R.color.swipe_color_3, R.color.swipe_color_4);
//	// 	リスナーをセット
//	mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
//
//
//	private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//		@Override
//		public void onRefresh() {
//			// 更新処理を実装
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					// 更新が終了したらインジケータ非表示
//					mSwipeRefreshLayout.setRefreshing(false);
//				}
//			}, 1000); // 3秒待機
//		}