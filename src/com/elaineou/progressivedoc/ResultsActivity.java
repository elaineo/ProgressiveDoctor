package com.elaineou.progressivedoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ResultsActivity extends Activity implements OnClickListener  {
	 public final static String PDResultsUri = "https://progressdoctor.appspot.com/betos/mobile";
	 public final static String TAG = "ResultsActivity";
	 public final static String ICD_RESULTS = "com.elaineou.progressivedoc.ICD_RESULTS"; 
	 
	 private Button bOk;	
	 
	 private ArrayList<ICDResults> icd;
	private String icdLink;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        
    	bOk = (Button) findViewById(R.id.btnOk);
        bOk.setOnClickListener(this);

		Intent intent = getIntent();
		icdLink = intent.getStringExtra(BetosActivity.ICD_RESULTS);

        //retrieve results 
  	  	GetICD icd_getter = new GetICD();
		try {
			icd = icd_getter.execute(icdLink).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        final ListView listview = (ListView) findViewById(R.id.listrview);		
	    ResultsAdapter adapter = new ResultsAdapter(this, icd);
	    listview.setAdapter(adapter);                
    }
	 public void onClick(View view) {
	      if (view.getId() == R.id.btnOk) { 	   
	    	  callNewActivity();
	      }
	 }
	
	 public void callNewActivity() {
	        //Start over
	        Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			finish();
	 }
	 
	 class GetICD extends AsyncTask<String,Void,ArrayList<ICDResults>> {
		 @Override
		 protected ArrayList<ICDResults> doInBackground(String... params) {		      
			 ArrayList<ICDResults> icds = new ArrayList<ICDResults>();
			 // Creating HTTP client
			 HttpClient httpClient = new DefaultHttpClient();
			 String paramstr = "/"+icdLink;
			 HttpGet httpGet = new HttpGet(PDResultsUri+paramstr);
		      
		      // Making HTTP Request
		      try {
		          HttpResponse response = httpClient.execute(httpGet);
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
			          for (int i = 0; i < posts.length(); i++) {  
			        	     JSONObject childJSONObject = posts.getJSONObject(i);
			        	     String minp = childJSONObject.getString("minpay");
			        	     String maxp = childJSONObject.getString("maxpay");
			        	     String t = childJSONObject.getString("treatment");
			        	     icds.add(new ICDResults(t,maxp,minp));
			        	}
			          return icds;
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
	    protected void onPostExecute(ArrayList<ICDResults> result) {
	        returnToken(result);
	    }
	    private ArrayList<ICDResults> returnToken(ArrayList<ICDResults> result) {
	    	return result;
	    }
	 }	 
}