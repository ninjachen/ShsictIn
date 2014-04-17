package com.wonders.shsict.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;

import com.wonders.shsict.R;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final int TAB_NUMBER = 5;

	private static String shsictUrl = getShsictServiceURLString();

	private final static String[] urls = { shsictUrl, shsictUrl, shsictUrl,
			shsictUrl, shsictUrl };
	/*
	 * private String[] urls ={"http://www.google.com" ,"http://www.baidu.com"
	 * ,"http://www.qq.com" ,"http://www.36kr.com" ,"http://weibo.com"};
	 */

//	private WebView webview;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);

//		webview = (WebView) findViewById(R.id.webview);
		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			switch(i){
			case 0:
				actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_main_page).setTabListener(this));
				break;
			case 1:
				actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_online_query).setTabListener(this));
				break;
			case 2:
				actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_analysis).setTabListener(this));
				break;
			case 3:
				actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_my_follows).setTabListener(this));
				break;
			case 4:
				actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_more).setTabListener(this));
				break;
			}
//			actionBar.addTab(actionBar.newTab().setIcon(R.drawable.m1)
//					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
//					.setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			// case 0:
			// // The first section of the app is the most interesting -- it
			// offers
			// // a launchpad into the other demonstrations in this example
			// application.
			// return new LaunchpadSectionFragment();

			default:
				// The other sections of the app are dummy placeholders.
				Fragment fragment = new WebviewSectionFragment();
				Bundle args = new Bundle();
				args.putString(WebviewSectionFragment.WEBVIEW_URL, urls[i]);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return TAB_NUMBER;
		}

//		@Override
//		public CharSequence getPageTitle(int position) {
//			String title;
//			switch (position) {
//			case 0:
//				title = "首页";
//				break;
//			case 1:
//				title = "在线查询";
//				break;
//			case 2:
//				title = "统计分析";
//				break;
//			case 3:
//				title = "我的关注";
//				break;
//			case 4:
//				title = "更多";
//				break;
//			default:
//				title = "";
//			}
//			return title;
//		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack();// 返回前一个页面
			return true;
		}*/
		return super.onKeyDown(keyCode, event);
	}

	private static String getShsictServiceURLString() {
		String configPath = Environment.getExternalStorageDirectory().getPath()
				+ "/Shsict.config";
		String ip = getIP(configPath);
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(ip).append("/Portal.aspx");
		return sb.toString();
	}

	private static String getIP(String path) {
		try {
			BufferedReader br = null;

			br = new BufferedReader(new FileReader(path));
			String ip = br.readLine();
			br.close();
			return ip;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "baidu.com";

	}
}
