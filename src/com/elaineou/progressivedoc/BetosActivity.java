package com.elaineou.progressivedoc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BetosActivity extends Activity {
	 public final static String PDBetosUri = "https://progressdoctor.appspot.com/betos/mobile";
	 public final static String BETOS_LINKS = "com.elaineou.progressivedoc.BETOS_LINKS"; 
	 public final static String ICD_RESULTS = "com.elaineou.progressivedoc.ICD_RESULTS"; 
	 public final static String TAG = "BetosActivity";
	 
	 private ArrayList<BetosLinks> betos;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betos);
        
        final ListView listview = (ListView) findViewById(R.id.listview);
		Intent intent = getIntent();
		betos = intent.getParcelableArrayListExtra(SearchActivity.BETOS_LINKS);
		
	    SearchAdapter adapter = new SearchAdapter(this, betos);
	    listview.setAdapter(adapter);        
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view,
	            int position, long id) {
	          BetosLinks item = (BetosLinks) parent.getItemAtPosition(position);
	          Log.d(TAG,Integer.toString(position));
	          callResultsActivity(item.getICD());
	        }

	      });	    
		 
    }
		
	 public void callResultsActivity(String icd) {
	        //Go to final results		 
	        Intent intent = new Intent(this, ResultsActivity.class);
	        intent.putExtra(ICD_RESULTS, icd);
			startActivity(intent);
			finish();
	 }
	 
	
}