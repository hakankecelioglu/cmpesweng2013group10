package com.groupon.mobile;

import java.util.ArrayList;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.service.CommunityService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MyCommunitiesActivity  extends BaseActivity {
	CommunityAdapter arrayAdapter;
	ArrayList<Community> communities=null;
	ListView  listview;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_communities);


		setupUI();
	}
	
	private void setupUI() {
		
	setupListView();
	listview.setOnItemClickListener(listViewListener);  
	}
	
    private void setupListView(){
    	
    	listview = (ListView) findViewById(R.id.listview);
    	CommunityService communityService=new CommunityService(getApp());
    	communities = new ArrayList<Community>();
        arrayAdapter = new CommunityAdapter(MyCommunitiesActivity.this, R.layout.listview_community, communities);
        listview.setAdapter(arrayAdapter);
        communityService.getCommunities(new GrouponCallback<ArrayList<Community>>() {
    		public void onSuccess(ArrayList<Community> response) {
    		    for (Community c : response) {
    		        communities.add(c);
    		    }

    			arrayAdapter.notifyDataSetChanged();
    		}

    		public void onFail(String errorMessage) {
    			Toast.makeText(MyCommunitiesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    		}
    	});
    }
    private OnItemClickListener listViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	    {
		  /*
	      Intent intent = new Intent(MyCommunitiesActivity.this,Community.class);
		  intent.putExtra("communityId", c.getId());
		  startActivity(intent);
		  */
	    }



		

	};
}
