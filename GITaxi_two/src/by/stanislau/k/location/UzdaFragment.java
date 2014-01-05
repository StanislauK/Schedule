package by.stanislau.k.location;

import stanislau.gitaxi_two.R;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UzdaFragment extends Fragment {
    
	Typeface type;
	private static String FONT = "HelveticaNeueCyr-Light.otf";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		type = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), FONT);
		View view = inflater.inflate(R.layout.location_fragment_uzda, container, false);
		TextView ost_p = (TextView)view.findViewById(R.id.stopping_points_u);
		ost_p.setTypeface(type);
		
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
