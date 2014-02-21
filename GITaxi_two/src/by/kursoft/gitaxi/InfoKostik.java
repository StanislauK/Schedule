package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class InfoKostik extends SherlockDialogFragment implements android.view.View.OnClickListener{
	
	Typeface typeface;

	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		  	Drawable background = getResources().getDrawable(R.drawable.ab_background_textured_gitaxi);
		  	background.setAlpha(Consts.ALPHA);
		    getDialog().setTitle(R.string.kostik);
		    getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		    typeface = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), Consts.FONT);
		    View view = inflater.inflate(R.layout.infokostik, null);
		    view.findViewById(R.id.cancelDialogkostik).setOnClickListener(this);
		    ((TextView)view.findViewById(R.id.passperevkostik)).setTypeface(typeface);
		    ((TextView)view.findViewById(R.id.telMTCkostik)).setTypeface(typeface);
		    ((TextView)view.findViewById(R.id.telVELkostik)).setTypeface(typeface);
		    ((TextView)view.findViewById(R.id.number1)).setTypeface(typeface);
		    ((TextView)view.findViewById(R.id.number2)).setTypeface(typeface);
		    ((TextView)view.findViewById(R.id.number3)).setTypeface(typeface);
		    ((TextView)view.findViewById(R.id.number4)).setTypeface(typeface);
		    return view;
		  }

		  public void onClick(View v) {
		    dismiss();
		  }

		  
	
}
