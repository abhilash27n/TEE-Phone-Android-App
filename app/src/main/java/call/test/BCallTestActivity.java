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

public class BCallTestActivity extends Activity implements OnInitListener{
    /** Called when the activity is first created. */
	Button button_dial;
	Button button_contact;
	Button button_gesture;
	Button button_extra;
	Button button_preference;
	boolean audio, double_click, flush;
	SharedPreferences Pref;
	int flag_prev=0,flag_curr=0;
	TextToSpeech tts;
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tts = new TextToSpeech(this, this);
        addListenerOnButton();
    }
    public void addListenerOnButton(){
    	final Context context = this;
    	
    	Pref = PreferenceManager.getDefaultSharedPreferences(BCallTestActivity.this); //to check preferences
    	audio = Pref.getBoolean("pref_audio", true);
        double_click = Pref.getBoolean("pref_double_click", true);
        flush = Pref.getBoolean("pref_flush", true);
        
        if(double_click && audio){
        	
        	button_dial = (Button) findViewById(R.id.DialPadButton);
        	button_dial.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=1;
        			if(flag_prev != flag_curr)
        			{
        				say(button_dial.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Showing Dial pad");
        				Intent intent = new Intent (context, DialerPad.class);
        				startActivity(intent);
        			}
        		}
        	});
        	button_contact= (Button) findViewById(R.id.ContactListButton);
        	button_contact.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=2;
        			if(flag_prev != flag_curr)
        			{
        				say(button_contact.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Showing Contact List");
        				Intent intent = new Intent (context, ContactList.class);
        				startActivity(intent);
        			}
        		}
        	});
        	button_gesture= (Button) findViewById(R.id.GestureRecogButton);
        	button_gesture.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=3;
        			if(flag_prev != flag_curr)
        			{
        				say(button_gesture.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Showing Gesture Recognition options");
        				Intent intent = new Intent (context, GestureRecognition.class);
        				startActivity(intent);
        			}
        		}
        	});
        	button_extra= (Button) findViewById(R.id.ExtraButton);
        	button_extra.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=4;
        			if(flag_prev != flag_curr)
        			{
        				say(button_extra.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Showing Extra options");
        				Intent intent = new Intent (context, Extra.class);
        				startActivity(intent);
        			}
        		}
        	});
        	button_preference = (Button) findViewById(R.id.PreferenceButton);
        	button_preference.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=5;
        			if(flag_prev != flag_curr)
        			{
        				say(button_preference.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				say("Showing Preferences");
        				Intent intent = new Intent (context, PreferenceSet.class);
        				startActivity(intent);
        			}
        		}
        	});
        }
        else{
        	button_dial = (Button) findViewById(R.id.DialPadButton);
        	button_dial.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Showing Dial pad");
        				Intent intent = new Intent (context, DialerPad.class);
        				startActivity(intent);
        			
        		}
        	});
        	button_contact= (Button) findViewById(R.id.ContactListButton);
        	button_contact.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        			
        				say("Showing Contact List");
        				Intent intent = new Intent (context, ContactList.class);
        				startActivity(intent);
        			
        		}
        	});
        	button_gesture= (Button) findViewById(R.id.GestureRecogButton);
        	button_gesture.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Showing Gesture Recognition options");
        				Intent intent = new Intent (context, GestureRecognition.class);
        				startActivity(intent);
        			
        		}
        	});
        	button_extra= (Button) findViewById(R.id.ExtraButton);
        	button_extra.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        			
        				say("Showing Extra options");
        				Intent intent = new Intent (context, Extra.class);
        				startActivity(intent);
        			
        		}
        	});
        	button_preference = (Button) findViewById(R.id.PreferenceButton);
        	button_preference.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Showing Preferences");
        				Intent intent = new Intent (context, PreferenceSet.class);
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
    		while (tts.isSpeaking())
    		{}
    	}
    	
    }

	@Override
	public void onInit(int status) {
		
		//say("Hello World");
		
	}
	
	@Override
	public void onDestroy() {
		say("Application Closed");
		/*if (tts != null) {
			tts.stop();
			tts.shutdown();
		}*/
		
		super.onDestroy();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
        setContentView(R.layout.main);
        tts = new TextToSpeech(this, this);
        addListenerOnButton();
	}
	
}