package by.kursoft.gitaxi;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import stanislau.gitaxi_two.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import by.kursoft.gitaxi.database.DB;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;


//����� ��������� ��� ��� ��� �������, ������ ���

public class MainActivity extends SherlockFragmentActivity implements
		OnItemClickListener {

	//��� ���  � �����	
		

	//Зачеим хранишь контекст???
	private Context mContext;  
	//Зачем хранишь ссылку на экшен бар??
	private ActionBar mActionBar;
	
	
	private DB db;
	
	//Зачем хранишь курсор?
	//Плохое название для курсора
	private Cursor c;
	private SQLiteDatabase sqlDataBase;
	//Плохое название для класса
	private MySimpleCursorAdapter mySimpleCA;
	

	private SharedPreferences sPref;
	//плохое название для эдитора
	private Editor ed;

	private Calendar calendar;

	private int currentVersion = 0;
	
	//Что такое DOW???
	private int dow;
	private int currentDay = 0;

	private ListView listViewFromUzdaToMinsk;
	private ListView listViewFromMinskToUzda;
	private ViewPager mViewPager;

	private Menu mMenu;
	private SubMenu mSubMenu;

	public static TextView owner;
	public static TextView time;

	//Зачем инициализируешь строку??? И почему тогда остальны ене инициализируешь???
	private String dayOfWeek = null;
	private String currentDayOfWeek;
	//Не соответствует конвенциям именования переменных в джава
	private String temp_day;
	
	//Неизменяемые значения выноси в константы
	private String fromUzdaToMinsk = "0";
	private String fromMinskToUzda = "1";
	
	//Желательно чтобы переменные не начинались с глагола
	private SherlockDialogFragment callDialog;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Все названия в ресурсы выноси
		getSupportActionBar().setTitle("�����");
		
		//Лучше вынести в отдельный метож
		calendar = Calendar.getInstance();
		dow = calendar.get(Calendar.DAY_OF_WEEK);
		currentDay = dow + 2;
		
		//это вообще гадство какое-то )))
		mContext = this;
		
		//инициализация БД в отдельный метож
		db = new DB(mContext);
		db.open();
		sqlDataBase = db.dbhelper.getReadableDatabase();
		
		//про название курсора писал. Инициализируй курсор перед испоьзованием
		c = sqlDataBase.query(DB.DB_TABLE_TEMP, null, null, null, null, null,
				null);

		listViewFromUzdaToMinsk = new ListView(mContext);
		listViewFromUzdaToMinsk.setOnItemClickListener(this);
		listViewFromMinskToUzda = new ListView(mContext);
		listViewFromMinskToUzda.setOnItemClickListener(this);

		sPref = getPreferences(MODE_PRIVATE);
		ed = sPref.edit();
		
		// вынести  вотделньный метод
		if (c.getCount() == 0) {
			ed.putInt(DB.SAVE_VERSION, currentVersion);
			ed.commit();
			//опять таки ты создал переменную контект а используешь this.
			// Используй просто thшs а не  MainActivity.this
			if (Utils.isNetworkAvailable(MainActivity.this)) {
				firstDialog();
			} else {
				// Все строки в ресурсы
				HelpFunctions.showAlertCrouton(this, "���������� ���������� � ����������");
			}
		}

		// Зачем использовать Лист когда у тебя ограниченное и заранее известное количетсво маршрутов?
		// лучше листа использовать обычный массив. А еще лучше перечисление
		List<View> pages = new ArrayList<View>();
		pages.add(listViewFromUzdaToMinsk);
		pages.add(listViewFromMinskToUzda);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		TaxiViewPagerAdapter mTaxiViewPagerAdapter = new TaxiViewPagerAdapter(
				mContext, pages);
		mViewPager.setAdapter(mTaxiViewPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(mViewPagerPageChangeListener);

		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//строки в ресурсы
		mActionBar.setTitle("Taxi");

		
		Tab tab = mActionBar.newTab();
		//строки в ресурсы
		tab.setText("�� �. ����");
		tab.setTabListener(mActionBarTabListener);
		mActionBar.addTab(tab);
		
		//1. переменная таб инициализируется дважды. В данном случае лучше было завести вторую переменную
		//2. Копипаст! Избавься от него
		tab = mActionBar.newTab();
		//строки в ресурсы
		tab.setText("�� �. ������");
		tab.setTabListener(mActionBarTabListener);
		mActionBar.addTab(tab);

		setTitle(dow);
	}
	
	//Реализацию интерфейса выноси в отдельный класс. (Подсказка "во внутренний")
	//����� ��������� ������ � implements
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

	//Реализацию интерфейса выноси в отдельный класс. (Подсказка "во внутренний")
	//���� �����
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
			// Строки в ресурсы
			pDialog.setMessage("��������...");
			pDialog.setCancelable(false);
			pDialog.show();
			version = sPref.getInt(DB.SAVE_VERSION, 0);
			temp = currentDay;
			
			/*
				Разделяй код в методах на логические функциональные блоки а не пиши все в кучу типа:
				pDialog.setMessage("��������...");
				pDialog.setCancelable(false);
				pDialog.show();
				version = sPref.getInt(DB.SAVE_VERSION, 0);
				temp = currentDay;
			*/
		}

		@Override
		protected Void doInBackground(Void... params) {
			// 1. Не правиьлное именование переменной
			// 2. эту переменную можно сделать константой, т.к. она не будет изменяться
			version_of_database = Utils.getJSONString(getApplicationContext(), Consts.urlVersion);
			if (JSONReader.getVersion(version_of_database) != version) {
				ed.putInt(DB.SAVE_VERSION, JSONReader.getVersion(version_of_database));
				ed.commit();
				result = Utils.getJSONString(getApplicationContext(), Consts.urlSchedule);
			} else {
				//возвращение null - неочень хорошая идея. Лучше вернуть пустую строку.
				result = null;
			}

			return null;
		}

		@Override
		//плохое название параметра ххх
		protected void onPostExecute(Void xxx) {
			super.onPostExecute(xxx);
			pDialog.dismiss();

			if (result == null || result.length() == 0) {
				// Строки в ресурсы
				HelpFunctions.showConfirmCrouton(MainActivity.this, "���������� �� ����������");
			} else {
				//блок try-catch лучше вынести в отдельный метод.
				// Проглатываешь исключения. Не узнаешь об ошибке!
				try {
					// Строки в ресурсы
					HelpFunctions.showConfirmCrouton(MainActivity.this, "���������� ���������");
					afterRefresh(temp);
					sqlDataBase.delete(DB.DB_TABLE_TEMP, null, null);
					JSONReader.saveSheldule(DB.ARRAY_UZDA_MINSK, result, db);
					JSONReader.saveSheldule(DB.ARRAY_MINSK_UZDA, result, db);
					afterRefresh(currentDay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	//������ � �������
	//�������� ���� ������� � ������ Menu
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		this.mMenu = menu;
		mSubMenu = menu.addSubMenu(1, Consts.MENU_TITLE, 1, dayOfWeek);
		// Строки в ресурсы
		// опять же про логические блоки
		
		//меню тоже лучше в ресурсы вынести.
		mSubMenu.add(0, 2, 2, "�������");
		// mSubMenu.add(0, 3, 3, "������");
		mSubMenu.add(0, 4, 4, "�����������");
		mSubMenu.add(0, 5, 5, "�������");
		mSubMenu.add(0, 6, 6, "�����");
		mSubMenu.add(0, 7, 7, "�������");
		mSubMenu.add(0, 8, 8, "�������");
		mSubMenu.add(0, 9, 9, "�������");
		mSubMenu.add(0, 10, 10, "�����������");
		MenuItem subMenuItem = mSubMenu.getItem();
		subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		SubMenu location_subMenu = menu.addSubMenu(2, 11, 1, "Location")
				.setIcon(R.drawable.location_place);
		MenuItem location_subMenuItem = location_subMenu.getItem();
		location_subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		SubMenu overflow_subMenu = menu.addSubMenu(3, 12, 2, "Info").setIcon(
				R.drawable.ic_action_overflow);
		overflow_subMenu.add(3, 13, 2, "�������� ����������");
		overflow_subMenu.add(3, 14, 2, "� ����������");

		MenuItem overflow_subMenuItem = overflow_subMenu.getItem();
		overflow_subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}


	private void setOptionTitle(int id, String title) {
		MenuItem item = mMenu.findItem(id);
		item.setTitle(title);
	}

	//���������� ����� ����. ���� ������� � �����
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		
		// Копипаст. конкретнейший.
		// Строки в ресуры
		// на один блок case один метод
		
		switch (item.getItemId()) {
		case 2:
			setOptionTitle(Consts.MENU_TITLE, getCurrentDay(dow));
			Adapter(temp_day, fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter(temp_day, fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 3:
			// setOptionTitle(Consts.MENU_TITLE, Consts.TOMORROW);
			break;
		case 4:
			setOptionTitle(Consts.MENU_TITLE, Consts.MONDAY);
			Adapter("monday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("monday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 4;
			break;
		case 5:
			setOptionTitle(Consts.MENU_TITLE, Consts.TUESDAY);
			Adapter("tuesday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("tuesday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 5;
			break;

		case 6:
			setOptionTitle(Consts.MENU_TITLE, Consts.WEDNESDAY);
			Adapter("wednesday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("wednesday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 6;
			break;
		case 7:
			setOptionTitle(Consts.MENU_TITLE, Consts.THURSDAY);
			Adapter("thursday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("thursday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 7;
			break;
		case 8:
			setOptionTitle(Consts.MENU_TITLE, Consts.FRIDAY);
			Adapter("friday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("friday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 8;
			break;
		case 9:
			setOptionTitle(Consts.MENU_TITLE, Consts.SATURDAY);
			Adapter("saturday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("saturday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 9;
			break;
		case 10:
			setOptionTitle(Consts.MENU_TITLE, Consts.SUNDAY);
			Adapter("sunday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("sunday", fromMinskToUzda, listViewFromMinskToUzda);
			currentDay = 10;
			break;
		case 11:
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			break;
		case 13:
			if (Utils.isNetworkAvailable(MainActivity.this)) {
				new UpdateSheldue().execute();
			} else {
				HelpFunctions.showAlertCrouton(this,"��������� ���������� � ����������");
			}
			break;
		case 14:
			Intent aboutApp = new Intent(getApplicationContext(), AboutApp.class);
			startActivity(aboutApp);
			break;
		case android.R.id.home:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	// OnClickListener реализуй не в implement а в отдельном классе. 
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		owner = ((TextView) view.findViewById(R.id.owner));
		time = ((TextView) view.findViewById(R.id.time));
		callDialog = new CallDialog();
		callDialog.show(getSupportFragmentManager(), "callDialog");
		
	}

	//���������� ����� ����. ���� ������� � �����
	public void setTitle(int day) {
		switch (day) {
		case 1:
			currentDayOfWeek = Consts.SUNDAY;
			dayOfWeek = Consts.SUNDAY;
			Adapter("sunday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("sunday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 2:
			currentDayOfWeek = Consts.MONDAY;
			dayOfWeek = Consts.MONDAY;
			Adapter("monday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("monday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 3:
			currentDayOfWeek = Consts.TUESDAY;
			dayOfWeek = Consts.TUESDAY;
			Adapter("tuesday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("tuesday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 4:
			currentDayOfWeek = Consts.WEDNESDAY;
			dayOfWeek = Consts.WEDNESDAY;
			Adapter("wednesday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("wednesday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 5:
			currentDayOfWeek = Consts.THURSDAY;
			dayOfWeek = Consts.THURSDAY;
			Adapter("thursday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("thursday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 6:
			currentDayOfWeek = Consts.FRIDAY;
			dayOfWeek = Consts.FRIDAY;
			Adapter("friday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("friday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 7:
			currentDayOfWeek = Consts.SATURDAY;
			dayOfWeek = Consts.SATURDAY;
			Adapter("saturday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("saturday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		}
	}

	//�� ��������� ������ � ������ ������� ��������
	//�������� ������� � ��������� �����, � ���������� � �������. 
	//������� - ������ ��������. �� ����� ���������� � ��� ��� ������ �����
	
	//Никогда не используй устаревшие методы и классы.
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

	//���������� ����� ����. ���� ������� � �����
	public void afterRefresh(int day) {
		switch (day) {
		case 4:
			Adapter("monday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("monday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 5:
			Adapter("tuesday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("tuesday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 6:
			Adapter("wednesday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("wednesday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 7:
			Adapter("thursday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("thursday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 8:
			Adapter("friday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("friday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 9:
			Adapter("saturday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("saturday", fromMinskToUzda, listViewFromMinskToUzda);
			break;
		case 10:
			Adapter("sunday", fromUzdaToMinsk, listViewFromUzdaToMinsk);
			Adapter("sunday", fromMinskToUzda, listViewFromMinskToUzda);
			break;

		}
	}

	//���������� ����� ����. ���� ������� � �����
	//������ � �������
	public String getCurrentDay(int dow) {
		switch (dow) {
		case 1:
			currentDayOfWeek = Consts.SUNDAY;
			temp_day = "sunday";
			break;
		case 2:
			currentDayOfWeek = Consts.MONDAY;
			temp_day = "monday";
			break;
		case 3:
			currentDayOfWeek = Consts.TUESDAY;
			temp_day = "tuesday";
			break;
		case 4:
			currentDayOfWeek = Consts.WEDNESDAY;
			temp_day = "wednesday";
			break;
		case 5:
			currentDayOfWeek = Consts.THURSDAY;
			temp_day = "thursday";
			break;
		case 6:
			currentDayOfWeek = Consts.FRIDAY;
			temp_day = "friday";
			break;
		case 7:
			currentDayOfWeek = Consts.SATURDAY;
			temp_day = "saturday";
			break;
		}
		return currentDayOfWeek;
	}

	public void firstDialog(){
		AlertDialog.Builder adb = new Builder(mContext).
				setMessage("��� ������ ������� ���������� ��������� ����������").
				setPositiveButton("������", firstlistener).
				setCancelable(false);
				adb.show();
	}
	
	DialogInterface.OnClickListener firstlistener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			new UpdateSheldue().execute();
			dialog.cancel();
		}
	};
}
