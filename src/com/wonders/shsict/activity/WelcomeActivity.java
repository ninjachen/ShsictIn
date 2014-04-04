/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wonders.shsict.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.wonders.shsict.R;
import com.wonders.shsict.utils.ConfigUtil;

public class WelcomeActivity extends Activity implements OnClickListener {
	//	private AlertDialog alertDialog = null;
	//	private Thread loopThread;
	public static final int START_WITH_OUT_GO_TO_MAINPAGE = 777;
	public static boolean first_create = true;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		ImageView iv = (ImageView)findViewById(R.id.welcomeView);
		iv.setOnClickListener(this);
		
		Thread delayToMainPageThread = new Thread() {

			@Override
			public void run() {
				try {
					super.run();
					sleep(1000); //Delay of 5 seconds
					goToMainPage(WelcomeActivity.this);
				} catch (Exception e) {
					//sleep error
					e.printStackTrace();
				}
			}
		};
		//判斷是否是第一次進入程序
		String url = ConfigUtil.getShsictServiceURLString(WelcomeActivity.this);
		if (url != null) {
			if(first_create){
				first_create = false;
				delayToMainPageThread.start();
			}
		} else {
			ConfigUtil.storeUrl(getApplicationContext(), getString(R.string.default_server_ip));
			delayToMainPageThread.start();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		//		if (alertDialog == null) {
		//			alertDialog = ConfigUtil.showDialog(WelcomeActivity.this);
		//		}

		/*if (loopThread == null) {
			loopThread = new Thread() {

				@Override
				public void run() {
					super.run();
					try {
						while (true) {
							sleep(2000);
							WelcomeActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									String url = ConfigUtil.getShsictServiceURLString(WelcomeActivity.this);
									if (url != null) {
										Intent i = new Intent(WelcomeActivity.this, HomePageActivity.class);
										startActivity(i);
										WelcomeActivity.this.finish();
									} else if (alertDialog == null) {
										alertDialog = ConfigUtil.showDialog(WelcomeActivity.this);
									} else if (!alertDialog.isShowing()) {
										alertDialog.show();
									}

								}
							});
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			};
		}
		loopThread.start();*/

	}

	@Override
	protected void onPause() {
		super.onPause();
		/*if (loopThread.isAlive()) {
			loopThread.interrupt();
			loopThread = null;
		}*/
	}

	@Override
	public void onClick(View v) {
		goToMainPage(WelcomeActivity.this);
	}

	public void goToMainPage(Activity activity) {
		Intent i = new Intent(activity, HomePageActivity.class);
		activity.startActivity(i);
		activity.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == START_WITH_OUT_GO_TO_MAINPAGE)
				first_create = false;
		}
	}
}
