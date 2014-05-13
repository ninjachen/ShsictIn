package com.wonders.shsict.activity;

import com.wonders.shsict.R;
import com.wonders.shsict.service.ScheduleService;
import com.wonders.shsict.utils.ScheduleUtil;

import android.app.Activity;
import android.os.Bundle;

public class ScheduleClientActivity extends Activity {
		 private final static int INTERVAL = 5; 
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_message_client);  
	        //Start polling service  
	        System.out.println("Start polling service...");  
	        ScheduleUtil.startSchedule(this, INTERVAL, ScheduleService.class, ScheduleService.ACTION);  
	    }  
	      
	    @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //Stop polling service  
	        System.out.println("Stop polling service...");  
	        ScheduleUtil.stopSchedule( ScheduleService.class, ScheduleService.ACTION);  
	    }  
	  
}
