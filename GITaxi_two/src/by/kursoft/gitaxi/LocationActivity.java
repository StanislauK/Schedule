package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class LocationActivity extends SherlockFragmentActivity {

	ActionBar mActionBar;
	ViewPager mViewPager;
	SherlockDialogFragment kostik;
	DialogFragment mishonok;

	@Override
	protected void onCreate(Bundle savedInstantState) {
		super.onCreate(savedInstantState);
		setContentView(R.layout.location_activity);
		
		

		mViewPager = (ViewPager) findViewById(R.id.location_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(mViewPagerPageChangeListener);

		mActionBar = getSupportActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = mActionBar.newTab();
		tab.setText("из г. ”зда");
		tab.setTabListener(mActionBarTabListener);

		mActionBar.addTab(tab);
		tab = mActionBar.newTab();
		tab.setText("из г. ћинска");
		tab.setTabListener(mActionBarTabListener);
		mActionBar.addTab(tab);
		
		kostik = new InfoKostik();
		mishonok = new InfoMishonok();

	}

	ActionBar.TabListener mActionBarTabListener = new ActionBar.TabListener() {

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			 mViewPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	};
	ViewPager.OnPageChangeListener mViewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(mViewPager.getCurrentItem());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
			case 0:
				return UzdaFragment.newInstance("UzdaFragment, Instance1");
			case 1:
				return MinskFragment.newInstance("MinskFragment, Instance1");
			default:
				return UzdaFragment.newInstance("UzdaFragment, Instance1");
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent homeIntent = new Intent(this, MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			break;
		case 2:
			kostik.show(getSupportFragmentManager(), "kostik");
			break;
		case 3:
			mishonok.show(getSupportFragmentManager(), "mishonok");
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		
		SubMenu subMenu = menu.addSubMenu(1, 1, 1, "More");
		subMenu.setIcon(R.drawable.action_about);
		subMenu.add(1,2,2,"»рин“ранс—ервис");
		subMenu.add(1,3,3,"”зда“рансѕлюс");
		MenuItem menuItem = subMenu.getItem();
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	@Override
	public void onBackPressed() {
		Intent homeIntent = new Intent(this, MainActivity.class);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeIntent);
		overridePendingTransition(R.anim.animation_first_l, R.anim.animation_two_l);
		super.onBackPressed();
	}

}
