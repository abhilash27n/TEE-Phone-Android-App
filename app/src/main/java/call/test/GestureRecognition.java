package call.test;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GestureRecognition extends Activity implements OnInitListener{
    /** Called when the activity is first created. */
	Button button_gesture_input;
	Button button_gesture_setting;
	boolean audio, double_click, flush;
	SharedPreferences Pref;
	int flag_prev=0,flag_curr=0;
	TextToSpeech tts;
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturerecognition);
        tts = new TextToSpeech(this, this);
        addListenerOnButton();
    }
    public void addListenerOnButton(){
    	final Context context = this;
    	
    	Pref = PreferenceManager.getDefaultSharedPreferences(GestureRecognition.this); //to check preferences
    	audio = Pref.getBoolean("pref_audio", true);
        double_click = Pref.getBoolean("pref_double_click", true);
        flush = Pref.getBoolean("pref_flush", true);
        
        if(double_click && audio){
        	
        	button_gesture_input = (Button) findViewById(R.id.gesture_recog_input);
        	button_gesture_input.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=1;
        			if(flag_prev != flag_curr)
        			{
        				say(button_gesture_input.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Draw numbers for Input");
        				Intent intent = new Intent (context, GestureCall.class);
        				startActivity(intent);
        			}
        		}
        	});
        	button_gesture_setting= (Button) findViewById(R.id.gesture_recog_settings);
        	button_gesture_setting.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=2;
        			if(flag_prev != flag_curr)
        			{
        				say(button_gesture_setting.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Showing Gesture Settings");
        				Intent intent = new Intent (context, GestureBuilderActivity.class);
        				startActivity(intent);
        			}
        		}
        	});
        	        	
        }
        else{
        	button_gesture_input = (Button) findViewById(R.id.gesture_recog_input);
        	button_gesture_input.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Draw numbers for Input");
        				Intent intent = new Intent (context, GestureCall.class);
        				startActivity(intent);
        			
        		}
        	});
        	button_gesture_setting= (Button) findViewById(R.id.gesture_recog_settings);
        	button_gesture_setting.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Showing Gesture Settings");
        				Intent intent = new Intent (context, GestureBuilderActivity.class);
        				startActivity(intent);
        			
        		}
        	});
        }
        	    	
    }
    public void say(String text2say){
    	if(audio)     //work only if audio is on
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
	
	
	
}