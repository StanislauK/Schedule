package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class CallDialog extends SherlockDialogFragment {

	private ListView dialog_listview;
	private CustomAlertList adapter;
	private String[] operators = { "MTC", "Velcom" };
	private Integer[] imageIds = { R.drawable.ic_mts_logo,
			R.drawable.ic_velcom_logo };

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Drawable background = getResources().getDrawable(
				R.drawable.ab_background_textured_gitaxi);
		background.setAlpha(Consts.ALPHA);
		getDialog().setTitle("Выберите оператора");
		getDialog().setContentView(R.layout.dialog_list);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.dialog_list, null);
		dialog_listview = (ListView) view.findViewById(R.id.alertlist);
		dialog_listview.setOnItemClickListener(listener);
		adapter = new CustomAlertList(getActivity(), operators, imageIds);
		dialog_listview.setAdapter(adapter);
		return view;
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				if (MainActivity.owner.getText().equals(Consts.KONSTANTIN)) {
					HelpFunctions.dial(Consts.NUMBER_KONSTANTIN_MTC,
							getActivity());
				}
				if (MainActivity.owner.getText().equals(Consts.MISHONOK)) {
					HelpFunctions.dial(Consts.NUMBER_MISHONOK_MTC,
							getActivity());
				}
				break;

			case 1:
				if (MainActivity.owner.getText().equals(Consts.KONSTANTIN)) {
					HelpFunctions.dial(Consts.NUMBER_KONSTANTIN_VELCOM,
							getActivity());
				}
				if (MainActivity.owner.getText().equals(Consts.MISHONOK)) {
					HelpFunctions.dial(Consts.NUMBER_MISHONOK_VELCOM,
							getActivity());
				}
				break;
			}
		}
	};

}
