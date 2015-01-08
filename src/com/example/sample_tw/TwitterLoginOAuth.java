package com.example.sample_tw;

import android.app.Activity;
import android.os.Bundle;
import java.io.*;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;
import twitter4j.Status;

public class TwitterLoginOAuth {

	// コンシューマ・キーとコンシューマ・シークレット
	private static final String CALLBACK = "www.surugadai.ac.jp"; // コールバック先のURL（PINを指定する場合はいらない）
	private static final String m_ConsumerKey = "SjaTsufLGmFsVyFCwfA28eekb";
	private static final String m_ConsumerSecret = "5fF7B1eQSBxnfw7gVbLM33swhaJ6AXkDrs8pUYE4qboDK3K4i1";

	public static void main(String[] args) {

		Twitter twitter;

		// アクセストークンの読み込み、別ブラウザでの認証
		AccessToken accessToken = loadAccessToken();

		// アクセストークンが既に保存されていれば，それを利用して
		// Twitter 認証を行う．さもなくば，アクセストークンを取りにいく．
		try {
			if (accessToken != null) {
				// 自分のディレクトリに保存していた Access Token を利用する．
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(m_ConsumerKey, m_ConsumerSecret);
				twitter.setOAuthAccessToken(accessToken);
			} else {
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(m_ConsumerKey, m_ConsumerSecret);
				// アクセストークンの取得
				accessToken = getOAuthAccessToken(twitter);

				// 自分のディレクトリに Access Token を保存しておく
				storeAccessToken(accessToken);
			}

			// 自分のステータスの更新（＝ツイートの投稿）
			Status status = twitter.updateStatus(args[0]);
			System.out.println("Successfully updated the status to ["
					+ status.getText() + "].");
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	// アクセストークンの取得
	static AccessToken getOAuthAccessToken(Twitter twitter) {
		RequestToken requestToken = null;
		AccessToken accessToken = null;

		try {
			// リクエストトークンの作成
			// (メモ) アクセストークンを取得後，保存して再利用するならば
			// リクエストトークンの作成は１度きりでよい．
			requestToken = twitter.getOAuthRequestToken();

			// ブラウザで Twitter 認証画面を表示するよう促し，
			// PIN コードを入力させ，アクセストークンを作成（取得）する
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			while (null == accessToken) {
				System.out
						.println("Open the following URL and grant access to your account:");
				System.out.println(requestToken.getAuthorizationURL());
				System.out
						.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
				String pin = br.readLine();
				try {
					if (pin.length() > 0) {
						accessToken = twitter.getOAuthAccessToken(requestToken,
								pin);
					} else {
						accessToken = twitter.getOAuthAccessToken();
					}
				} catch (TwitterException te) {
					if (401 == te.getStatusCode()) {
						System.out.println("Unable to get the access token.");
					} else {
						te.printStackTrace();
					}
				}
			} // while()
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return accessToken;
	}

	// アクセストークンの読み込み
	private static AccessToken loadAccessToken() {
		File f = createAccessTokenFileName();

		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(f));
			AccessToken accessToken = (AccessToken) is.readObject();
			return accessToken;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// アクセストークンの保存
	private static void storeAccessToken(AccessToken accessToken) {
		// ファイル名の生成
		File f = createAccessTokenFileName();

		// 親ディレクトリが存在しない場合，親ディレクトリを作る．
		File d = f.getParentFile();
		if (!d.exists()) {
			d.mkdirs();
		}

		// ファイルへの書き込み
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(f));
			os.writeObject(accessToken);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// アクセストークンを保存するファイル名を生成する
	static File createAccessTokenFileName() {
		// (メモ) System.getProperty("user.home") の返し値は
		// ホームディレクトリの絶対パス
		String s = System.getProperty("user.home")
				+ "/.twitter/client/sample/accessToken.dat";
		return new File(s);
	}
}
