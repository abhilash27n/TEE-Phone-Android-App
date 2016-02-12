package call.test;


import java.util.ArrayList;
import java.util.List;





import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*class Mp3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}*/

public class MusicPlayer extends Activity implements OnInitListener{
    /** Called when the activity is first created. */
	Button button_play;
	Button button_pause;
	Button button_next;
	Button button_prev;
	int flag_prev=0,flag_curr=0;
	TextToSpeech tts;
	
	
    Cursor musiccursor;
    int music_column_index;
    int count;
    int position = 0;
    List<String> songs;
	
	boolean audio, double_click, flush;
	SharedPreferences Pref;
	
	
	//private static String MEDIA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	//private List<String> songs = new ArrayList<String>();
	private MediaPlayer mp = new MediaPlayer();
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayer);
        tts = new TextToSpeech(this, this);
        updateSongList();
        addListenerOnButton();
       
    }
    public void addListenerOnButton(){
    	
    	Pref = PreferenceManager.getDefaultSharedPreferences(MusicPlayer.this); //to check preferences
    	audio = Pref.getBoolean("pref_audio", true);
        double_click = Pref.getBoolean("pref_double_click", true);
        flush = Pref.getBoolean("pref_flush", true);
        
    	if(double_click && audio){
    		
    		button_play = (Button) findViewById(R.id.play);
        	button_play.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=1;
        			if(flag_prev != flag_curr)
        			{
        				say(button_play.getText().toString());
        				flag_prev = flag_curr;
        				//Toast.makeText(getApplicationContext(), MEDIA_PATH ,
                		//		Toast.LENGTH_LONG).show();
        			}
        			else
        			{
        				say("Playing Music");
        				playSong(position);
        				
        			}
        		}
        	});
        	button_pause= (Button) findViewById(R.id.pause);
        	button_pause.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=2;
        			if(flag_prev != flag_curr)
        			{
        				say(button_pause.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				if(mp.isPlaying())
        				{
        					say("Music Stopped");
        					mp.stop();
        				}
        				else
        				{
        					say("Music not playing");
        				}
        			}
        		}
        	});
        	button_next= (Button) findViewById(R.id.next);
        	button_next.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=3;
        			if(flag_prev != flag_curr)
        			{
        				say(button_next.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				if(mp.isPlaying())
        				{
        					say("Next track");
            				nextSong();
        				}
        				else
        				{
        					say("Music not playing");
        				}
        				
        				
        			}
        		}
        	});
        	button_prev= (Button) findViewById(R.id.previous);
        	button_prev.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			flag_curr=4;
        			if(flag_prev != flag_curr)
        			{
        				say(button_prev.getText().toString());
        				flag_prev = flag_curr;
        			}
        			else
        			{
        				if(mp.isPlaying())
        				{
        					say("Previous track");
            				prevSong();
        				}
        				else
        				{
        					say("Music not playing");
        				}
        				
        				
        			}
        		}
        	});
    	}
    	else
    	{
    		button_play = (Button) findViewById(R.id.play);
        	button_play.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        				say("Playing Music");
        				playSong(position);
        				
        			
        		}
        	});
        	button_pause= (Button) findViewById(R.id.pause);
        	button_pause.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        			if(mp.isPlaying())
    				{
    					say("Music Stopped");
    					mp.stop();
    				}
    				else
    				{
    					say("Music not playing");
    				}
        			
        		}
        	});
        	button_next= (Button) findViewById(R.id.next);
        	button_next.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        			if(mp.isPlaying())
    				{
    					say("Next track");
        				nextSong();
    				}
    				else
    				{
    					say("Music not playing");
    				}
        				
        			
        		}
        	});
        	button_prev= (Button) findViewById(R.id.previous);
        	button_prev.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			
        			if(mp.isPlaying())
    				{
    					say("Previous track");
        				prevSong();
    				}
    				else
    				{
    					say("Music not playing");
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
		if(mp.isPlaying())
		{
			mp.stop();
			say("Music Stopped");
			
		}
		say("Showing extra options");
		/*if (tts != null) {
			tts.stop();
			tts.shutdown();
		}*/
		

		super.onDestroy();
	}
	private void nextSong() {
        if (++position >= songs.size()) {
                // Last song, just reset currentPosition
                position = 0;
        } 
                // Play next song
                playSong(position);
        
    }
	private void prevSong() {
        if (--position < 0) {
                // First song, go to last song
                position = songs.size()-1;
        } 
                // Play previous song
                playSong(position);
        
    }
	public void updateSongList() {
        /*File home = new File(MEDIA_PATH);
        if (home.listFiles(new Mp3Filter()).length > 0) {
                for (File file : home.listFiles(new Mp3Filter())) {
                        songs.add(file.getName());
                }
        }*/
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String[] projection = {
		        MediaStore.Audio.Media._ID,
		        MediaStore.Audio.Media.ARTIST,
		        MediaStore.Audio.Media.TITLE,
		        MediaStore.Audio.Media.DATA,
		        MediaStore.Audio.Media.DISPLAY_NAME,
		        MediaStore.Audio.Media.DURATION
		};

		musiccursor = this.managedQuery(
		        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		        projection,
		        selection,
		        null,
		        null);

		songs = new ArrayList<String>();
		while(musiccursor.moveToNext()){
		        //songs.add(musiccursor.getString(0) + "||" + musiccursor.getString(1) + "||" +   musiccursor.getString(2) + "||" +   musiccursor.getString(3) + "||" +  musiccursor.getString(4) + "||" +  musiccursor.getString(5));
			songs.add(musiccursor.getString(0));
		}
	}
	private void playSong(int position) {
        /*try {
 
                mp.reset();
                mp.setDataSource(songPath);
                mp.prepare();
                mp.start();
 
                // Setup listener so next song starts automatically
                mp.setOnCompletionListener(new OnCompletionListener() {
 
                        public void onCompletion(MediaPlayer arg0) {
                                nextSong();
                        }
 
                });
 
        } catch (IOException e) {
                Log.v(getString(R.string.app_name), e.getMessage());
        }
    }*/
		if(songs.size()>0)
		{
			music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			musiccursor.moveToPosition(position);
			String filename = musiccursor.getString(music_column_index);
			
			try {
	            if (mp.isPlaying()) {
	                  mp.reset();
	            }
	            mp.reset();
	            mp.setDataSource(filename);
	            mp.prepare();
	            mp.start();
	            // Setup listener so next song starts automatically
	            mp.setOnCompletionListener(new OnCompletionListener() {

	                    public void onCompletion(MediaPlayer arg0) {
	                            nextSong();
	                    }

	            });
			} catch (Exception e) {

	     	 }
			
		}
		else
		{
			say("No songs on phone");
		}
	}
	/*@Override
	public void onStop()
	{
		if(mp.isPlaying())
		{
			mp.stop();
			say("Music Stopped");
			
		}
	}*/
	 
}