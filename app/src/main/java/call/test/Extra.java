package call.test;


import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Extra extends Activity implements OnInitListener{
    /** Called when the activity is first created. */
	Button button_date;
	Button button_camera;
	Button button_musicplayer;
	Button button_msgReader;
	int flag_prev=0,flag_curr=0;
	TextToSpeech tts;
	boolean audio, double_click, flush;
	SharedPreferences Pref;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra);
        tts = new TextToSpeech(this, this);
        addListenerOnButton();
    }
    public void addListenerOnButton(){
    	final Context context = this;
    	
    	Pref = PreferenceManager.getDefaultSharedPreferences(Extra.this); //to check preferences
    	audio = Pref.getBoolean("pref_audio", true);
        double_click = Pref.getBoolean("pref_double_click", true);
        flush = Pref.getBoolean("pref_flush", true);
        
        if(double_click && audio){
        	button_date = (Button) findViewById(R.id.Date_Time);
        	button_date.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=1;
        			if(flag_prev != flag_curr)
        			{
        				say(button_date.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            			say(currentDateTimeString);
        				
        			}
        		}
        	});
        	
        	button_msgReader= (Button) findViewById(R.id.Msg_Read);
        	button_msgReader.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=2;
        			if(flag_prev != flag_curr)
        			{
        				say(button_msgReader.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Message Reader Opened");
        				Intent intent = new Intent (context, MessageReader.class);
        				startActivity(intent);   				
        				
        			}
        		}
        	});
        	button_musicplayer= (Button) findViewById(R.id.Music);
        	button_musicplayer.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=3;
        			if(flag_prev != flag_curr)
        			{
        				say(button_musicplayer.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Music Player opened");
        				Intent intent = new Intent (context, MusicPlayer.class);
        				startActivity(intent);    				
        			}
        		}
        	});
        	button_camera= (Button) findViewById(R.id.Camera); 
        	button_camera.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=4;
        			if(flag_prev != flag_curr)
        			{
        				say(button_camera.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				if(checkCameraHardware(context))
            			{
            				say("Camera open");
            				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
               			    startActivityForResult(intent, 0); 
            			}
            			else
            			{
            				say("Device has no camera");
            			}
        			}
        		}
        	});
        }
        else
        {
        	button_date = (Button) findViewById(R.id.Date_Time);
        	button_date.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            			say(currentDateTimeString);
        				
        			
        		}
        	});
        	
        	button_msgReader= (Button) findViewById(R.id.Msg_Read);
        	button_msgReader.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Message Reader Opened");
        				Intent intent = new Intent (context, MessageReader.class);
        				startActivity(intent);   				
        				
        			
        		}
        	});
        	button_musicplayer= (Button) findViewById(R.id.Music);
        	button_musicplayer.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Music Player opened");
        				Intent intent = new Intent (context, MusicPlayer.class);
        				startActivity(intent);    				
        			
        		}
        	});
        	button_camera= (Button) findViewById(R.id.Camera); 
        	button_camera.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				if(checkCameraHardware(context))
            			{
            				say("Camera open");
            				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
               			    startActivityForResult(intent, 0); 
            			}
            			else
            			{
            				say("Device has no camera");
            			}
        			
        		}
        	});
        }
    }
    public void say(String text2say){
    	if(audio)
    	{
    		if(flush)
    		{
    			tts.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
    		}
    		else
    		{
    			tts.speak(text2say, TextToSpeech.QUEUE_ADD, null);
    		}
    	}
    	
    }

	@Override
	public void onInit(int status) {
		
		//say("Hello World");
		
	}
	
	@Override
	public void onDestroy() {
		say("Showing main menu");
		/*if (tts != null) {
			tts.stop();
			tts.shutdown();
		}*/

		super.onDestroy();
	}
	
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
}