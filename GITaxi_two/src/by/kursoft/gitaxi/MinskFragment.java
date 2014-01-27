package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MinskFragment extends Fragment {

	Typeface type;
	private static String FONT = "HelveticaNeueCyr-Light.otf";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Drawable minsk_foto = getResources().getDrawable(R.drawable.foto_minsk);
		minsk_foto.setAlpha(80);
		type = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), FONT);
		View v = inflater.inflate(R.layout.location_fragment_minsk, container, false);
		v.setBackground(minsk_foto);
	    TextView tv = (TextView)v.findViewById(R.id.stopping_points_m);
	    tv.setTypeface(type);
	    return v;
	}

	public static MinskFragment newInstance(String text) {

	    MinskFragment f = new MinskFragment();
	    Bundle b = new Bundle();
	    b.putString("msg", text);

	    f.setArguments(b);

	    return f;
	}
	
	
}