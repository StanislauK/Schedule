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

public class UzdaFragment extends Fragment {
    
	Typeface type;
	private static String FONT = "HelveticaNeueCyr-Light.otf";
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Drawable uzda_foto = getResources().getDrawable(R.drawable.foto_uzda);
		uzda_foto.setAlpha(80);
		type = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), FONT);
		View view = inflater.inflate(R.layout.location_fragment_uzda, container, false);
		TextView ost_p = (TextView)view.findViewById(R.id.stopping_points_u);
		ost_p.setTypeface(type);
		if (Build.VERSION.SDK_INT >= 16){
			view.setBackground(uzda_foto);
		} else {
			view.setBackgroundDrawable(uzda_foto);
		}
		
		return view;
	}
	
	public static UzdaFragment newInstance(String text) {

        UzdaFragment f = new UzdaFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    
}
