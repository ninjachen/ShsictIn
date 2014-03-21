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
import android.view.Window;

import com.wonders.shsict.R;

public class WelcomeActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		Thread welcomeThread = new Thread() {

			@Override
			public void run() {
				try {
					super.run();
					sleep(5000); //Delay of 5 seconds
				} catch (Exception e) {

				} finally {

					Intent i = new Intent(WelcomeActivity.this, HomePageActivity.class);
					startActivity(i);
					finish();
				}
			}
		};
		welcomeThread.start();

	}
}
