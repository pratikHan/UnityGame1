package com.example.kluggame2;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class ScreenFoulGame extends Fragment implements OnClickListener {

	RadioButton rbswap, rbstart,rb1,rb2,rb3,rb4,rb5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_foul_screen, null);

		final RelativeLayout layoutcheckboxes = (RelativeLayout) view
				.findViewById(R.id.layout1);
		final RelativeLayout layoutradios = (RelativeLayout) view
				.findViewById(R.id.layout2);
		final LinearLayout linearhead1 = (LinearLayout) view
				.findViewById(R.id.layoutA);
		final LinearLayout linearhead2 = (LinearLayout) view
				.findViewById(R.id.layoutB);
		final RadioGroup radiogroup = (RadioGroup) view
				.findViewById(R.id.radioGroup1);
		rbswap = (RadioButton) view.findViewById(R.id.radioButtonSwap);
		rbstart = (RadioButton) view.findViewById(R.id.radioButtonRestart);
		rb1 = (RadioButton) view.findViewById(R.id.radio1);
		rb2 = (RadioButton) view.findViewById(R.id.radio2);
		rb3 = (RadioButton) view.findViewById(R.id.radio3);
		rb4 = (RadioButton) view.findViewById(R.id.radio4);
		rb5 = (RadioButton) view.findViewById(R.id.radio5);
		
		
		
		
		
		CheckBox cbcheckwrong = (CheckBox) view.findViewById(R.id.checkBox1);
		CheckBox cbnopawns = (CheckBox) view.findViewById(R.id.checkBox2);

		rbswap.setOnClickListener(this);
		rbstart.setOnClickListener(this);
		rb1.setOnClickListener(this);
		rb2.setOnClickListener(this);
		rb3.setOnClickListener(this);
		rb4.setOnClickListener(this);
		rb5.setOnClickListener(this);

		radiogroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						
						Log.d("Onchckedchange","CheckedId :"+checkedId+" Group :  "+group );
						switch (checkedId) {
						case R.id.radio1:
							Log.d("radio1","radio1");
							rb2.setChecked(false);
							rb3.setChecked(false);
							rb4.setChecked(false);
							rb5.setChecked(false);
							break;
						
						case R.id.radio2:
							Log.d("radio2","radio2");	
							rb1.setChecked(false);
							rb3.setChecked(false);
							rb4.setChecked(false);
							rb5.setChecked(false);
							break;	

						case R.id.radio3:
							rb2.setChecked(false);
							rb1.setChecked(false);
							rb4.setChecked(false);
							rb5.setChecked(false);
							break;
							
						case R.id.radio4:
							rb2.setChecked(false);
							rb3.setChecked(false);
							rb1.setChecked(false);
							rb5.setChecked(false);
							break;
							
						case R.id.radio5:
							rb2.setChecked(false);
							rb3.setChecked(false);
							rb4.setChecked(false);
							rb1.setChecked(false);
							break;	
						default:
							break;
						}
						radiogroup.clearCheck();
					
					}
				});

		cbcheckwrong.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					layoutcheckboxes.setVisibility(View.VISIBLE);
					linearhead1.setVisibility(View.VISIBLE);
					linearhead2.setVisibility(View.GONE);

				} else {
					layoutcheckboxes.setVisibility(View.GONE);
					linearhead1.setVisibility(View.VISIBLE);
					linearhead2.setVisibility(View.VISIBLE);

				}

			}
		});

		cbnopawns.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					linearhead1.setVisibility(View.GONE);
					layoutradios.setVisibility(View.VISIBLE);

					linearhead2.setVisibility(View.VISIBLE);

				} else {

					layoutradios.setVisibility(View.GONE);
					linearhead1.setVisibility(View.VISIBLE);
					linearhead2.setVisibility(View.VISIBLE);

				}
			}
		});

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int code = v.getId();
		switch (code) {
		case R.id.radioButtonSwap:
			if (rbswap.isChecked())
				rbstart.setChecked(false);
			
				

			break;
		case R.id.radioButtonRestart:
			if (rbstart.isChecked())
				rbswap.setChecked(false);

			break;
			
		case R.id.radio1:
			Log.d("radio1","radio1");
			rb2.setChecked(false);
			rb3.setChecked(false);
			rb4.setChecked(false);
			rb5.setChecked(false);
			break;
		
		case R.id.radio2:
			Log.d("radio2","radio2");	
			rb1.setChecked(false);
			rb3.setChecked(false);
			rb4.setChecked(false);
			rb5.setChecked(false);
			break;	

		case R.id.radio3:
			rb2.setChecked(false);
			rb1.setChecked(false);
			rb4.setChecked(false);
			rb5.setChecked(false);
			break;
			
		case R.id.radio4:
			rb2.setChecked(false);
			rb3.setChecked(false);
			rb1.setChecked(false);
			rb5.setChecked(false);
			break;
			
		case R.id.radio5:
			rb2.setChecked(false);
			rb3.setChecked(false);
			rb4.setChecked(false);
			rb1.setChecked(false);
			break;		

		default:
			break;
		}

	}
}
