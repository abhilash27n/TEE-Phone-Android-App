/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*package call.test;

import android.app.ExpandableListActivity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;
import android.widget.Toast;

*/
/**
 * Demonstrates expandable lists backed by Cursors
 */
/*public class ContactList extends ExpandableListActivity implements OnInitListener {
    private int mGroupIdColumnIndex; 
    TextToSpeech tts;
    int flagcallcontact=0;
    String contactclicked;
    private String mPhoneNumberProjection[] = new String[] {
            People.Phones._ID, People.Phones.NUMBER
    };

    
    private ExpandableListAdapter mAdapter;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(this, this);
        // Query for people
        Cursor groupCursor = managedQuery(People.CONTENT_URI,
                new String[] {People._ID, People.NAME}, null, null, null);

        // Cache the ID column index
        mGroupIdColumnIndex = groupCursor.getColumnIndexOrThrow(People._ID);

        // Set up our adapter
        mAdapter = new MyExpandableListAdapter(groupCursor,
                this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.layout.simple_expandable_list_item_1,
                new String[] {People.NAME}, // Name for group layouts
                new int[] {android.R.id.text1},
                new String[] {People.NUMBER}, // Number for child layouts
                new int[] {android.R.id.text1});
        setListAdapter(mAdapter);
        final ExpandableListView lv = getExpandableListView();
        
        
        lv.setOnChildClickListener(new OnChildClickListener() {
        	@Override
        	public boolean onChildClick(ExpandableListView parent, View view,
				int groupPosition, int childPosition, long id) {
        		
        		String contact = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1);
        		String number = " "+((TextView) view).getText();
        		String text = contact + number;
        		// When clicked, show a toast with the TextView text
        		Toast.makeText(getApplicationContext(), text ,
        				Toast.LENGTH_SHORT).show();
        		if(flagcallcontact==0 || contactclicked.equals(text)==false)
        		{
        			flagcallcontact=1;
        			contactclicked=text;
        			say("Press again to call "+contact+" on number "+number);
        		}
        		else if(flagcallcontact == 1 && contactclicked.equals(text))
        		{
        			flagcallcontact = 0;
        			say("Calling "+contact);
        			call(number);
        		}
        		
        		return false;
        	}

			
        });
        lv.setOnGroupExpandListener(new OnGroupExpandListener(){

			@Override
			public void onGroupExpand(int groupPosition) {
				String text = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1)+" expanded";
        		// When clicked, show a toast with the TextView text
        		Toast.makeText(getApplicationContext(), text ,
        				Toast.LENGTH_SHORT).show();
        		say(text);
            					
			}
        	
        }
        );
        lv.setOnGroupCollapseListener(new OnGroupCollapseListener(){

			@Override
			public void onGroupCollapse(int groupPosition) {
				String text = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1)+" collapsed";
        		// When clicked, show a toast with the TextView text
        		Toast.makeText(getApplicationContext(), text ,
        				Toast.LENGTH_SHORT).show();
        		say(text);
            					
			}
        	
        }
        );
        /*lv.setOnGroupClickListener(new OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, 
                View view, int groupPosition, long id)
            {
            	String text = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1);
        		// When clicked, show a toast with the TextView text
        		Toast.makeText(getApplicationContext(), text ,
        				Toast.LENGTH_SHORT).show();
        		say(text);
            	return false;
            }
        });*/
  /*      lv.setOnScrollListener(new OnScrollListener(){
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            	/*String text = ((Cursor)mAdapter.getGroup(firstVisibleItem)).getString(1)+" on top of list";
        		// When clicked, show a toast with the TextView text
        		Toast.makeText(getApplicationContext(), text ,
        				Toast.LENGTH_SHORT).show();
        		say(text);*/
 /*           }
           public void onScrollStateChanged(AbsListView view, int scrollState) {
              // TODO Auto-generated method stub
              if(scrollState == 0) {
            	  int index = lv.getFirstVisiblePosition();
            	  String text = ((Cursor)mAdapter.getGroup(index)).getString(1)+" on top of list";
          		// When clicked, show a toast with the TextView text
          		Toast.makeText(getApplicationContext(), text ,
          				Toast.LENGTH_SHORT).show();
          		say(text);
              }
            }
          });
    }

    public class MyExpandableListAdapter extends SimpleCursorTreeAdapter {

        public MyExpandableListAdapter(Cursor cursor, Context context, int groupLayout,
                int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                int[] childrenTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childrenFrom,
                    childrenTo);
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            // Given the group, we return a cursor for all the children within that group 

            // Return a cursor that points to this contact's phone numbers
            Uri.Builder builder = People.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, groupCursor.getLong(mGroupIdColumnIndex));
            builder.appendEncodedPath(People.Phones.CONTENT_DIRECTORY);
            Uri phoneNumbersUri = builder.build();

            // The returned Cursor MUST be managed by us, so we use Activity's helper
            // functionality to manage it for us.
            return managedQuery(phoneNumbersUri, mPhoneNumberProjection, null, null, null);
        }

    }
    public void say(String text2say){
    	tts.speak(text2say, TextToSpeech.QUEUE_ADD, null);
    }

	@Override
	public void onInit(int status) {
		
		//say("Hello World");
		
	}
	
	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}

		super.onDestroy();
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

	
}
*/
            
            /*
             * Copyright (C) 2007 The Android Open Source Project
             *
             * Licensed under the Apache License, Version 2.0 (the "License");
             * you may not use this file except in compliance with the License.
             * You may obtain a copy of the License at
             *
             *      http://www.apache.org/licenses/LICENSE-2.0
             *
             * Unless required by applicable law or agreed to in writing, software
             * distributed under the License is distributed on an "AS IS" BASIS,
             * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
             * See the License for the specific language governing permissions and
             * limitations under the License.
             */

            package call.test;

            import android.app.ExpandableListActivity;
            import android.content.ActivityNotFoundException;
            import android.content.AsyncQueryHandler;
            import android.content.ContentUris;
            import android.content.Context;
            import android.content.Intent;
import android.content.SharedPreferences;
            import android.database.Cursor;
            import android.net.Uri;
            import android.os.Bundle;
import android.preference.PreferenceManager;
            import android.provider.ContactsContract.CommonDataKinds.Phone;
            import android.provider.ContactsContract.Contacts;
            import android.speech.tts.TextToSpeech;
            import android.speech.tts.TextToSpeech.OnInitListener;
            import android.util.Log;
            import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
            import android.widget.CursorTreeAdapter;
            import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
            import android.widget.SimpleCursorTreeAdapter;
            import android.widget.TextView;
            import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

            /**
             * Demonstrates expandable lists backed by Cursors
             */
            public class ContactList extends ExpandableListActivity implements OnInitListener {

            	int flagcallcontact=0;
            	boolean audio, double_click, flush;
            	SharedPreferences Pref;
            	String contactclicked;
            	TextToSpeech tts;
                private static final String[] CONTACTS_PROJECTION = new String[] {
                    Contacts._ID,
                    Contacts.DISPLAY_NAME
                };
                private static final int GROUP_ID_COLUMN_INDEX = 0;

                private static final String[] PHONE_NUMBER_PROJECTION = new String[] {
                        Phone._ID,
                        Phone.NUMBER
                };

                private static final int TOKEN_GROUP = 0;
                private static final int TOKEN_CHILD = 1;

                private static final class QueryHandler extends AsyncQueryHandler {
                    private CursorTreeAdapter mAdapter;

                    public QueryHandler(Context context, CursorTreeAdapter adapter) {
                        super(context.getContentResolver());
                        this.mAdapter = adapter;
                    }

                    @Override
                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                        switch (token) {
                        case TOKEN_GROUP:
                            mAdapter.setGroupCursor(cursor);
                            break;

                        case TOKEN_CHILD:
                            int groupPosition = (Integer) cookie;
                            mAdapter.setChildrenCursor(groupPosition, cursor);
                            break;
                        }
                    }
                }

                public class MyExpandableListAdapter extends SimpleCursorTreeAdapter {

                    // Note that the constructor does not take a Cursor. This is done to avoid querying the 
                    // database on the main thread.
                    public MyExpandableListAdapter(Context context, int groupLayout,
                            int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                            int[] childrenTo) {

                        super(context, null, groupLayout, groupFrom, groupTo, childLayout, childrenFrom,
                                childrenTo);
                    }

                    @Override
                    protected Cursor getChildrenCursor(Cursor groupCursor) {
                        // Given the group, we return a cursor for all the children within that group 

                        // Return a cursor that points to this contact's phone numbers
                        Uri.Builder builder = Contacts.CONTENT_URI.buildUpon();
                        ContentUris.appendId(builder, groupCursor.getLong(GROUP_ID_COLUMN_INDEX));
                        builder.appendEncodedPath(Contacts.Data.CONTENT_DIRECTORY);
                        Uri phoneNumbersUri = builder.build();

                        mQueryHandler.startQuery(TOKEN_CHILD, groupCursor.getPosition(), phoneNumbersUri, 
                                PHONE_NUMBER_PROJECTION, Phone.MIMETYPE + "=?", 
                                new String[] { Phone.CONTENT_ITEM_TYPE }, null);

                        return null;
                    }
                }

                private QueryHandler mQueryHandler;
                private CursorTreeAdapter mAdapter;

                @Override
                public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    tts = new TextToSpeech(this, this);
                    // Set up our adapter
                    mAdapter = new MyExpandableListAdapter(
                            this,
                            android.R.layout.simple_expandable_list_item_1,
                            android.R.layout.simple_expandable_list_item_1,
                            new String[] { Contacts.DISPLAY_NAME }, // Name for group layouts
                            new int[] { android.R.id.text1 },
                            new String[] { Phone.NUMBER }, // Number for child layouts
                            new int[] { android.R.id.text1 });

                    setListAdapter(mAdapter);
                    final ExpandableListView lv = getExpandableListView();
                    Pref = PreferenceManager.getDefaultSharedPreferences(ContactList.this); //to check preferences
                	audio = Pref.getBoolean("pref_audio", true);
                    double_click = Pref.getBoolean("pref_double_click", true);
                    flush = Pref.getBoolean("pref_flush", true);
                    lv.setOnChildClickListener(new OnChildClickListener() {
                    	@Override
                    	public boolean onChildClick(ExpandableListView parent, View view,
            				int groupPosition, int childPosition, long id) {
                    		
                    		String contact = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1);
                    		String number = " "+((TextView) view).getText();
                    		String text = contact + number;
                    		// When clicked, show a toast with the TextView text
                    		Toast.makeText(getApplicationContext(), text ,
                    				Toast.LENGTH_SHORT).show();
                    		if(double_click && audio)
                    		{
                    			if(flagcallcontact==0 || contactclicked.equals(text)==false)
                    			{
                    				flagcallcontact=1;
                    				contactclicked=text;
                    				say("Press again to call "+contact+" on number "+number);
                    			}
                    			else if(flagcallcontact == 1 && contactclicked.equals(text))
                    			{
                    				flagcallcontact = 0;
                    				say("Calling "+contact);
                    				call(number);
                    			}
                    		}
                    		else
                    		{
                    			say("Calling "+contact);
                    			call(number);
                    		}
                    		return false;
                    	}

            			
                    });
                    lv.setOnGroupExpandListener(new OnGroupExpandListener(){

            			@Override
            			public void onGroupExpand(int groupPosition) {
            				String text = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1)+" expanded";
                    		// When clicked, show a toast with the TextView text
                    		Toast.makeText(getApplicationContext(), text ,
                    				Toast.LENGTH_SHORT).show();
                    		say(text);
                        					
            			}
                    	
                    }
                    );
                    lv.setOnGroupCollapseListener(new OnGroupCollapseListener(){

            			@Override
            			public void onGroupCollapse(int groupPosition) {
            				String text = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1)+" collapsed";
                    		// When clicked, show a toast with the TextView text
                    		Toast.makeText(getApplicationContext(), text ,
                    				Toast.LENGTH_SHORT).show();
                    		say(text);
                        					
            			}
                    	
                    }
                    );
                    /*lv.setOnGroupClickListener(new OnGroupClickListener()
                    {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, 
                            View view, int groupPosition, long id)
                        {
                        	String text = ((Cursor)mAdapter.getGroup(groupPosition)).getString(1);
                    		// When clicked, show a toast with the TextView text
                    		Toast.makeText(getApplicationContext(), text ,
                    				Toast.LENGTH_SHORT).show();
                    		say(text);
                        	return false;
                        }
                    });*/
                    lv.setOnScrollListener(new OnScrollListener(){
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        	/*String text = ((Cursor)mAdapter.getGroup(firstVisibleItem)).getString(1)+" on top of list";
                    		// When clicked, show a toast with the TextView text
                    		Toast.makeText(getApplicationContext(), text ,
                    				Toast.LENGTH_SHORT).show();
                    		say(text);*/
                        }
                       public void onScrollStateChanged(AbsListView view, int scrollState) {
                          // TODO Auto-generated method stub
                          if(scrollState == 0) {
                        	  int index = lv.getFirstVisiblePosition();
                        	  String text = ((Cursor)mAdapter.getGroup(index)).getString(1)+" on top of list";
                      		// When clicked, show a toast with the TextView text
                      		Toast.makeText(getApplicationContext(), text ,
                      				Toast.LENGTH_SHORT).show();
                      		say(text);
                          }
                        }
                      });

                    mQueryHandler = new QueryHandler(this, mAdapter);

                    // Query for people
                    mQueryHandler.startQuery(TOKEN_GROUP, null, Contacts.CONTENT_URI, CONTACTS_PROJECTION, 
                            Contacts.HAS_PHONE_NUMBER + "=1", null, null);
                }

                @Override
                protected void onDestroy() {
                	say("Showing Main menu");
                    super.onDestroy();

                    // Null out the group cursor. This will cause the group cursor and all of the child cursors
                    // to be closed.
                    mAdapter.changeCursor(null);
                    mAdapter = null;
                   /* if (tts != null) {
            			tts.stop();
            			tts.shutdown();
            		}*/
                    

            		
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
                private void call(String phoneNumber) {
            		try {
            			Intent callIntent = new Intent(Intent.ACTION_CALL);
            			callIntent.setData(Uri.parse("tel:" + phoneNumber));
            			startActivity(callIntent);
            		} catch (ActivityNotFoundException activityException) {
            			Log.e("dialing-example", "Call failed", activityException);
            		}
            	}

            	
            }