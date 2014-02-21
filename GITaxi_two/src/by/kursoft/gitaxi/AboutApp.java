package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class AboutApp extends SherlockActivity {
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapp);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Consts.FONT);
		
		TextView myDescription = (TextView)findViewById(R.id.myDescription);
		myDescription.setTypeface(typeface);
		TextView about = (TextView)findViewById(R.id.about);
		about.setTypeface(typeface);
		TextView myName = (TextView)findViewById(R.id.myName);
		myName.setTypeface(typeface);
		TextView developer = (TextView)findViewById(R.id.developer);
		developer.setTypeface(typeface);
		TextView myContact = (TextView)findViewById(R.id.myContact);
		myContact.setTypeface(typeface);
		TextView email = (TextView)findViewById(R.id.email);
		email.setTypeface(typeface);
		TextView myPhone = (TextView)findViewById(R.id.myPhone);
		myPhone.setTypeface(typeface);

	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			Intent homeIntent = new Intent(this, MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
