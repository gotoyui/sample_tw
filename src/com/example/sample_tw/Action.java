package com.example.sample_tw;

// スレッドを使いたいので、ActionクラスにインターフェイスRunnableを実装
public class Action implements Runnable {

	// TwitterAdapter型のadapterを宣言
	private TwitterAdapter adapter;

	// ActionメソッドはTwitterAdapter型の_adapterを引数もつ
	public Action(TwitterAdapter _adapter) {
		adapter = _adapter;
	}

	// 引数のないRunメソッドでadapter(listview)を更新する
	@Override
	public void run() {
		adapter.notifyDataSetChanged();
	}
}