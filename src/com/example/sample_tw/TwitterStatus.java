package com.example.sample_tw;

// 名前とつぶやきを保持するシンプルなクラス
public class TwitterStatus {
	private String screenName;
	private String text;
	private long id;
	private String url;

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

	// urlをgetしてsetする
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}