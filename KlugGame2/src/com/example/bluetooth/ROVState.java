package com.example.bluetooth;

import android.os.*;
import java.nio.*;
import android.widget.*;
import android.util.*;
import android.app.*;
import java.net.*;
import java.io.*;

public class ROVState
{
    public static String back;
    private static int delta;
    public static String error;
    public static String front;
    public static String heading;
    public static String left;
    private static int max_torpedo;
    public static String nitro;
    public static String ping;
    public static String reply;
    public static String right;
    public static String test;
    public CommunicationThread comm;
    int heave;
    int id;
    private Handler mHandler;
    private final Runnable mProgressChecker;
    int surge;
    int torpedo_left;
    int torpedo_right;
    int yaw;
    
    static {
        ROVState.delta = 10;
        ROVState.max_torpedo = 3;
        ROVState.heading = "heading";
        ROVState.front = "front";
        ROVState.back = "back";
        ROVState.left = "left";
        ROVState.right = "right";
        ROVState.nitro = "nitro";
        ROVState.test = "test";
        ROVState.reply = "reply";
        ROVState.ping = "ping";
        ROVState.error = "error";
    }
    
    public ROVState() {
        this.mHandler = new Handler();
        this.torpedo_left = ROVState.max_torpedo;
        this.torpedo_right = ROVState.max_torpedo;
        this.mProgressChecker = new Runnable() {
            @Override
            public void run() {
                ROVState.this.sendUpdates();
                if (ROVState.this.mHandler != null) {
                    ROVState.this.mHandler.postDelayed(ROVState.this.mProgressChecker,500L);
                }
            }
        };
        this.id = 0;
    }
    
    public void On() {
        this.comm.library.On();
    }
    
    public void connect(final String s) {
        this.comm.library.connect(s);
    }
    
    byte[] createMotionCommand(final String s, final int n, final int n2) {
        final ByteBuffer allocate = ByteBuffer.allocate(6);
        
        allocate.put((byte)('<'));
        if (s.equals(ROVState.front)) {
            allocate.put((byte)1);
        }
        if (s.equals(ROVState.right)) {
            allocate.put((byte)4);
        }
        if (s.equals(ROVState.back)) {
            allocate.put((byte)2);
        }
        if (s.equals(ROVState.left)) {
            allocate.put((byte)3);
        }
        if (s.equals(ROVState.nitro)) {
            allocate.put((byte)5);
        }
        if (s.equals(ROVState.ping)) {
            allocate.put((byte)0);
        }
        //value
        allocate.put((byte)n);
        //id
        allocate.putShort((short)n2);
        //
        allocate.put((byte)('>'));
        //reset
        allocate.position(0);
        return allocate.array();
    }
    
    public ArrayAdapter<String> getList() {
        return this.comm.library.getList();
    }
    
    public boolean getStatus() {
        final boolean cstatus = this.comm.cstatus;
        this.setStatus();
        return cstatus;
    }
    
    public boolean isOn() {
        return this.comm.library.isOn();
    }
    
    public void play() {
        this.comm.start();
    }
    
    public void sendCommand(final String s, final int n) {
        byte[] array = null;
        int n2 = 0;
        int n3 = 0;
        
            if (s == ROVState.front || s == ROVState.back || s == ROVState.left || s == ROVState.right) {
                //final String nitro = ROVState.nitro;
                array = this.createMotionCommand(s, n, this.id);
                n2 = this.id;
                ++this.id;
                n3 = 6;
        }
        if (s == ROVState.ping) {
            array = this.createMotionCommand(s, -1, this.id);
            n2 = this.id;
            ++this.id;
            n3 = 6;
        }
        if (this.id == 65535) {
            this.id = 0;
        }
        //Log.i("Command ", String.valueOf(s) + ":" + array);
        //len
        this.comm.addWork(s, array, n3, n2);
    }
    
    public void sendUpdates() {
    	
        this.sendCommand(ROVState.ping, -1);
    }
    
    public void setStatus() {
        this.comm.setStatus();
    }
    
    public void startCommunication(final Activity activity,String name) throws UnknownHostException, IOException {
        this.comm = new CommunicationThread(activity,name);        
        this.On();
        
        this.comm.library.connect(name);
        Log.e("GG","completed with connect");
        this.play();
        Log.e("GG","completed starting the thread");
        this.mHandler.post(this.mProgressChecker);
        
    }
    
    public void stop() {
        this.mHandler = null;
        this.comm.library.stop();
        if (!this.comm.isAlive()) {
            return;
        }
        try {
            this.comm.join();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
