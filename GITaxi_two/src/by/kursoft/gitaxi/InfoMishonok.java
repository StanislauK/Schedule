package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class InfoMishonok extends SherlockDialogFragment implements OnClickListener{
	
	Typeface typeface;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Drawable background = getResources().getDrawable(R.drawable.ab_background_textured_gitaxi);
	  	background.setAlpha(Consts.ALPHA);
		getDialog().setTitle(R.string.mishonok);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		typeface = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), Consts.FONT);
		View view = inflater.inflate(R.layout.infomishonok, null);
		view.findViewById(R.id.cancelDialogmishonok).setOnClickListener(this);
		((TextView)view.findViewById(R.id.passperevmishonok)).setTypeface(typeface);
		((TextView)view.findViewById(R.id.telMTCmishonok)).setTypeface(typeface);
		((TextView)view.findViewById(R.id.telVELmishonok)).setTypeface(typeface);
		return view;
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}
}
