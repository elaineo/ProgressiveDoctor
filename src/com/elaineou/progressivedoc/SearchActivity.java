package com.elaineou.progressivedoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener  {
	 public final static String PDSearchUri = "https://progressdoctor.appspot.com/search/mobile";
	 public final static String TAG = "SearchActivity";
	 public final static String BETOS_LINKS = "com.elaineou.progressivedoc.BETOS_LINKS"; 
	 
	 private EditText pSearch;
	 private Button bSearch;	
	 
	 private String txtSearch;
	 private ArrayList<BetosLinks> betos;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	pSearch = (EditText) findViewById(R.id.MainSearch);
    	bSearch = (Button) findViewById(R.id.btnSearch);
        bSearch.setOnClickListener(this);
        
    }
	 public void onClick(View view) {
	      if (view.getId() == R.id.btnSearch) { 	   
	    	  txtSearch = pSearch.getText().toString();
		      if(txtSearch.equalsIgnoreCase("")) {
		    	  showToastMessage("Please enter some search terms.");  
		    	  return;
		      }
	    	  //send search terms to server 
	    	  GetSearch pd_getter = new GetSearch();
	    	  try {
				betos = pd_getter.execute(txtSearch).get();
		    	if(betos!=null) {
		    		callPageActivity(betos);
		    	} else {
		    		showToastMessage("Connection to server failed.");
			    	return;	
		    	}
			  } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  } catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	 }
	
	 public void callPageActivity(ArrayList<BetosLinks> result) {
	        //Start the Betos Page
	        Intent intent = new Intent(this, BetosActivity.class);
	        intent.putParcelableArrayListExtra(BETOS_LINKS, result);
			startActivity(intent);
			finish();
	 }
	 
	 class GetSearch extends AsyncTask<String,Void,ArrayList<BetosLinks>> {
		 @Override
		 protected ArrayList<BetosLinks> doInBackground(String... params) {		      
			 ArrayList<BetosLinks> betos = new ArrayList<BetosLinks>();
			 // Creating HTTP client
			 HttpClient httpClient = new DefaultHttpClient();
			 String paramstr = "?terms="+txtSearch;
			 HttpPost httpPost = new HttpPost(PDSearchUri+paramstr);
		      
		      // Making HTTP Request
		      try {
		          HttpResponse response = httpClient.execute(httpPost);
		          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		          StringBuilder builder = new StringBuilder();
		          for (String line = null; (line = reader.readLine()) != null;) {
		              builder.append(line).append("\n");
		          }
		          Log.d("Http Response:", builder.toString());
		          try {
			          JSONTokener tokener = new JSONTokener(builder.toString());
			          JSONObject object = (JSONObject) tokener.nextValue();
			          JSONArray posts = object.getJSONArray("posts");
			          if (posts.length()<1) {
			    		  showToastMessage("No results found. Make sure you are only using words that a doctor would use.");
			        	  return null;
			          }
			          for (int i = 0; i < posts.length(); i++) {  
			        	     JSONObject childJSONObject = posts.getJSONObject(i);
			        	     String cc = childJSONObject.getString("cc");
			        	     String icd = childJSONObject.getString("icd");
			        	     betos.add(new BetosLinks(cc,icd));
			        	}
			          return betos;
  		          } catch (JSONException e) {
		              // TODO Auto-generated catch block
		              e.printStackTrace();
		          }    
		          // writing response to log
		      } catch (ClientProtocolException e) {
		          // writing exception to log
		          e.printStackTrace();
		      } catch (IOException e) {
		          // writing exception to log
		          e.printStackTrace();
		      }
		      return null;
	   }
	    protected void onPostExecute(ArrayList<BetosLinks> result) {
	        returnToken(result);
	    }
	    private ArrayList<BetosLinks> returnToken(ArrayList<BetosLinks> result) {
	    	return result;
	    }
	 }	 
	 /**
	 * Helper method to show the toast message
	 **/
	 void showToastMessage(String message){
	  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.about:
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}