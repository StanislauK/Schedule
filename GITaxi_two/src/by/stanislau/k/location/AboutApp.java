package by.stanislau.k.location;

import stanislau.gitaxi_two.MainActivity;
import stanislau.gitaxi_two.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class AboutApp extends SherlockActivity {

	ActionBar mActionBar;
	private TextView application_name;
	private TextView application_version;
	private TextView about;
	private TextView contact_info;
	private TextView e_mail;
	private TextView other;
	private RelativeLayout rl;
	private Drawable rl_background;
	private Typeface typeFace;
	private static String FONT = "HelveticaNeueCyr-Light.otf";

	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);

		mActionBar = getSupportActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		application_name = (TextView) findViewById(R.id.application_name);
		application_version = (TextView) findViewById(R.id.application_version);
		about = (TextView) findViewById(R.id.about);
		contact_info = (TextView) findViewById(R.id.contact_info);
		e_mail = (TextView) findViewById(R.id.e_mail);
		other = (TextView) findViewById(R.id.other);
		
		rl = (RelativeLayout) findViewById(R.id.about_layout);

		rl_background = getResources().getDrawable(
				R.drawable.ab_background_textured_gitaxi);
		rl_background.setAlpha(60);
		
		if (Build.VERSION.SDK_INT >= 16){
			rl.setBackground(rl_background);
		} else {
			rl.setBackgroundDrawable(rl_background);
		}		
		typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(), FONT);
		
		application_name.setTypeface(typeFace);
		application_version.setTypeface(typeFace);
		about.setTypeface(typeFace);
		contact_info.setTypeface(typeFace);
		e_mail.setTypeface(typeFace);
		other.setTypeface(typeFace);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent homeIntent = new Intent(this, MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			overridePendingTransition(R.anim.animation_first_l,
					R.anim.animation_two_l);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent homeIntent = new Intent(this, MainActivity.class);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeIntent);
		overridePendingTransition(R.anim.animation_first_l,
				R.anim.animation_two_l);
		super.onBackPressed();
	}
}
