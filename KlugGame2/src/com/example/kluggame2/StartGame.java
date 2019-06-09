package com.example.kluggame2;

import com.example.kluggame2.FragmentAngleUI.GestureListener;
import com.example.kluggame2.FragmentAngleUI.MyDragListener;


import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VerticalSeekBar;

public class StartGame extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	TextView txtsteps, txtangle;
	ImageButton foul, steps, rotate, guide;
	VerticalSeekBar seek_bar;
	RelativeLayout relativecircle,relativecirclebackground,layoutsteps,leftsteps,rightangle;
	ImageView pointer;
	AndroidUnityInterface uinterface;
	MainActivity act;

	private static final double SWIPE_MIN_DISTANCE = 30;
	private static final String TAG = "FragmentAngleUI";
	String stepsdata;
	
	public StartGame(){}
	public StartGame(MainActivity _act) {
		uinterface = AndroidUnityInterface.getInstance();

		act = _act;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_start_game, null);

		relativecircle = (RelativeLayout) view
				.findViewById(R.id.relativecircledrag);
		relativecirclebackground = (RelativeLayout) view
				.findViewById(R.id.relativecircle);
		layoutsteps = (RelativeLayout) view
				.findViewById(R.id.layoutchunksteps);
		leftsteps = (RelativeLayout) view
				.findViewById(R.id.leftsidestepsbackground);
		rightangle = (RelativeLayout) view
				.findViewById(R.id.rightsideanglebackground);
		
		
		pointer = (ImageView) view.findViewById(R.id.pointer);

		foul = (ImageButton) view.findViewById(R.id.imageButtonfoul);
		rotate = (ImageButton) view.findViewById(R.id.imageButtonRotate);
		steps = (ImageButton) view.findViewById(R.id.imageButtonsteps);
		guide = (ImageButton) view.findViewById(R.id.imageButtonGuide4);

		txtsteps = (TextView) view.findViewById(R.id.textsteps);
		txtangle = (TextView) view.findViewById(R.id.textangle);

		seek_bar = (VerticalSeekBar) view.findViewById(R.id.seekBar1);
		seek_bar.setOnSeekBarChangeListener(this);

		txtsteps.setText("" + seek_bar.getProgress());

		relativecircle.setOnDragListener(new MyDragListener());
		pointer.setOnTouchListener(new MyTouchListener());

		rotate.setOnClickListener(this);
		steps.setOnClickListener(this);
		foul.setOnClickListener(this);
		guide.setOnClickListener(this);
		
		

		return view;
	}

	int progressval = 0;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.d("on prog", "" + progress);
		progressval = progress;
		txtsteps.setText("" + (progressval + 1));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Log.d("on stop tracking", "" + progressval);
		txtsteps.setText("" + progressval);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.imageButtonfoul:

			
			act.KlugDialogs(act.Foulgame, 0);
			
			
			break;
		case R.id.imageButtonGuide4:

			break;
		case R.id.imageButtonRotate:

			stepsdata = txtsteps.getText().toString();
			relativecircle.setVisibility(v.VISIBLE);
			relativecirclebackground.setVisibility(v.VISIBLE);
			layoutsteps.setVisibility(v.GONE);
			leftsteps.setBackgroundResource(R.drawable.stepsx);
			rightangle.setBackgroundResource(R.drawable.anglex);
			
			
			break;
		case R.id.imageButtonsteps:
			
			relativecircle.setVisibility(v.GONE);
			relativecirclebackground.setVisibility(v.GONE);
			layoutsteps.setVisibility(v.VISIBLE);
			leftsteps.setBackgroundResource(R.drawable.stepsgame2);
			rightangle.setBackgroundResource(R.drawable.anglegame2);
			
			break;

		default:
			break;
		}

	}

	// private GestureDetector gdt=new GestureDetector(new GestureListener());

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {

			Log.d(TAG, "" + motionEvent.getAction() + "ooo"
					+ motionEvent.ACTION_UP);

			if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

				Log.d(TAG, "GetX" + motionEvent.getX());
				Log.d(TAG, "GetY" + motionEvent.getY());
			}

			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

				Log.d("action down", "xxxxx");

				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.INVISIBLE);

				return true;
			} else {
				return true;
			}
		}

	}

	class MyDragListener implements OnDragListener {

		float latx = 0;
		float laty = 0;

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			// View v1 = (View) event.getLocalState();
			// Log.d(TAG, "Bottom"+v1.getBottom());
			// Log.d(TAG, "Top"+v1.getTop());
			// Log.d(TAG, "Left"+v1.getLeft());
			// Log.d(TAG, "Right"+v1.getRight());

			Log.d("ondrag", "entered");

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				float Xo = event.getX();
				float Yo = event.getY();
				Log.d("Dragstarted", "Xo " + (int) Xo + "Yo " + (int) Yo);
				break;
			case DragEvent.ACTION_DRAG_ENTERED:

				break;
			case DragEvent.ACTION_DRAG_EXITED:

				break;

			case DragEvent.ACTION_DRAG_LOCATION:

				float X2 = event.getX();
				float Y2 = event.getY();
				Log.d("dragLocation", " X2 " + (int) X2 + " Y2 " + (int) Y2);

				latx = X2;
				laty = Y2;

				if ((((latx - 200) * (latx - 200)) + ((laty - 200) * (laty - 200))) <= (200 * 200)) {
					Log.d("INSIDE", "TRUE");
					Log.d("Point",
							""
									+ (((latx + 200) * (latx + 200)) + ((laty + 200) * (laty + 200))));
				} else {
					Log.d("Point",
							""
									+ (((latx + 200) * (latx + 200)) + ((laty + 200) * (laty + 200))));
					Log.d("OUTSIDE", "TRUE");
				}

				Log.d("else", "else");
				View v1 = (View) event.getLocalState();
				v1.setX(X2 - (v1.getWidth() / 2));
				v1.setY(Y2 - (v1.getHeight() / 2));
				v1.setVisibility(v1.VISIBLE);

				break;

			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup

				// View view = (View) event.getLocalState();
				// ViewGroup owner = (ViewGroup) view.getParent();
				// owner.removeView(view);
				// RelativeLayout container = (RelativeLayout) v;
				// container.addView(view);
				// view.setVisibility(View.VISIBLE);

				float X = event.getX();
				float Y = event.getY();

				Log.d("ACTION DROP", "X " + (int) X + "Y " + (int) Y);

				View view = (View) event.getLocalState();

				// if(((X*X)+(Y*Y))<=(165*165) ){
				// // latx=event.getX();
				// // laty=event.getY();
				// // Log.d("ACTION DROP", "X " + (int) X + "Y " + (int) Y);
				//
				view.setX(X - (view.getWidth() / 2));
				view.setY(Y - (view.getHeight() / 2));
				Log.d("LOGCATXXXX", "X " + (int) view.getX() + "Y "
						+ (int) view.getY());
				view.setVisibility(View.VISIBLE);

				// Log.d("drop else", "--------------------------------------");
				// view.setX(latx-(view.getWidth()/2));
				// view.setY(laty-(view.getHeight()/2));
				// view.setVisibility(View.VISIBLE);

				break;
			case DragEvent.ACTION_DRAG_ENDED:

			default:
				break;
			}
			return true;
		}

	}

}
