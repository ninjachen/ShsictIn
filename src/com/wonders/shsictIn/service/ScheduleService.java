package com.wonders.shsictIn.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wonders.shsictIn.BuildConfig;
import com.wonders.shsictIn.R;
import com.wonders.shsictIn.activity.WebViewActivity;
import com.wonders.shsictIn.utils.ConfigUtil;
import com.wonders.shsictIn.utils.HttpUtil;
import com.wonders.shsictIn.utils.ScheduleUtil;
import com.wonders.shsictIn.utils.WebAppInterface;

public class ScheduleService extends Service {

	public static final String ACTION = "com.wonders.shsictIn.service.ScheduleService";

	private Notification mNotification;
	private NotificationManager mManager;

	private PollingThread pollingThread;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		pollingThread = new PollingThread();
		pollingThread.start();
	}

	//初始化通知栏配置  
	private void initNotifiManager() {
		//		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//		int icon = R.drawable.ic_launcher;
		//		mNotification = new Notification();
		//		mNotification.icon = icon;
		//		mNotification.tickerText = "New Message";
		//		mNotification.defaults |= Notification.DEFAULT_SOUND;
		//		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
		//  你所关注的船舶有变更
				.setContentTitle(getResources().getString(R.string.notification_title))
				// 点击查看
				.setContentText(getResources().getString(R.string.notification_text));
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, WebViewActivity.class);
		resultIntent.putExtra(WebViewActivity.GOTO_Favourite, true);
		//reuse the old activity 
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(WebViewActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		//reuse the old activity 
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification = mBuilder.build();
	}

	//弹出Notification  
	private void showNotification() {
		mNotification.when = System.currentTimeMillis();
		ScheduleUtil.stopSchedule(ScheduleService.class, ScheduleService.ACTION);
		//		//Navigator to the new activity when click the notification title  
		//		Intent i = new Intent(this, MessageActivity.class);
		//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_SINGLE_TOP);//FLAG_ACTIVITY_NEW_TASK
		//		mNotification.setLatestEventInfo(this, getResources().getString(R.string.app_name), getResources().getString(R.string.notification_message), pendingIntent);
		mManager.notify(0, mNotification);
		WebViewActivity.isFavouriteUpdate = true;
		WebViewActivity.playRingTone(getApplicationContext());
	}

	/** 
	 * Polling thread 
	 * 模拟向Server轮询的异步线程 
	 * @Author Ryan 
	 * @Create 2013-7-13 上午10:18:34 
	 */
	int count = 0;

	class PollingThread extends Thread {
		@Override
		public void run() {
			//			System.out.println("Polling...");
			if(BuildConfig.DEBUG)
				Log.i("Ninja", "polling thread is running");
				
			count++;
			//				while (ScheduleUtil.isShowNotiFication) {
			//当用户关注有变更时，弹出通知  
			String uid = HttpUtil.getUIDFromCookie(ConfigUtil.cachedServerIp);
			if (uid != null) {
				int i = ScheduleUtil.queryFavouriteChangeNum(uid);
//				if(BuildConfig.DEBUG)
//					Log.i("Ninja", "polling thread get a result :" + i);
				
				if (i > 0) {
					showNotification();
				}
				//					ScheduleUtil.isShowNotiFication = true;
				//						System.out.println("New message!");
				//					}
			}
			//				sleep(1000 * interval);
			//				}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pollingThread != null) {

			try {
				pollingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		pollingThread = null;
		if (BuildConfig.DEBUG)
			Log.i("Ninja", "Service:onDestroy");
	}

}
