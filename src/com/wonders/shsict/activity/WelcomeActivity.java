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
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.wonders.shsict.R;
import com.wonders.shsict.utils.ConfigUtil;

public class WelcomeActivity extends Activity {
	private AlertDialog alertDialog = null;
	private Thread loopThread;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Thread delayToMainPageThread = new Thread() {

			@Override
			public void run() {
				try {
					super.run();
					sleep(1000); //Delay of 5 seconds
					Intent i = new Intent(WelcomeActivity.this, HomePageActivity.class);
					startActivity(i);
					finish();
				} catch (Exception e) {
					//sleep error
					e.printStackTrace();
				}
			}
		};
		//判斷是否是第一次進入程序
		String url = ConfigUtil.getShsictServiceURLString(WelcomeActivity.this);
		if (url != null) {
			delayToMainPageThread.start();
		} else if (alertDialog == null) {
			alertDialog = ConfigUtil.showDialog(WelcomeActivity.this);
		}

		if (loopThread == null) {
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
		loopThread.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (loopThread.isAlive()) {
			loopThread.interrupt();
			loopThread = null;
		}
	}

}
