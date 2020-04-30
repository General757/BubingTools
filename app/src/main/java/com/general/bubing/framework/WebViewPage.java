package com.general.bubing.framework;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bubing.framework.SingleActivity;
import com.general.bubing.R;

public class WebViewPage extends SingleActivity
{
	String title;
	String url;
	WebView webWebview;

	@Override
	public void onBuild()
	{
		super.onBuild();
		setContentView(R.layout.activity_webview);
		title = this.getExtras().getString("title");
		url = this.getExtras().getString("url");

		ImageView appBack = (ImageView) findViewById(R.id.appBack);
		appBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				close();
			}
		});
		TextView appTitle = (TextView) findViewById(R.id.appTitle);
		appTitle.setText(title);
		webWebview = (WebView) findViewById(R.id.webView);

		WebSettings webSet = webWebview.getSettings();
		webSet.setJavaScriptEnabled(true);
		webSet.setAllowFileAccess(true);
		webSet.setBuiltInZoomControls(true);
		webWebview.loadUrl(url);
		webWebview.setWebViewClient(new WebviewClient());

	}

	private class WebviewClient extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}

	}
}
