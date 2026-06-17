package com.widger.neuvemepro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;

public class MainActivity extends Activity {

    private static final String GAS_URL = "https://script.google.com/macros/s/AKfycbwTnQK4MHIulnKL7V_9t-5vqAgsPz8VNM_SqgCt6zUYgjKlT8fpS-nBQvaHpOjY063X/exec";

    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout offlineLayout;
    private boolean isLoaded = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView      = findViewById(R.id.webview);
        progressBar  = findViewById(R.id.progress_bar);
        offlineLayout = findViewById(R.id.offline_layout);

        Button retryBtn = findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApp();
            }
        });

        setupWebView();

        // --- Rappel quotidien (notification locale, sans serveur) ---
        ReminderScheduler.createNotificationChannel(this);
        ReminderScheduler.scheduleDaily(this);
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }
        // --------------------------------------------------------------

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
            isLoaded = true;
        } else {
            loadApp();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(false);
        settings.setGeolocationEnabled(false);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                    isLoaded = true;
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("script.google.com") || url.contains("accounts.google.com")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                offlineLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!isConnected()) {
                    webView.setVisibility(View.GONE);
                    offlineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadApp() {
        if (!isConnected()) {
            webView.setVisibility(View.GONE);
            offlineLayout.setVisibility(View.VISIBLE);
            return;
        }
        offlineLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(GAS_URL);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }
}
