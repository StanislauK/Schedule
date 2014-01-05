package stanislau.gitaxi_two;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import by.stanislau.k.location.LocationActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import database.DB;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends SherlockActivity implements
		OnItemClickListener {

	ActionBar mActionBar;
	public Context mContext;
	DB db;
	Cursor c;
	SQLiteDatabase sqlDataBase;
	MySimpleCursorAdapter mySimpleCA;
	CustomAlertList adapter;

	Typeface type;

	UpdateSheldue update;

	SharedPreferences sPref;
	Editor ed;

	Calendar calendar;

	int version_of_db;
	int current_version = 0;
	int dow;
	int current_day = 0;

	int owner_id = 0;

	ListView listView_um;
	ListView listView_mu;
	ViewPager mViewPager;

	Menu mMenu;
	SubMenu mSubMenu;

	TextView owner;
	TextView time;

	static final private int MENU_TITLE = Menu.FIRST;
	String day_of_week = null;
	String current_day_of_week;
	String temp_day;
	String u_m = "0";
	String m_u = "1";
	String[] operators = { "MTC", "Velcom" };
	Integer[] imageId = { R.drawable.ic_mts_logo, R.drawable.ic_velcom_logo };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		calendar = Calendar.getInstance();
		dow = calendar.get(Calendar.DAY_OF_WEEK);

		current_day = dow + 2;

		mContext = this;
		db = new DB(mContext);
		db.open();
		sqlDataBase = db.dbhelper.getReadableDatabase();
		c = sqlDataBase.query(DB.DB_TABLE_TEMP, null, null, null, null, null,
				null);

		listView_um = new ListView(mContext);
		listView_um.setOnItemClickListener(this);
		listView_mu = new ListView(mContext);
		listView_mu.setOnItemClickListener(this);

		sPref = getPreferences(MODE_PRIVATE);
		ed = sPref.edit();

		if (c.getCount() == 0) {
			ed.putInt(DB.SAVE_VERSION, current_version);
			ed.commit();
			if (Utils.isNetworkAvailable(MainActivity.this)) {
				new AlertDialog.Builder(mContext)
						.setCancelable(false)
						.setMessage(
								"При первом запуске приложения необходимо загрузить расписание")
						.setPositiveButton("Начать",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										new UpdateSheldue().execute();
										dialog.cancel();
									}
								}).show();

			} else {
				Crouton.showText(this, "Отсутствует соединение с интернетом",
						Style.ALERT, R.id.listMode);
			}
		}

		List<View> pages = new ArrayList<View>();
		pages.add(listView_um);
		pages.add(listView_mu);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		TaxiViewPagerAdapter mTaxiViewPagerAdapter = new TaxiViewPagerAdapter(
				mContext, pages);
		mViewPager.setAdapter(mTaxiViewPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(mViewPagerPageChangeListener);

		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab = mActionBar.newTab();
		tab.setText("из г. Узда");
		tab.setTabListener(mActionBarTabListener);

		mActionBar.addTab(tab);
		tab = mActionBar.newTab();
		tab.setText("из г. Минска");
		tab.setTabListener(mActionBarTabListener);
		mActionBar.addTab(tab);

		setTitle(dow);
		time = (TextView) findViewById(R.id.time);

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
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int position) {
		}
	};

	class UpdateSheldue extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		String version_of_database = null;
		String result = null;
		int version;
		int temp;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Обновляю...");
			pDialog.setCancelable(false);
			pDialog.show();
			version = sPref.getInt(DB.SAVE_VERSION, 0);
			temp = current_day;
		}

		@Override
		protected Void doInBackground(Void... params) {

			version_of_database = Utils.getJSONString(Consts.urlVersion);

			if (getVersion(version_of_database) != version) {
				ed.putInt(DB.SAVE_VERSION, getVersion(version_of_database));
				ed.commit();
				result = Utils.getJSONString(Consts.urlSchedule);
			} else {
				result = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void xxx) {
			super.onPostExecute(xxx);
			pDialog.dismiss();

			if (result == null || result.length() == 0) {
				Crouton.showText(MainActivity.this, "Расписание не изменялось",
						Style.CONFIRM, R.id.listMode);
			} else {
				try {
					Crouton.showText(MainActivity.this, "Обновление завершено",
							Style.CONFIRM, R.id.listMode);
					afterRefresh(temp);
					sqlDataBase.delete(DB.DB_TABLE_TEMP, null, null);
					saveSheldule(DB.ARRAY_UZDA_MINSK, result);
					saveSheldule(DB.ARRAY_MINSK_UZDA, result);
					afterRefresh(current_day);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		this.mMenu = menu;
		mSubMenu = menu.addSubMenu(1, MENU_TITLE, 1, day_of_week);
		mSubMenu.add(0, 2, 2, "Сегодня");
		// mSubMenu.add(0, 3, 3, "Завтра");
		mSubMenu.add(0, 4, 4, "Понедельник");
		mSubMenu.add(0, 5, 5, "Вторник");
		mSubMenu.add(0, 6, 6, "Среда");
		mSubMenu.add(0, 7, 7, "Четверг");
		mSubMenu.add(0, 8, 8, "Пятница");
		mSubMenu.add(0, 9, 9, "Суббота");
		mSubMenu.add(0, 10, 10, "Воскресенье");
		MenuItem subMenuItem = mSubMenu.getItem();
		subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		SubMenu location_subMenu = menu.addSubMenu(2, 11, 1, "Location")
				.setIcon(R.drawable.location_map);
		MenuItem location_subMenuItem = location_subMenu.getItem();
		location_subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		SubMenu overflow_subMenu = menu.addSubMenu(3, 12, 2, "Info").setIcon(
				R.drawable.ic_action_overflow);
		overflow_subMenu.add(3, 13, 2, "Обновить расписание");

		MenuItem overflow_subMenuItem = overflow_subMenu.getItem();
		overflow_subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	public void saveSheldule(String array, String result) throws JSONException {

		JSONObject rootJson = new JSONObject(result);
		JSONArray jsonArray = rootJson.getJSONArray(array);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject objJson = jsonArray.getJSONObject(i);
			db.addRec(objJson.getString(DB.COLUMN_DAY),
					objJson.getString(DB.COLUMN_TIME),
					objJson.getString(DB.COLUMN_OWNER),
					objJson.getInt(DB.COLUMN_DIRECTION));
		}
	}

	public int getVersion(String result) {
		try {
			JSONObject rootJson = new JSONObject(result);
			version_of_db = rootJson.getInt("version");
		} catch (JSONException e) {
			e.printStackTrace();
			version_of_db = -1;
		}

		return version_of_db;

	}

	private void setOptionTitle(int id, String title) {
		MenuItem item = mMenu.findItem(id);
		item.setTitle(title);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {
		case 2:
			setOptionTitle(MENU_TITLE, getCurrentDay(dow));
			Adapter(temp_day, u_m, listView_um);
			Adapter(temp_day, m_u, listView_mu);
			break;
		case 3:
			// setOptionTitle(MENU_TITLE, Consts.TOMORROW);
			break;
		case 4:
			setOptionTitle(MENU_TITLE, Consts.MONDAY);
			Adapter("monday", u_m, listView_um);
			Adapter("monday", m_u, listView_mu);
			current_day = 4;
			break;
		case 5:
			setOptionTitle(MENU_TITLE, Consts.TUESDAY);
			Adapter("tuesday", u_m, listView_um);
			Adapter("tuesday", m_u, listView_mu);
			current_day = 5;
			break;

		case 6:
			setOptionTitle(MENU_TITLE, Consts.WEDNESDAY);
			Adapter("wednesday", u_m, listView_um);
			Adapter("wednesday", m_u, listView_mu);
			current_day = 6;
			break;
		case 7:
			setOptionTitle(MENU_TITLE, Consts.THURSDAY);
			Adapter("thursday", u_m, listView_um);
			Adapter("thursday", m_u, listView_mu);
			current_day = 7;
			break;
		case 8:
			setOptionTitle(MENU_TITLE, Consts.FRIDAY);
			Adapter("friday", u_m, listView_um);
			Adapter("friday", m_u, listView_mu);
			current_day = 8;
			break;
		case 9:
			setOptionTitle(MENU_TITLE, Consts.SATURDAY);
			Adapter("saturday", u_m, listView_um);
			Adapter("saturday", m_u, listView_mu);
			current_day = 9;
			break;
		case 10:
			setOptionTitle(MENU_TITLE, Consts.SUNDAY);
			Adapter("sunday", u_m, listView_um);
			Adapter("sunday", m_u, listView_mu);
			current_day = 10;
			break;
		case 11:
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.animation_first, R.anim.animation_two);
			break;
		case 13:
			if (Utils.isNetworkAvailable(MainActivity.this)) {
				new UpdateSheldue().execute();
			} else {
				Crouton.showText(this, "Проверьте соединение с интернетом",
						Style.ALERT, R.id.listMode);
			}
			break;
		case android.R.id.home:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		owner = ((TextView) view.findViewById(R.id.owner));
		if (owner.equals(Consts.KONSTANTIN)) {
			owner_id = 1;
		} else
			owner_id = 2;
		CustomDialog(owner_id);

	}

	public void setTitle(int day) {
		switch (day) {
		case 1:
			current_day_of_week = Consts.SUNDAY;
			day_of_week = Consts.SUNDAY;
			Adapter("sunday", u_m, listView_um);
			Adapter("sunday", m_u, listView_mu);
			break;
		case 2:
			current_day_of_week = Consts.MONDAY;
			day_of_week = Consts.MONDAY;
			Adapter("monday", u_m, listView_um);
			Adapter("monday", m_u, listView_mu);
			break;
		case 3:
			current_day_of_week = Consts.TUESDAY;
			day_of_week = Consts.TUESDAY;
			Adapter("tuesday", u_m, listView_um);
			Adapter("tuesday", m_u, listView_mu);
			break;
		case 4:
			current_day_of_week = Consts.WEDNESDAY;
			day_of_week = Consts.WEDNESDAY;
			Adapter("wednesday", u_m, listView_um);
			Adapter("wednesday", m_u, listView_mu);
			break;
		case 5:
			current_day_of_week = Consts.THURSDAY;
			day_of_week = Consts.THURSDAY;
			Adapter("thursday", u_m, listView_um);
			Adapter("thursday", m_u, listView_mu);
			break;
		case 6:
			current_day_of_week = Consts.FRIDAY;
			day_of_week = Consts.FRIDAY;
			Adapter("friday", u_m, listView_um);
			Adapter("friday", m_u, listView_mu);
			break;
		case 7:
			current_day_of_week = Consts.SATURDAY;
			day_of_week = Consts.SATURDAY;
			Adapter("saturday", u_m, listView_um);
			Adapter("saturday", m_u, listView_mu);
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void Adapter(String day, String direction, ListView lv) {

		String[] whereArgs = { day, direction };

		Cursor cursor = db.getData(whereArgs);
		this.startManagingCursor(cursor);

		String[] from = new String[] { DB.COLUMN_TIME, DB.COLUMN_OWNER };
		int[] to = new int[] { R.id.time, R.id.owner };

		mySimpleCA = new MySimpleCursorAdapter(mContext, R.layout.temp, cursor,
				from, to);

		lv.setAdapter(mySimpleCA);

	}

	public void afterRefresh(int day) {
		switch (day) {
		case 4:
			Adapter("monday", u_m, listView_um);
			Adapter("monday", m_u, listView_mu);
			break;
		case 5:
			Adapter("tuesday", u_m, listView_um);
			Adapter("tuesday", m_u, listView_mu);
			break;
		case 6:
			Adapter("wednesday", u_m, listView_um);
			Adapter("wednesday", m_u, listView_mu);
			break;
		case 7:
			Adapter("thursday", u_m, listView_um);
			Adapter("thursday", m_u, listView_mu);
			break;
		case 8:
			Adapter("friday", u_m, listView_um);
			Adapter("friday", m_u, listView_mu);
			break;
		case 9:
			Adapter("saturday", u_m, listView_um);
			Adapter("saturday", m_u, listView_mu);
			break;
		case 10:
			Adapter("sunday", u_m, listView_um);
			Adapter("sunday", m_u, listView_mu);
			break;

		}
	}

	public String getCurrentDay(int dow) {
		switch (dow) {
		case 1:
			current_day_of_week = Consts.SUNDAY;
			temp_day = "sunday";
			break;
		case 2:
			current_day_of_week = Consts.MONDAY;
			temp_day = "monday";
			break;
		case 3:
			current_day_of_week = Consts.TUESDAY;
			temp_day = "tuesday";
			break;
		case 4:
			current_day_of_week = Consts.WEDNESDAY;
			temp_day = "wednesday";
			break;
		case 5:
			current_day_of_week = Consts.THURSDAY;
			temp_day = "thursday";
			break;
		case 6:
			current_day_of_week = Consts.FRIDAY;
			temp_day = "friday";
			break;
		case 7:
			current_day_of_week = Consts.SATURDAY;
			temp_day = "saturday";
			break;
		}
		return current_day_of_week;
	}

	public void dial(String tel) {
		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(tel)));
	}

	private void CustomDialog(int id) {
		Dialog dialog = null;

		ListView dialog_listview;

		dialog = new Dialog(mContext);
		dialog.setTitle("Ваш оператор?");
		dialog.setContentView(R.layout.dialog_list);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		// Prepare ListView in dialog
		dialog_listview = (ListView) dialog.findViewById(R.id.alertlist);
		adapter = new CustomAlertList(MainActivity.this, operators, imageId);
		dialog_listview.setAdapter(adapter);

		dialog_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					if (owner.getText().equals(Consts.KONSTANTIN)) {
						dial(Consts.NUMBER_KONSTANTIN_MTC);
					}
					if (owner.getText().equals(Consts.MISHONOK)) {
						dial(Consts.NUMBER_MISHONOK_MTC);
					}
					break;

				case 1:
					if (owner.getText().equals(Consts.KONSTANTIN)) {
						dial(Consts.NUMBER_KONSTANTIN_VELCOM);
					}
					if (owner.getText().equals(Consts.MISHONOK)) {
						dial(Consts.NUMBER_MISHONOK_VELCOM);
					}
					break;
				}

			}
		});

		dialog.show();
	}

}
