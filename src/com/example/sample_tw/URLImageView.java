package com.example.sample_tw;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twitter4j.Status;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

public class URLImageView extends ImageView {

	public URLImageView(Context context) {
		super(context);
	}

	public URLImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public URLImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setUrl(final String src) {
		// future task を利用してスレッドをつくる。画像をもらってくる！！
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Bitmap image = null; // 初期化
		try {
			image = exec.submit(new Callable<Bitmap>() {
				@Override
				public Bitmap call() throws Exception {
					Bitmap image2;
					URL url = new URL(src); // URLの画像をもらってくるtry&catch
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					image2 = BitmapFactory.decodeStream(input);
					return image2;
				}
			}).get();
			if (image != null) { // もらってきたURLをBitmap型のimageに入れる！
				setImageBitmap(image);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
