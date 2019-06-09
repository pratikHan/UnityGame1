package com.example.kluggame2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.bluetooth.BluetoothInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unity3d.player.UnityPlayerNativeActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint.Join;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends UnityPlayerNativeActivity implements FragmentCallback,
	 OnClickListener, KDialogInterface,SharedataInterface {

	public static String TAG = "KlugTek/Chakraviuh";
	public static String sharedata;
	String frag1;

	Animation animation;

	public class GameData {
		String playername;
		String gamename;
		int max_players;
		int level;
		int maxRound;
		int maxNumber;

		public GameData() {
			max_players = 4;
		}
	}

	public static String toJson(GameSettings p) {

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j;
		String l2 = new String();
		try {
			// j = new JSONObject(local);
			l2 = gson.toJson(p, GameSettings.class);

			// for(int i=0;i<l2.length;i++)
			// {
			// / l1.add(l2[i]);
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l2;

	}

	public class GameSettings {
		public String GameName;
		public int max_players;
		int level;
		int maxRound;
		int maxNumber;

	}

	public static void SaveSettings() {
		GameSettings s1 = getSettings1();
		// String
		// settings=SaveExternalStorage.readFile(getSettingsDir()+"/game_settings.json");
		if (s1 != null) {
			// saveext=new SaveExternalStorage();
			// SaveExternalStorage ses=new SaveExternalStorage();

			s1.level = gdata.level;
			s1.maxNumber = gdata.maxNumber;
			SaveExternalStorage.writeFile(MainActivity.toJson(s1),
					getSettingsDir() + "/game_settings.json");
			Log.e("ZZ", "completed writing to file " + s1.level + ":"
					+ s1.maxNumber);
			// String readex=ses.readfromExternalStorage("sonscorecard.txt");
			// String readin=ses.readInternalStorage("sonscorecard.txt");

			// Log.d("READX", readex);
			// Log.d("READIN", readin);

			// Log.d(TAG,""+edName.getText().toString().concat(".txt"));
		}

	}

	public static GameSettings getSettings1() {
		String settings = SaveExternalStorage.readFile(getSettingsDir()
				+ "/game_settings.json");
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j1;
		try {
			j1 = new JSONObject(settings);
			GameSettings s = gson.fromJson(j1.toString(), GameSettings.class);
			return s;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;

		}

	}

	public void getSettings() {
		String settings = SaveExternalStorage.readFile(getSettingsDir()
				+ "/game_settings.json");
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j1;
		try {
			j1 = new JSONObject(settings);
			GameSettings s = gson.fromJson(j1.toString(), GameSettings.class);
			gdata.max_players = s.max_players;
			gdata.maxNumber = s.maxNumber;
			gdata.maxRound = s.maxRound;
			gdata.level = s.level;
			Log.e("ZZZ", "AXAX" + gdata.max_players + ":" + gdata.maxNumber
					+ ":" + gdata.level + ":" + gdata.level);
			TAG = s.GameName;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static GameData gdata;
	public static String ExternDir = "/mnt/sdcard/" + TAG;// Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+TAG;
	BluetoothInterface binterface;
	static AndroidUnityInterface uinterface;
	static ScreenCreateGame screencreategame = null;
	static ScreenJoinGame screenjoingame = null;
	static StartGame startgame = null;

	// static ScoreCard sc=null;

	public static String getmaxNumber() {
		return "" + gdata.maxNumber;
	}

	public static int getMaxRound() {
		if (gdata != null) {
			return gdata.maxRound;
		}
		return 5;
	}

	public static String getLevel() {
		if (gdata != null) {
			return "" + gdata.level;
		}
		return "" + 3;
	}

	public static String getExternDir() {
		ExternDir = "/mnt/sdcard/" + TAG + "/";// Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+TAG;
		return ExternDir;
	}

	public static String getGameName() {
		return gdata.gamename;
	}

	public static String getPlayerName() {
		return gdata.playername;
	}

	public static String getUserDir() {
		return getExternDir() + "Users" + "/";

	}

	public static String getSettingsDir() {
		return getExternDir() + "Settings" + "/";
	}

	public static String getPuzzlesDir() {
		return getExternDir() + "Puzzles" + "/";
	}

	Context context;
	SoundHandler sound;

	 ScreenLetsPlay letsplay;
	 Screen2 s2;
	 Screen3 s3 ;
	 Screen4 s4 ;
	 Screen6 s6 ;
	 Screen7 s7 ;
	 ScreenAboutYourself s51 ;

	private WakeLock wakeLock;
	public int Foulgame = R.layout.layout_foul_screen;
	public int CreatingGame = R.layout.popup_connecting_to_drive;
	public int WaitHost = R.layout.alert_layer;

	public int ErrorCreatingGame = R.layout.popup_error_creating_game;
	public int StartGame = R.layout.popup_startgame;

	public int ErrorJoiningGame = R.layout.popup_error_joining_game;
	public int Disconnected = R.layout.popup_game_disconnected;
	public int ExitGame = R.layout.popup_do_you_want_to_exit;

	public void BluetoothCallback1() {
		Log.e("ZZZZ", "Calling end bot");
		if (startgame != null) {
			// ***** startgame.EndBot();
		}
	}

	public void BluetoothCallback(int status) {
		Log.e("ZZ", "bluetooth is enabled status is " + status);
		if (screencreategame != null) {
			if (status == 0) {
				screencreategame.create_game_flag = true;
			} else if (status == 1) {
				screencreategame.create_game_flag = false;
			} else if (status == 2) {
				screencreategame.create_game_flag = false;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		SoundHandler.setContext(this);
		final long delay = 1000;// ms
		Handler handler = new Handler();

		Runnable runnable = new Runnable() {
			public void run() {
				ViewGroup rootView = (ViewGroup) MainActivity.this
						.findViewById(android.R.id.content);
				rootView.setKeepScreenOn(true);
				// find the first leaf view (i.e. a view without children)
				// the leaf view represents the topmost view in the view stack
				View topMostView = getLeafView(rootView);
				// let's add a sibling to the leaf view
				ViewGroup leafParent = (ViewGroup) topMostView.getParent();
				// Button sampleButton = new Button(MainActivity.this);
				// sampleButton.setText("Press Me");

				View view = getLayoutInflater().inflate(R.layout.activity_main,
						null, false);
				Log.d("Main", "Main Activity starts");
				view.setKeepScreenOn(true);
				SoundHandler.setContext(MainActivity.this);
				// mBackgroundSound = new BackgroundSound();

				leafParent.addView(view, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SplashScreenActivity hello = new SplashScreenActivity();

				fragmentTransaction.add(R.id.fragment_container, hello);
				fragmentTransaction.commit();

				init();
				sound = new SoundHandler();
				 Log.e("ZZ","External Directory is "+MainActivity.ExternDir);
                 gdata=new GameData();

             //*****    getSettings();
			}
		};

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				TAG);
		uinterface = AndroidUnityInterface.getInstance();
		handler.postDelayed(runnable, delay);
		
		// setContentView(R.layout.activity_main);

	}

	private View getLeafView(View view) {
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			for (int i = 0; i < vg.getChildCount(); ++i) {
				View chview = vg.getChildAt(i);
				View result = getLeafView(chview);
				if (result != null)
					return result;
			}
			return null;
		} else {

			Log.e("ZZ", "Found leaf view");
			return view;
		}
	}

	public void init() {
		startgame = new StartGame(this);
		screenjoingame = new ScreenJoinGame(this);
		screencreategame = new ScreenCreateGame("",this);

		s2 = new Screen2();
		s3 = new Screen3();
		s4 = new Screen4();
		letsplay = new ScreenLetsPlay(this);
		s51 = new ScreenAboutYourself();
		s6 = new Screen6();
		s7 = new Screen7(this);

	}

	

	

	//
	// @Override
	// public void fragmentReplace(Fragment fragment) {
	// // TODO Auto-generated method stub
	// Log.d("REPLACED", "FRAGMENT");
	// FragmentManager fragmentManager = getFragmentManager();
	// FragmentTransaction fragmentTransaction = fragmentManager
	// .beginTransaction();
	//
	// fragmentTransaction.replace(R.id.fragment_container, fragment);
	// fragmentTransaction.commit();
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

    	// TODO Auto-generated method stub
    	Log.e("ZZ","onClick "+v.getId());
    	if(v.getTag()==null)
    	{
    		//***		if(v.getId()==R.layout.hint_dialog)
    		{
    			//***		KlugDialogs(HintDialog,1);
    			return;
    		}
    		/*if(v.getId()==R.layout.alert_layer) // need to add host waiting dialog
    		{
    			KlugDialogs(WaitHost,1);
    		}*/
    		    		
    	//	return;
    	}
    	String tag=(String)v.getTag();
    	if((((String)(v.getTag())).equals("puzzlenotwonbtnOk")))
    	{
    		//***		KlugDialogs(PuzzleNotWon,1);
    	}
    	else if((((String)(v.getTag())).equals("wonpuzzlebtn")))
    	{
    		//***		KlugDialogs(PuzzleWon,1);
    		if(startgame!=null)
    		{
    		//***	startgame.detectiveAnswering(false);
    			//***	startgame.footerDisplay(true);//15* screen to be designed
    		}
    	}
    	else if(tag.equals("createGamebtnleave"))
    	{
    			if(screencreategame!=null)
    			{
    				screencreategame.createGameDialog(false);
    			}
    	}
    	else
    	{
			if(startgame!=null)
			{
				startgame.onClick(v);
			}
    		
    	}
    
	}

	@Override
	public ArrayList<String> getDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHints() {
		// TODO Auto-generated method stub
		return 0;
	}

	public class KDialog extends Dialog implements
			android.view.View.OnClickListener {

		RadioButton rbswap, rbstart, rb1, rb2, rb3, rb4, rb5;

		int theme = 0;
		KDialogInterface call;
		View child;
		LinearLayout myLinearLayout;
		RelativeLayout mainLayout;

		public KDialog(Context context, int _theme, KDialogInterface callback) {
			super(context, R.style.FullHeightDialog);
			theme = _theme;
			call = callback;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			Log.e(TAG, "Creating content");
			child = getLayoutInflater().inflate(theme, null);

			setContentView(child);
			WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
			params1.copyFrom(getWindow().getAttributes());
			// Log.e("ZZ","windows size is"+displaymetrics.widthPixels+":"+displaymetrics.heightPixels+":"+lp.width+":"+lp.height);
			params1.width = width;
			params1.height = height;

			getWindow().setAttributes(params1);

			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

			setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					// Clear the not focusable flag from the window
					// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

					// Update the WindowManager with the new attributes (no
					// nicer way I know of to do this)..
					// WindowManager wm = (WindowManager)
					// MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
					// wm.updateViewLayout(getWindow().getDecorView(),getWindow().getAttributes());
					// /getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				}
			});
			setCancelable(false);
			// this.setOnKeyListener(onKeyListener)

			// this.findViewById(R.id.creatingGame).getLayoutParams().width=LayoutParams.MATCH_PARENT;
			// this.findViewById(R.id.creatingGame).getLayoutParams().height=LayoutParams.MATCH_PARENT;
			// getWindow().setLayout(width, height);
			// Log.e("ZZ","windows size is"+displaymetrics.widthPixels+":"+displaymetrics.heightPixels+":"+params1.width+":"+params1.height);
			Log.e("VV", "displaying dialogs 1 " + System.currentTimeMillis());

			final RelativeLayout layoutcheckboxes = (RelativeLayout) child
					.findViewById(R.id.layout1);
			final RelativeLayout layoutradios = (RelativeLayout) child
					.findViewById(R.id.layout2);
			final LinearLayout linearhead1 = (LinearLayout) child
					.findViewById(R.id.layoutA);
			final LinearLayout linearhead2 = (LinearLayout) child
					.findViewById(R.id.layoutB);
			final RadioGroup radiogroup = (RadioGroup) child
					.findViewById(R.id.radioGroup1);
			rbswap = (RadioButton) child.findViewById(R.id.radioButtonSwap);
			rbstart = (RadioButton) child.findViewById(R.id.radioButtonRestart);
			rb1 = (RadioButton) child.findViewById(R.id.radio1);
			rb2 = (RadioButton) child.findViewById(R.id.radio2);
			rb3 = (RadioButton) child.findViewById(R.id.radio3);
			rb4 = (RadioButton) child.findViewById(R.id.radio4);
			rb5 = (RadioButton) child.findViewById(R.id.radio5);

			CheckBox cbcheckwrong = (CheckBox) child
					.findViewById(R.id.checkBox1);
			CheckBox cbnopawns = (CheckBox) child.findViewById(R.id.checkBox2);

			
			if(rbswap!=null && rbstart!=null && rb1!=null && rb2!=null && rb3!=null && rb4!=null && rb5!=null){
			rbswap.setOnClickListener(this);
			rbstart.setOnClickListener(this);
			rb1.setOnClickListener(this);
			rb2.setOnClickListener(this);
			rb3.setOnClickListener(this);
			rb4.setOnClickListener(this);
			rb5.setOnClickListener(this);
			}

			/*
			radiogroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub

							Log.d("Onchckedchange", "CheckedId :" + checkedId
									+ " Group :  " + group);
							switch (checkedId) {
							case R.id.radio1:
								Log.d("radio1", "radio1");
								rb2.setChecked(false);
								rb3.setChecked(false);
								rb4.setChecked(false);
								rb5.setChecked(false);
								break;

							case R.id.radio2:
								Log.d("radio2", "radio2");
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
					}); */

			if(cbcheckwrong!=null){
			cbcheckwrong
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
		}

			if(cbnopawns!=null){
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
		}
			ImageButton back = (ImageButton) child
					.findViewById(R.id.imageButtonBack);
			if (back != null) {
				Log.d("ZZ", "ONclickofpopupinstructions");
				back.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});

			}

			
			
//
//			Button back1=(Button)child.findViewById(R.id.btnBack);
//		   if(back!=null){
//			   Log.d("ZZ", "ONclickofpopupinstructions");
//			   back.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					dismiss();
//				}
//			});
			   
//		   }
		   Button b=(Button)child.findViewById(R.id.cancelbtn);
			if(b!=null)
			{
			Log.e("ZZ","SETTING ON CLICK LISTENER");
			b.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(v.getId()==R.id.cancelbtn)
					{
			         //   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

			            //Update the WindowManager with the new attributes (no nicer way I know of to do this)..
			           // WindowManager wm = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
			            //wm.updateViewLayout(getWindow().getDecorView(),getWindow().getAttributes());
				//**		play(1);
						if(call!=null)
						call.leave(theme);
						
						dismiss();
					}
					
				}


				
			});
			
			
			Button b1=(Button)child.findViewById(R.id.retrybtn);
			if(b1!=null)
			{
			Log.e("ZZ","SETTING ON CLICK LISTENER");
			b1.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(v.getId()==R.id.retrybtn)
					{
			            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

			            //Update the WindowManager with the new attributes (no nicer way I know of to do this)..
			            //WindowManager wm = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
			            //wm.updateViewLayout(getWindow().getDecorView(),getWindow().getAttributes());
			//**M			play(2);		
						if(call!=null)
						call.retry(theme);
						
						dismiss();
					}
					
				}


				
			});
			}
			
			}
			else
			{
				Log.e("ZZ","SETTING ON CLICK LISTENER is null");
			}
			
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

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
				Log.d("radio1", "radio1");
				rb2.setChecked(false);
				rb3.setChecked(false);
				rb4.setChecked(false);
				rb5.setChecked(false);
				break;

			case R.id.radio2:
				Log.d("radio2", "radio2");
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

	boolean init = false;
	KDialog dialog = null;
	//ArrayList<String> _sum;
	//int hints;
	int width, height;

	

	public void KlugDialogs(int dialogs, int status) {
		if (init == false) {
			init = true;
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			
			if(dialogs==this.Foulgame){
				width = (int) (displaymetrics.widthPixels);
				height = (int) (displaymetrics.heightPixels);
			}else{
			width = (int) (displaymetrics.widthPixels *0.90);
			height = (int) (displaymetrics.heightPixels *0.90);
			}

		}

		if (status == 0) {
			if (dialog != null) {
				Log.e("ZZ", "calling dialog dismiss");
				if (dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			} else {
				Log.e("ZZ", "KlugDialogs");
			}

		}
		/*
		 * if(dialogs==this.GameWon) { Log.e("ZZ","Creating games");
		 * if(status==0) { dialog=new KDialog(this,R.layout.game_won,null);
		 * dialog.show(); } else {
		 * 
		 * if (dialog!=null && dialog.isShowing()) {
		 * Log.e("ZZ","calling dialog dismiss"); dialog.dismiss(); dialog=null;
		 * }
		 * 
		 * } }
		 */

		if (dialogs == this.Foulgame) {
			Log.e("ZZ", "Foulgame Dialog");
			if (status == 0) {
				dialog = new KDialog(this, Foulgame, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.ExitGame) {
			Log.e("ZZ", "Creating games");
			if (status == 0) {
				dialog = new KDialog(this, ExitGame, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}
		if (dialogs == CreatingGame) {
			Log.e("ZZ", "Creating games");
			if (status == 0) {
				dialog = new KDialog(this, R.layout.popup_connecting_to_drive,
						null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		} else if (dialogs == ErrorCreatingGame) {
			Log.e("ZZ", "ErrorCreating games");
			if (status == 0) {
				dialog = new KDialog(this, R.layout.popup_error_creating_game,
						this);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		} else if (dialogs == ErrorJoiningGame) {
			Log.e("ZZ", "ErrorJoiningGame games " + status);
			if (status == 0) {
				dialog = new KDialog(this, ErrorJoiningGame, this);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		else if (dialogs == StartGame) {
			Log.e("ZZ", "StartGame games");
			if (status == 0) {
				dialog = new KDialog(this, StartGame, null);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		else if (dialogs == WaitHost) {
			Log.e("ZZ", "PuzzleWon games");
			if (status == 0) {
				dialog = new KDialog(this, WaitHost, this);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

	}
	
	




	

@Override
protected void onResume()
{
	Log.e("onResume","onResume");
	super.onResume();
	 wakeLock.acquire();
}


@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	wakeLock.release();
	Log.e("Onpause","Paused");
	//sound.pause();
	
}

@Override
public void  onDestroy()
{
	if(binterface!=null)
	{
		binterface.onDestroy();
	}
	super.onDestroy();
}

@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	Log.e("OnStop","Stop");
	sound.quit();
	
}




@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	
	
	
	Log.e("ZZZZZ","Back Pressed");
	sound.pause();
	KlugDialogs(this.ExitGame,0);
	//super.onBackPressed();
	//onPause();
	
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    //No call for super(). Bug on API Level > 11.
}
@Override
public void leave(int id) {
	// TODO Auto-generated method stub
	//calling screen7 on click met
	if(id==this.WaitHost)
	{
		if(screenjoingame!=null)
		{
			screenjoingame.LeaveGame();
		}
	}
	if(id==ErrorJoiningGame)
	{
		if(screenjoingame!=null)
		{
			//need callback for leave
		}
	}
	
	if(id==ExitGame)
	{
		//close bluetooth and Wifi
		Log.e("EEEEE","closing the activity");
		this.finish();
		
	}
	
	if(id==ErrorCreatingGame)
	{
		if(screencreategame!=null)
		{
			Log.e(TAG,"leaving creating game");
			screencreategame.onClick(0);
		}
		
	}
//	******if(id==GameWon)
//	{
//		
//		Log.e("EEEEE","closing the activity");
//		this.finish();
//		//show game won dialog
//		/*if(screencreategame!=null)
//		{
//			Log.e(TAG,"leaving creating game");
//			screencreategame.onClick(0);
//		}*/
//		
//	}
	
}


@Override
public void retry(int id) {
	// TODO Auto-generated method stub
	if(id==this.ErrorCreatingGame)
	{
		if(screencreategame!=null)
		{
			Log.e(TAG,"retruing attempt to connect");
			screencreategame.onClick(1);
		}
	}
	if(id==ErrorJoiningGame)
	{
		if(screenjoingame!=null)
		{
			//need callback for leave
			screenjoingame.Retry();
		}
	}

	
}


@Override
public void sharedata(String s) {
	// TODO Auto-generated method stub
	
	gdata.playername=s;
	//Log.d("sharedata", sharedata);
}


/*MediaPlayer beep_hightone, beep_plucked, beepbrightpop, button_letsplay,
coolclicker, game_play, setupgame, slide_network, slide_rock,
slidescissors, splash, tap_warm, tap_wooden, tap_crisp,
tappercrussive, toys, walkthrough, letsplay,wrong_answer,right_answer;
*/

public void  BotMusic()
{
	//play1(10);
	if(uinterface!=null)
	{
		uinterface.sendUnityMessage(AndroidUnityInterface.BotMusic1,""+uinterface.local.id);
	}
}

@Override
public void fragmentReplace(int code) {
	// TODO Auto-generated method stub
	Log.d(TAG, "Fragment replace int code");
	FragmentManager fragmentManager = getFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();
	// fragmentTransaction.setCustomAnimations(R.anim.enter_anim,
	// R.anim.exit_anim);
	fragmentTransaction.addToBackStack(null);
	switch (code) {
	// case 1:
	// Log.d("REPLACED", "FRAGMENT");

	// Screen3 hello = new Screen3();
	// fragmentTransaction.replace(R.id.fragment_container,fragment );
	// fragmentTransaction.commit();
	// break;

	case 2:
		// Screen2 s2=new Screen2();

		// Screen3 hello = new Screen3();

		// TESTING to be made s2
	if(s2.isAdded())
	{Log.d("Fragment already added", "XXXXXX");
	}else{
		fragmentTransaction.replace(R.id.fragment_container, s2);
		fragmentTransaction.commit();
	}
		break;

	case 3:

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container, s3);
		fragmentTransaction.commit();
		break;

	case 4:

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container, s4);
		fragmentTransaction.commit();
		break;

	case 5:

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container, letsplay);
		fragmentTransaction.commit();
		break;

	case 51:

		fragmentTransaction.replace(R.id.fragment_container, s51)
				.commitAllowingStateLoss();

		break;

	case 6:

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container, s6);
		fragmentTransaction.commit();
		break;

	case 7:

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container, s7);
		fragmentTransaction.commit();
		break;
		}
}

@Override
public void fragmentReplace(int code, String args) {
	// TODO Auto-generated method stub
	FragmentManager fragmentManager = getFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();

	// setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );

	// fragmentTransaction.setCustomAnimations(R.anim.enter_anim,
	// R.anim.exit_anim);

	fragmentTransaction.addToBackStack(null);
	// transaction.replace(R.id.listFragment, new YourFragment());

	switch (code) {
	
//	case 7:
//		if (s7 == null) {
//			s7 = new Screen7(this);
//		}
//
//		// Screen3 hello = new Screen3();
//		fragmentTransaction
//				.replace(R.id.fragment_container, s7);
//		fragmentTransaction.commit();
//		break;
	
	case 8:
		if (screenjoingame == null) {
			screenjoingame = new ScreenJoinGame(this);
		}

		// Screen3 hello = new Screen3();
		fragmentTransaction
				.replace(R.id.fragment_container, screenjoingame);
		fragmentTransaction.commit();
		break;

	case 9:
		if (screencreategame == null) {
			screencreategame = new ScreenCreateGame(args, this);
		}
		screencreategame.GameName = args;

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container,
				screencreategame);
		fragmentTransaction.commit();
		break;

	case 10:
		if (startgame == null) {
			startgame = new StartGame(this);
		}

		// Screen3 hello = new Screen3();
		fragmentTransaction.replace(R.id.fragment_container, startgame);
		fragmentTransaction.commit();
		break;
	}
	
}
}
