package by.stanislau.k.location;

import stanislau.gitaxi_two.R;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MinskFragment extends Fragment {

	Typeface type;
	private static String FONT = "HelveticaNeueCyr-Light.otf";
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Drawable minsk_foto = getResources().getDrawable(R.drawable.foto_minsk);
		minsk_foto.setAlpha(80);
		type = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), FONT);
		View view = inflater.inflate(R.layout.location_fragment_minsk, container, false);
	    TextView tv = (TextView)view.findViewById(R.id.stopping_points_m);
	    tv.setTypeface(type);
	    
	    if (Build.VERSION.SDK_INT >= 16){
			view.setBackground(minsk_foto);
		} else {
			view.setBackgroundDrawable(minsk_foto);
		}
	    
	    return view;
	}

	public static MinskFragment newInstance(String text) {

	    MinskFragment f = new MinskFragment();
	    Bundle b = new Bundle();
	    b.putString("msg", text);

	    f.setArguments(b);

	    return f;
	}
	
	
}
