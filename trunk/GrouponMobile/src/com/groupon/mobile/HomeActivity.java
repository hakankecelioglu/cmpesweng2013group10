package com.groupon.mobile;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.groupon.mobile.frag.CreateCommunityFragment;
import com.groupon.mobile.frag.HomeFragment;
import com.groupon.mobile.frag.MyCommunitiesFragment;
import com.groupon.mobile.frag.ProfileFragment;
import com.groupon.mobile.layout.NavDrawerItem;
import com.groupon.mobile.layout.NavDrawerListAdapter;
import com.groupon.mobile.model.User;

public class HomeActivity extends BaseActivity {
	private DrawerLayout mDrawerLayout;
	private ListView leftBarListView;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_activity2);
		User user = getLoggedUser();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftBarListView = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

		// Recycle the typed array
		navMenuIcons.recycle();

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);

		View header = View.inflate(HomeActivity.this, R.layout.left_bar_user_layout, null);
		TextView leftBarUsername = (TextView) header.findViewById(R.id.leftbar_username);
		leftBarUsername.setText(user.getUsername());
		leftBarListView.addHeaderView(header);
		leftBarListView.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close_home) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		leftBarListView.setOnItemClickListener(new SlideMenuClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(leftBarListView);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position == 0) {
				// listview header is clicked!
				return;
			}
			// display view for selected nav drawer item
			displayView(position - 1);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		boolean addBackState = true;

		switch (position) {
		case 0:
			fragment = new HomeFragment();
			addBackState = false;
			break;
		case 1:
			fragment = new CreateCommunityFragment();
			break;
		case 2:
			fragment = new ProfileFragment();
			break;
		case 3:
			fragment = new MyCommunitiesFragment();
			break;
		case 4:
			doSearch();
			break;
		case 5:
			doLogout();
			break;
		default:
			return;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction tr = fragmentManager.beginTransaction();
			if (!addBackState) {
				int bs = fragmentManager.getBackStackEntryCount();
				for (int i = 0; i < bs; i++) {
					fragmentManager.popBackStack();
				}
			} else {
				tr.addToBackStack(null);
			}

			tr.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			leftBarListView.setItemChecked(position + 1, true);
			leftBarListView.setSelection(position + 1);
			setTitle(navMenuTitles[position]);
		}

		mDrawerLayout.closeDrawer(leftBarListView);
	}

	private void doSearch() {
		// TODO not implemented yet!
	}

	private void doLogout() {
		
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 
			alertDialogBuilder.setTitle("Logout");

			alertDialogBuilder
				.setMessage("Are you sure to logout?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog,int id) {
						getApp().setAuthToken(null);
						getApp().setLoggedUser(null);

						Intent intent = new Intent(HomeActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
		
	}

}
