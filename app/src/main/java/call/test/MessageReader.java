package call.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MessageReader extends Activity implements OnInitListener {

  EditText mEditor;
  TextToSpeech tts;
  Button button_repeat_msg;
  Button button_next_msg;
  int flag_prev=0,flag_curr=0;
  
  boolean audio, double_click, flush;
  SharedPreferences Pref;
  
  String name="";   //get name of contact
  
  Cursor cur;    //to point to messages.  
  String sms = "";
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      setContentView(R.layout.msgreader);
      tts = new TextToSpeech(this, this);
      mEditor = (EditText) findViewById(R.id.message_Field);
      
      
      addListenerOnButton();
      
      Pref = PreferenceManager.getDefaultSharedPreferences(MessageReader.this); //to check preferences
      audio = Pref.getBoolean("pref_audio", true);
      double_click = Pref.getBoolean("pref_double_click", true);
      flush = Pref.getBoolean("pref_flush", true);
      
      
      Uri uriSMSURI = Uri.parse("content://sms/inbox");
      cur = getContentResolver().query(uriSMSURI, null, null, null,null);
      
      
    	  
      if(cur.moveToNext())
      {
    	  name = getContactName(cur.getString(2));
    	  sms += "From :" + name + " : " + cur.getString(12)+"\n";
    	  mEditor.setText(sms);
    	  sayMessage(sms);
      }
      else
      {
    	  say("No messages to be read");
               
      }
  
      mEditor.setOnTouchListener(new OnTouchListener() {    //to disable open of keypad on touch of text box
    	  		
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			return true;
		}
	}); 
  }
  public void addListenerOnButton(){
  
	  if(double_click && audio)
	  {
		  button_repeat_msg = (Button) findViewById(R.id.repeat);
			button_repeat_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					flag_curr=1;
					if(flag_prev != flag_curr)
					{
						say(button_repeat_msg.getText().toString());
						flag_prev = flag_curr;
					}
					else
					{
						String message = mEditor.getText().toString();
		    			sayMessage(message);
						
					}
				}
			});
			
			button_next_msg= (Button) findViewById(R.id.next_message);
			button_next_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					flag_curr=2;
					if(flag_prev != flag_curr)
					{
						say(button_next_msg.getText().toString());
						flag_prev = flag_curr;
					}
					else
					{
						 if(cur.moveToNext())
					      {
							  name = getContactName(cur.getString(2));
					    	  sms = "From :" + name + " : " + cur.getString(12)+"\n";
					    	  mEditor.setText(sms);
					    	  sayMessage(sms);
					      }
					      else
					      {
					    	  say("No messages to be read");
					               
					      }   
						
						
					}
				}
			});
	  }
	  else
	  {
		  button_repeat_msg = (Button) findViewById(R.id.repeat);
			button_repeat_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
						String message = mEditor.getText().toString();
		    			sayMessage(message);
						
					
				}
			});
			
			button_next_msg= (Button) findViewById(R.id.next_message);
			button_next_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
						 if(cur.moveToNext())
					      {
							  name = getContactName(cur.getString(2));
					    	  sms = "From :" + name + " : " + cur.getString(12)+"\n";
					    	  mEditor.setText(sms);
					    	  sayMessage(sms);
					      }
					      else
					      {
					    	  say("No messages to be read");
					               
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
  
  public void sayMessage(String text2say){
	  	if(flush)
		{
			tts.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
		}
		else
		{
			tts.speak(text2say, TextToSpeech.QUEUE_ADD, null);
		}
	  }

	@Override
	public void onInit(int status) {
		
		//say("Hello World");
		
	}
	
	@Override
	public void onDestroy() {
		say("Showing extra options");
		/*if (tts != null) {
			tts.stop();
			tts.shutdown();
		}*/

		super.onDestroy();
	}
	public String getContactName(final String phoneNumber) 
    {  
        Uri uri;
        String[] projection;

        if (Build.VERSION.SDK_INT >= 5)
        {
            uri = Uri.parse("content://com.android.contacts/phone_lookup");
            projection = new String[] { "display_name" };
        }
        else
        { 
            uri = Uri.parse("content://contacts/phones/filter");
            projection = new String[] { "name" }; 
        } 

        uri = Uri.withAppendedPath(uri, Uri.encode(phoneNumber)); 
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null); 

        String contactName = phoneNumber;

        if (cursor.moveToFirst()) 
        { 
            contactName = cursor.getString(0);
        } 

        cursor.close();
        cursor = null;

        return contactName; 
    }


}
