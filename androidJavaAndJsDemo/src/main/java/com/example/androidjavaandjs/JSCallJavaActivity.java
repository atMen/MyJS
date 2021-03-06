package com.example.androidjavaandjs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

/**
 * JS调用Java代码
 * 
 * @author 杨光福
 * 
 */
public class JSCallJavaActivity extends Activity {

	private Handler handler = new Handler();

	public static final String TAG = JSCallJavaActivity.class.getSimpleName();
	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_javascript_call_java);
		// 加载页面
		webView = (WebView) findViewById(R.id.webview);
		// 允许JavaScript执行
		webView.getSettings().setJavaScriptEnabled(true);
		// 找到Html文件，也可以用网络上的文件
//		webView.loadUrl("file:///android_asset/JsCallJava.html");
		webView.loadUrl("http://10.0.2.2:8080/JsCallJava.html");
		// 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
		webView.addJavascriptInterface(new Contact(), "yangguangfu");
	}

	private final class Contact {
		// JavaScript调用此方法拨打电话
		public void call(String phone) {
			Log.e(TAG, "java --call(String phone)---==" + phone);
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone)));
		}
		
		public void playVideo(String videoUrl) {
			Log.e(TAG, "java --playVideo(String videoUrl)---==" + videoUrl);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(videoUrl), "video/*");
			startActivity(intent);
		}

		// Html调用此方法传递数据
		public void showcontacts() {
			
			handler.post(new Runnable() {

				@Override
				public void run() {
					Log.e(TAG, "java --showcontacts---");
					// 下面的代码建议在子线程中调用
					String json = "[{\"name\":\"阿福\", \"amount\":\"100000\", \"phone\":\"18600012345\"}]";
					// 调用JS中的方法
					webView.loadUrl("javascript:show('" + json + "')");
					
					//视频信息
					String jsonVideo = "[{\"name\":\"我是视频点击播放\", \"amount\":\"9999\", \"phone\":\"http://10.0.2.2:8080/oppo.mp4\"}]";
					webView.loadUrl("javascript:showvideo('" + jsonVideo + "')");

				}
			});

		}

		// Html调用此方法传递数据
		public void showvideourl() {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Log.e(TAG, "java --showvideourl---");
					// 下面的代码建议在子线程中调用
					String json = "[{\"name\":\"yangguangfu\", \"amount\":\"100000\", \"phone\":\"18600012345\"}]";
					// 调用JS中的方法
					webView.loadUrl("javascript:showvideo('" + json + "')");

				}
			});

		}
	}
}
