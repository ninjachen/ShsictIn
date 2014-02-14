package com.wonders.shsict.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebviewSectionFragment extends Fragment {

	public static final String WEBVIEW_URL = "url";
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_webview, container, false);
            Bundle args = getArguments();
           ((WebView) rootView.findViewById(R.id.webview)).loadUrl(args.getString(WEBVIEW_URL));
            return rootView;
        }
}
