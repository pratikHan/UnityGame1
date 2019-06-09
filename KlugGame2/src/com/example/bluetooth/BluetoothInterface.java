package com.example.bluetooth;



import java.util.*;

import com.example.kluggame2.MainActivity;

import android.app.*;
import android.graphics.Color;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.OnKeyListener;




public class BluetoothInterface 
{
	
	public static int bluetooth=1;
	
	public static  class Log
	{
		public static void e(String a,String b)
		{
			android.util.Log.e(a,b);
		}
	}
	
	int ix=0;
    public class Item
    {
    	ArrayList<String> a1 = new ArrayList<String>();
    	
    	int current;
    	
    	public void add(int steps,String command)
    	{
    		for(int i=0;i<steps;i++)
    		{
    			a1.add(command);
    		}
    	}
    	
    	public ArrayList<String> Generate(ArrayList<String> commands)
    	{
    		a1.clear();
    		String operator="";
    		for(int i=0;i<commands.size();i++)
    		{
    			if(i==0)
    			{
    				int steps=Integer.parseInt(commands.get(i));
    				add(steps,"front");
    				//return null;
    				continue;
    			}
    			if(i%2!=0)
    			{
    				operator=commands.get(i);
    			}
    			else
    			{
    				if(operator.equals("+"))
    				{
    					int steps=Integer.parseInt(commands.get(i));
    					add(steps,"front");
    				}
    				if(operator.equals("-"))
    				{
    					int steps=Integer.parseInt(commands.get(i));
    					add(steps,"back");
    				}
    				if(operator.equals("*"))
    				{
    					int steps=Integer.parseInt(commands.get(i));
    					add(steps,"right");
    				}
    				if(operator.equals("/"))
    				{
    					int steps=Integer.parseInt(commands.get(i));
    					add(steps,"left");
    				}
    			}
    			
    			
    			
    		}
    		return a1;
    		
    	}
    	
    	Item()
    	{
    	//      Log.e("ZZZ","answert id "+map.compute(a1));
		      current=0;
    	}
    	
    	
    };
	
    
    
	private static String TAG;
    
    
    
        
    PhotoDecodeRunnable r1;
    ROVState state;
    Thread t1;
    Handler textViewUpdaterHandler;
    
    static {
        BluetoothInterface.TAG = "kp";
    }
    
    MainActivity act;
    public BluetoothInterface(MainActivity _act) {
    	act=_act;
        this.textViewUpdaterHandler = new Handler(Looper.getMainLooper());
        this.r1 = new PhotoDecodeRunnable();
    }
    
    
    
    
    
    int button_mode=0;
    
    
    public MainActivity getActivity()
    {
    	return act;
    }
    public void onCreateView(final String name)
    {                   
        if(bluetooth==1)
        {        
        	
        	textViewUpdaterHandler.post(new Runnable() {
        	    public void run() {
        	        while (true) {
        	            try {
        	                (state = new ROVState()).startCommunication(getActivity(),name);
        	                new Handler();
        	                new Thread(new ConnectionStatus(getActivity())).start();
        	                ///Log.e("ZZ", "returning root view");
        	                break;
        	                
        	            }
        	            catch (Exception ex) {
        	                ex.printStackTrace();
        	                
        	            }
        	            
        	        }

        	    }
        	});
        	
        }
        else
        {
        	act.BluetoothCallback(0);
        }
    
    }
    
    
    
    
    public void onDestroy() {
    	if(bluetooth==1)
    	{
        this.state.stop();
        this.r1.runflag=false;
        try {
        if (this.t1 != null && this.t1.isAlive()) {
            this.t1.join();
        }        

        }
    	
        
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    	}
        //Log.e("BB","completed destroying all threads");
    }
    
    public void play(ArrayList<String> equation) {
        try {
        	
            if (this.r1 != null) {
            	
                this.r1.runflag = false;
            }
            if (this.t1 != null && this.t1.isAlive()) {
                this.t1.join();
            }
            r1.commands=equation;
            this.r1.runflag = true;
            (this.t1 = new Thread(this.r1)).start();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public class ConnectionStatus implements Runnable
    {
        MainActivity a;
        int scount;
        int mode=2;
        public ConnectionStatus(final MainActivity a) {
            this.scount = 0;
            this.a = a;
        }
        
        @Override
        public void run() {
            if (state != null && state.comm != null) {
                while (true) {          
                            try {
                                Thread.sleep(1000L);
                             	if(r1!=null && r1.runflag==false)
                             		break;
                             	
                             	//Log.e("ZZ", "klug connected "+state.comm.comm_flag+":"+state.comm.cstatus+":"+mode);
                                    if (state.comm.comm_flag==true && state.comm.cstatus==true) {
    //                                   
                                        this.scount = 0;
                                        //im2.setMode(0);
                                        if(mode!=0)
                                        {
                                        	//Log.e("ZZ", "klug connected");
                                        	act.BluetoothCallback(0);
                                        	state.comm.cstatus=true;
                                        	break;
                                        }
                                        mode=0;
                                        //break;
                                    }
                                    else
                                    {
                                    try {
                                        //state.startCommunication(this.a);
                                        //state.setStatus();
                                        //if (state.getStatus()) {
                                        //    break ;
                                        //}
      //                                  Log.i("ZZ", "klug not connected " + this.scount);
                                        ++this.scount;
                                        if (this.scount > 10 && scount<=20) {
                                        	if(mode!=1)
                                        	{
                                        		//Log.e("ZZ", "klug n connected");
                                        		act.BluetoothCallback(1);
                                        	//attempting to connect
                                            //im2.setMode(1);
                                            //textViewUpdaterHandler.post((Runnable)im2);
                                        	}
                                            continue;
                                        }
                                        if (this.scount > 20) {
                                        	if(mode!=1)
                                        	{
                                        		//Log.e("ZZ", "klug n connected 2");
                                        		act.BluetoothCallback(2);
                                        		mode=1;
                                        	//failed to connect
                                            //im2.setMode(2);
                                            //textViewUpdaterHandler.post((Runnable)im2);
                                        	}
                                        	break;
                                        }
                                        
                                        state.comm.cstatus=false;
                                    }
                                    catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    }
                                }                            
                            catch (InterruptedException ex3) {}
                        
                        
                	} 
                             
            }
        }
    }
    
    
    public class PhotoDecodeRunnable implements Runnable
    {
        public boolean runflag;
        int wcount;
        ArrayList<String> commands;
        public PhotoDecodeRunnable() {
            this.runflag = true;
            this.wcount = 0;
        }
             
        @Override
        public void run() {
        		Looper.prepare();
        		Item puzzles=new Item();
        		puzzles.Generate(commands);
        		for(int i=0;i<puzzles.a1.size();i++)
        		{
        			puzzles.current=i;
        			this.wcount = 0;
               
                
                String command="";
                
                //if(k!=-1)
                //{
                command=(String)puzzles.a1.get(puzzles.current);
                //}
                
                float value=0;
                                
                if(command.equals("front") || command.equals("back") )
                {
                	//front and back
                	value=5;
                }
                else if(command.equals("left")||command.equals("right"))
                {
                	//rotate 90 degrees
                	value=1;
                }
                
                
                Log.e("ZZ","sending command "+command+":"+value);
                //this.runflag=true;
                if(bluetooth==1)
                {
                //play music
                
                state.sendCommand(command, (int)value);                
                state.comm.next_flag = false;
                }
                //else
                //{
                	
               // }
                
                Log.e("ZZ","completed sending command "+command+":"+value);
                //puzzles.current=puzzles.current+1;
                this.wcount = 0;
                try {
                //Thread.sleep(1000L);
                if(bluetooth==1)
        		{
                int count=0;
                act.BotMusic();
                while (!state.comm.next_flag) {                 
                	{
                    
                				//if(count>=60)
                				//	break;
                				
                            	Thread.sleep(100L);
                            	count++;
                                if (!this.runflag) {
                                  //  Log.e("ZZ", "exiting current loop");
                                    break;
                                }///
                                
                            }
                            //Log.e("OO", "waiting for motion");
                                                                                                  
                    }
                }
                else
                {
                	Log.e("OO", "waiting for motion,playing music");
                	act.BotMusic();
                	Thread.sleep(2000L);
                }
                }
                catch (Exception ex2) {
                    ex2.printStackTrace();
         
                
                //Log.e("OO", "completed motion");
	                if (!this.runflag) {
	                    Log.e("ZZ", "exiting final current loop");
	                    return;
	                }
                   
        		}
                
        		}
                //Log.e("ZZ","completed command");
        		//completed motion send back command
            	//int ans=puzzles.compute();
            	
            	//Log.e("ZZZ","answer is "+ans);
            	puzzles.current=0;
            	act.BluetoothCallback1(); 

        //    }
        
        }
    }
}