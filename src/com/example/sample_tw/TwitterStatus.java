package com.example.sample_tw;

// 名前とつぶやきを保持するシンプルなクラス
public class TwitterStatus {
	private String screenName;
	private String text;
	private int time;
	private long id;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// screenNameをgetしてsetする
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	// textをgetしてsetする
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	// timeをgetしてsetする
	public int getTime() {
		return time;
	}

	

	// public void setTime(int Time) {
	// this.time = time;
	// }

}