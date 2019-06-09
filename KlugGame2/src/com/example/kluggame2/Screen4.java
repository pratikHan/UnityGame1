package com.example.kluggame2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Owner on 24-Jun-15.
 */
public class Screen4 extends Fragment {

	// ImageButton left,right;
	MediaPlayer mp, mp1;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen4, null);

		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		ImageButton right = (ImageButton) view.findViewById(R.id.imageButton2);
		Log.d("Screen 4", "Screen4");

		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Right Clicked",
				// Toast.LENGTH_SHORT).show();
				
				//mp = MediaPlayer.create(getActivity(), R.raw.tappercrussive);
				//mp.start();
				Log.d("ONclickk", "click");
				
				((MainActivity) getActivity()).fragmentReplace(5);
				

			}
		});

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Left Clicked",
				// Toast.LENGTH_SHORT).show();
				
				//mp1 = MediaPlayer.create(getActivity(), R.raw.tapcrisp);
				//mp1.start();

			//	Screen3 hello = new Screen3();
			
				((MainActivity) getActivity()).fragmentReplace(3);
			}
		});

		return view;
	}
}
