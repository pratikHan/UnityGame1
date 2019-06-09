package com.example.kluggame2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bluetooth.BluetoothInterface;
import com.example.kluggame2.AndroidUnityInterface.PlayerData;

import com.example.utils.Timerx;
import com.example.wifi.KlugCallback;
import com.example.wifi.WifiApControl;
import com.example.wifi.WifiScanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Owner on 24-Jun-15.
 */
public class ScreenCreateGame extends ListFragment implements
		Timerx.TimerInterface, KlugCallback {

	public static String TAG = "ScreenCreateGame->";
	// ListView l1;
	CustomListAdapter adapter;
	PlayerModel pm = new PlayerModel();
	List<PlayerModel> modelItems;
	Context context;
	WifiScanner scanner;
	String GameName;
	ImageButton right;

	AndroidUnityInterface uinterface;
	MainActivity act;

	public ScreenCreateGame() {
	}

	public ScreenCreateGame(String name, MainActivity _act) {
		GameName = name;
		uinterface = AndroidUnityInterface.getInstance();

		uinterface.creategame = this;
		act = _act;
	}

	public void start() {
		Log.e(TAG, "Starting game " + uinterface.state.state + ":"
				+ uinterface.wait_turn);
		timer.stop();
		if (uinterface.state.state == uinterface.wait_turn
				|| uinterface.state.state == uinterface.detective
				|| uinterface.state.state == uinterface.master) {

			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (timer != null)
						timer.stop();
					if (timer != null)
						utimer.stop();
					// TODO Auto-generated method stub
					Log.e(TAG, "calling start game ");
					((MainActivity) act).fragmentReplace(10, "");

				}

			});

			/*
			 * act.runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * });
			 */

		}
	}

	public void StartGame(int id) {

	}

	void checkGameStatus() {
		Log.e(TAG,
				"timer is completed 1 checking status "
						+ apControl.CheckStatus());
		if (apControl != null && apControl.CheckStatus()
				&& create_game_flag == true) {
			Log.e(TAG, "timer is End " + apControl.CheckStatus());
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					createGameDialog(false);

				}

			});

			// success ,starting the server
			// act.runOnUiThread(new PDialog(3));
			Log.e(TAG, "Sending message to unity ");
			uinterface.sendUnityMessage(AndroidUnityInterface.StartServer, "");
			if (timer != null)
				timer.stop();
			if (utimer != null)
				utimer.stop();
			utimer.start();

		} else {
			Log.e("ZZ", "creating error dialogs");
			timer.stop();
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					createGameDialog(false);
					errorCreateGame(true);
					if (timer != null)
						timer.stop();
					if (utimer != null)
						utimer.stop();

				}

			});

		}

	}

	public void End(int id) {

		switch (id) {

		case 0:
			checkGameStatus();
			break;
		}

	}

	public void Cancel(int id) {/*
								 * switch(id) {
								 * 
								 * case 0: if(apControl!=null) {
								 * apControl.setWifiAPDisabled(); }
								 * FragmentManager fragmentManager =
								 * getFragmentManager(); FragmentTransaction
								 * fragmentTransaction = fragmentManager
								 * .beginTransaction(); Screen7 hello = new
								 * Screen7(); fragmentTransaction.replace(R.id.
								 * fragment_container, hello);
								 * fragmentTransaction.commit(); break; }
								 */
	}

	boolean create_game_flag = false;

	// ProgressDialog pDialog;
	public void Complete(int id) {
		Log.e(TAG, "timer is completed " + id);
		switch (id) {

		case 0:
			checkGameStatus();
			break;
		}
	}

	Timerx timer, utimer;
	WifiApControl apControl;

	// ImageButton left,right,guide,settings,create;

	public void showStartingGameDialog(boolean status) {

		if (status == false) {
			act.KlugDialogs(act.StartGame, 1);
		} else {
			act.KlugDialogs(act.StartGame, 0);
		}
		// lay1.invalidate();

	}

	public void errorCreateGame(boolean status) {

		if (status == true) {
			act.KlugDialogs(act.ErrorCreatingGame, 0);
		} else {
			act.KlugDialogs(act.ErrorCreatingGame, 1);
		}
		// lay2.invalidate();
		return;
	}

	public void createGameDialog(boolean status) {
		Log.e("ZZ", "calling createGameDialog" + status);
		if (status == true) {

		} else {
			act.KlugDialogs(act.CreatingGame, 1);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen_create_game,
				null, false);
		TextView text = (TextView) view.findViewById(R.id.textView1);

		TextView et = (TextView) view.findViewById(R.id.edittext1);
		// Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
		// "fonts/beon.ttf");
		// et.setTypeface(face);

		// text.setTypeface(face);
		text.setText("Add Players");

		et.setText(GameName);
		et.setGravity(Gravity.CENTER);

		// CustomListAdapter adapter=new CustomListAdapter(getActivity(),
		// playerName);
		// l1=(ListView) view.findViewById(R.id.listview1);

		timer = new Timerx(1000, 20000, 4000, this, 0);
		utimer = new Timerx(1000, 200000, 4000, this, 1);
		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		right = (ImageButton) view.findViewById(R.id.imageButton2);

		Log.e("Screen Join Game", "ScreenJoinGame");
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				List<String> clist = adapter.getCheckedList();
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();

				String c = gson.toJson(clist);
				Log.e(TAG, "sending sting " + c);
				uinterface.sendUnityMessage(AndroidUnityInterface.JoinList, c);
				utimer.stop();
				uinterface.state.state=uinterface.wait_turn;
				act.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.e(TAG,"showStartingGameDialog");
						showStartingGameDialog(true);				
					}
					
					
				});

				// StartGame hello=new StartGame();
				((MainActivity) getActivity()).fragmentReplace(10, "");
				

			}
		});

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Left Clicked",
				// Toast.LENGTH_SHORT).show();
				timer.stop();
				utimer.stop();
				Screen7 hello = new Screen7();
				((MainActivity) getActivity()).fragmentReplace(7);
			}
		});

		if (MainActivity.gdata != null)
			MainActivity.gdata.gamename = MainActivity.TAG + ":";// +GameName;

		utimer.start();
		uinterface.creategame = this;

		uinterface.state.state = uinterface.idle;
		context = act;
		AttemptStart();
		Log.e("VV", "displaying dialogs 2 " + System.currentTimeMillis());

		return view;
	}

	public void AttemptStart() {
		createGameDialog(true);
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		apControl = WifiApControl.getApControl(wifiManager, act);
		if (apControl != null) {

			Log.e(TAG, "Strting game " + GameName);

			if (apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),
					true, GameName)) {
				Log.e("ZZ", "error in creating game " + GameName);
				errorCreateGame(true);

			} else {
				timer.start();
			}

			new Handler().post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// et.getText();
					if (act.binterface != null) {
						act.binterface.onDestroy();
					}
					// GameName="HC-05";
					act.binterface = new BluetoothInterface(act);
					act.binterface.onCreateView("klug01");
				}

			});
		} else {
			errorCreateGame(true);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		modelItems = new ArrayList<PlayerModel>();
		// modelItems.add(new PlayerModel("pratik", 0)) ;
		// modelItems.add(new PlayerModel("Ansh", 0)) ;
		// modelItems.add(new PlayerModel("Sneh", 0)) ;
		// modelItems.add(new PlayerModel("Prashant", 0)) ;
		// modelItems.add(new PlayerModel("chintan", 0)) ;

		adapter = new CustomListAdapter(getActivity(), modelItems, this);
		setListAdapter(adapter);

		this.changebuttonstate();

		// l1.setAdapter(adapter);
		/*
		 * getListView().setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * pos, long arg3) { // TODO Auto-generated method stub
		 * 
		 * Object obj = getListView().getAdapter().getItem(pos); String
		 * value=obj.toString();
		 * 
		 * Log.d("List items", "List items"); //Toast.makeText(getActivity(),
		 * ""+pm.getName().toString(),Toast.LENGTH_SHORT).show();
		 * Log.d("item is", ""+getListView().getItemIdAtPosition(pos));
		 * 
		 * 
		 * Log.d("item is", ""+value); Log.d("Model Items name",
		 * ""+adapter.rowItem.get(pos).getName()); Log.d("Model Items value",
		 * ""+adapter.rowItem.get(pos).getValue());
		 * 
		 * 
		 * Log.d("Model Items name", ""+modelItems.get(pos).getName());
		 * Log.d("Model Items value1", ""+modelItems.get(pos).getValue());
		 * 
		 * String _playername=modelItems.get(pos).getName(); int
		 * _value=modelItems.get(pos).getValue();
		 * 
		 * } });
		 */

	}

	public void checkAvailablePlayers() {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiApControl apControl = WifiApControl.getApControl(wifiManager, act);
		if (apControl != null) {

			apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),
					true, GameName);
		}

	}

	PlayerData[] list;

	public void listUpdate() {
		// TODO Auto-generated method stub

		ArrayList<PlayerModel> tmp=new ArrayList<PlayerModel>();
		
		for(int i=0;i <modelItems.size();i++)
		{
			tmp.add(modelItems.get(i));
		}
		
		// TODO Auto-generated method stub
		list=uinterface.getPlayerData();
		Log.e("ZZ->", "Received callback");
		//Log.e("ZZ->", "Received callback + wifi count" + scanner.getWifiCount());
		//if (scanner.getWifiCount() > 0) {

			if (adapter != null) {
				// adapter.clear();
				// products.clear();

				modelItems.clear();
				//String list[] = scanner.getWifiList();
				if(list!=null)
				{
				for (int i = 0; i < list.length; i++) {
					Log.e("ZZ->", "Received callback + updating list "+ list[i].playername);
					// products.add(list[i]);
					// adapter.add(list[i]);
					//if(list[i].type!=AndroidUnityInterface.server_type.server)
					if(i==0)
					{
						modelItems.add(new PlayerModel(list[i].playername,1));
					}
					else
					{
					boolean flag=false;
					for(int k=0;k<tmp.size();k++)
					{
						if(list[i].playername.equals(tmp.get(k).name))
						{
							modelItems.add(new PlayerModel(list[i].playername,tmp.get(k).value));
							flag=true;
							break;
						}
					}
					if(flag==false)
					{
						modelItems.add(new PlayerModel(list[i].playername,0));
					}
					}
					
					

				}
				act.runOnUiThread(new Runnable()
				 {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
					}
					 
				 });
				
				}

				
			}

		//}

	
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectStatus(boolean status) {
		// TODO Auto-generated method stub

	}

	public void onClick(int id) {
		// TODO Auto-generated method stub

		if (id == 0) {

			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// createGameDialog(true);
					if (timer != null)
						timer.stop();
					if (utimer != null)
						utimer.stop();

					((MainActivity) getActivity()).fragmentReplace(7);
					errorCreateGame(false);
				}

			});

			// goback to previous screen

		} else if (id == 1) {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					errorCreateGame(false);
					if (timer != null)
						timer.stop();
					if (utimer != null)
						utimer.stop();
					// utimer.stop();

					// timer=new
					// Timerx(1000,20000,4000,ScreenCreateGame.this,0);
					// utimer=new
					// Timerx(1000,200000,4000,ScreenCreateGame.this,1);
					AttemptStart();
					// createGameDialog(true);

				}

			});

			if (timer != null) {
				timer.stop();
			}
			timer = new Timerx(1000, 20000, 4000, this, 0);
			timer.start();
			// create game again
		}

	}

	@Override
	public boolean runTask(int id, int Counter) {

	//***	Log.e(TAG, "runTask " + apControl.CheckStatus());
		switch (id) {

		case 0:
			if ((apControl != null && !apControl.CheckStatus())
					|| create_game_flag == false) {
				Log.e("ZZ", "not success");
				/*
				 * act.runOnUiThread(new Runnable(){
				 * 
				 * @Override public void run() { // TODO Auto-generated method
				 * stub
				 * 
				 * // createGameDialog(false);
				 * 
				 * }
				 * 
				 * });
				 */
				// return true;
				return false;
			} else if (apControl != null && apControl.CheckStatus()
					&& create_game_flag == true) {
				Log.e("ZZ", "Success head to complete");
				return true;
			}

		case 1:
			Log.e(TAG, "counter is " + Counter);
			listUpdate();
			break;
		}
		return false;
	}

	public void changebuttonstate() {

		List<String> clist = adapter.getCheckedList();
		if (clist != null) {
			adapter.counter = clist.size();
		} else {
			adapter.counter = 0;
		}
		Log.e("counter X", "" + adapter.counter + ":"
				+ MainActivity.gdata.max_players);
		if (adapter.counter >= 1
				&& adapter.counter <= MainActivity.gdata.max_players) {
			// Log.d("trueee",""+CustomListAdapter.checkboxisChecked);
			right.setEnabled(true);
			right.getBackground().setColorFilter(null);

			right.setClickable(true);
		} else {
			// Log.d("false",""+CustomListAdapter.checkboxisChecked);
			right.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			right.setEnabled(false);
		}
	}

}
