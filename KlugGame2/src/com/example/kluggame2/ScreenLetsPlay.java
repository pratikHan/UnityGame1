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
public class ScreenLetsPlay extends Fragment {

	// ImageButton left,right;
	MediaPlayer mp, mp1;
	MainActivity act;
	public ScreenLetsPlay() {
		// TODO Auto-generated constructor stub
	}
	public ScreenLetsPlay(MainActivity _act){
		act=_act;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen5, null);

		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		ImageButton right = (ImageButton) view.findViewById(R.id.imageButton2);

		Log.d("Screen 5", "Screen5");
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Right Clicked",
				// Toast.LENGTH_SHORT).show();
				
			//	mp = MediaPlayer.create(getActivity(), R.raw.tappercrussive);
			//	mp.start();

			//	((MainActivity) getActivity()).sound.quit();
			
				((MainActivity) getActivity()).fragmentReplace(51);
			}
		});

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Left Clicked",
				// Toast.LENGTH_SHORT).show();
			
			//	mp1 = MediaPlayer.create(getActivity(), R.raw.tapcrisp);
			//	mp1.start();

				//Screen4 hello = new Screen4();
				((MainActivity) getActivity()).fragmentReplace(4);
				
			}
		});

		return view;
	}

}
