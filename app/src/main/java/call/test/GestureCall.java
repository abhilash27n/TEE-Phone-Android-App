package call.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class GestureCall extends Activity implements OnGesturePerformedListener, OnInitListener {
    private GestureLibrary mLibrary;
    EditText mEditor;
    TextToSpeech tts;
    boolean audio, flush; 
    //boolean double_click;
	SharedPreferences Pref;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturecall);
        mEditor = (EditText) findViewById(R.id.EditTextPhoneNum);
		tts = new TextToSpeech(this, this);
		String MEDIA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        //mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        mLibrary = GestureLibraries.fromFile(MEDIA_PATH+"gestures");
        if (!mLibrary.load()) {
        	finish();
        }

        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);
    }
	
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		Pref = PreferenceManager.getDefaultSharedPreferences(GestureCall.this); //to check preferences
    	audio = Pref.getBoolean("pref_audio", true);
    	flush = Pref.getBoolean("pref_flush", true);
        //double_click = Pref.getBoolean("pref_double_click", true);
		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				// Show the spell
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
				if(prediction.name.equalsIgnoreCase("Back Space"))
			    {
			    	
			    	if(mEditor.getText().toString().length()>0)
			    	{
			    		StringBuffer previousNumber = new StringBuffer(mEditor.getText().toString());
				    	say(previousNumber.charAt(previousNumber.length()-1)+" removed");
				    	mEditor.setText(previousNumber.deleteCharAt(previousNumber.length()-1));
			    	}
			    	else
			    	{
			    		say("Back Space. No Digit to be removed");
			    	}
			    }
			    else if(prediction.name.equalsIgnoreCase("Clear"))
				{
			    	mEditor.setText("");
			    	say("All characters removed");
				}
			    else if(prediction.name.equalsIgnoreCase("Call"))
			    {
			    	say("Calling "+mEditor.getText().toString());
			    	call(mEditor.getText().toString());
			    }
				else if(prediction.name.toCharArray()[0]>='0' && prediction.name.toCharArray()[0]<='9')
				{						
					StringBuffer previousNumber = new StringBuffer(mEditor.getText().toString());
					CharSequence phoneDigit = prediction.name;
					mEditor.setText(previousNumber.append(phoneDigit));
					say(phoneDigit+" entered");
					
				}
				else
				{
					Toast.makeText(this, "Not Recognized", Toast.LENGTH_SHORT).show();
					say("Not recognized, try again");
				}
				
				
			}
			else
			{
				Toast.makeText(this, "Not Recognized", Toast.LENGTH_SHORT).show();
				say("Not recognized, try again");
			}
		}
		else
		{
			Toast.makeText(this, "Not Recognized", Toast.LENGTH_SHORT).show();
			say("Not recognized, try again");
		}
		
	}
	private void call(String phoneNumber) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + phoneNumber));
			startActivity(callIntent);
		} catch (ActivityNotFoundException activityException) {
			Log.e("dialing-example", "Call failed", activityException);
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
	
	public void onInit(int status) {
		
		//say("Hello World");
		
	}
	
	@Override
	public void onDestroy() {
		say("Showing gesture options");
		/*if (tts != null) {
			tts.stop();
			tts.shutdown();
		}*/

		super.onDestroy();
	}
}