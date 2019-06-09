package com.example.bluetooth;

import android.app.*;
import java.util.concurrent.*;
import java.net.*;
import android.util.*;
import java.nio.*;
import java.io.*;
import android.bluetooth.*;
import android.content.*;
import android.widget.*;
import java.util.*;

public class CommunicationThread extends Thread
{
    private static final String SHUTDOWN_REQ = "SHUTDOWN";
    private static final UUID SPP_UUID;
    private BlockingQueue<Message> Queue;
    private BlockingQueue<Message> ReplyQueue;
    public boolean cstatus;
    Message current;
    Boolean isOpen;
    Boolean isTerminated;
    BluetoothLibrary library;
    public Boolean next_flag;
    Message reply;
    int scount;
    Boolean shuttingDown;
    public boolean comm_flag=false;
    static {
        SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
    String name="";
    public CommunicationThread(final Activity activity,String _name) throws UnknownHostException, IOException {
        this.Queue = new ArrayBlockingQueue<Message>(100);
        this.ReplyQueue = new ArrayBlockingQueue<Message>(100);
        this.isTerminated = false;
        this.shuttingDown = false;
        this.isOpen = false;
        this.next_flag = false;
        this.cstatus = false;
        this.scount = 0;
        name=_name;
        this.library = new BluetoothLibrary(activity);
    }
    
    public void addWork(final String s, final byte[] array, final int n, final int n2) {
        if (this.shuttingDown || this.isTerminated) {
            //Log.w("XX", "thread has shutdown or terminated"); 
            return;
        }
        try {
            if (s == ROVState.front || s == ROVState.back || s == ROVState.left || s == ROVState.right) {
            	//array -length - id            	
                current = new Message(s, array, n, n2);
                //Log.e("GG1","current id is "+n2);
            }
            if(library!=null && library.isOn())
            {
            this.Queue.put(new Message(s, array, n, n2));
            //Log.i("XX", "added work-" + this.Queue.size());
            }
        }
        catch (InterruptedException ex) {
            Log.w("EE", "error");
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unexpected interruption");
        }
    }
    
    public Boolean parseResponse(final byte[] array, final Message message) {
        //Log.w("RR", " reveuved bytes are " + array[0] + ":" + array[1] + ":" + array[2] + ":" + array[3] + ":" + array[4]);
        final ByteBuffer allocate = ByteBuffer.allocate(array.length);
        //Log.i("parese", "length of messsage is " + array.length);
        allocate.put(array, 0, array.length);
        //Log.i("parese", "length of buffer is " + allocate.capacity() + ":" + allocate.position());
        allocate.position(0);
        //header
        final byte value = allocate.get();
        //Log.w("GG", "rsss " + value + ":" + -86 + ":" + 170);
        if (value != '<') {
        	this.reply=null;
            return false;
        }
        //header-1value2-
        
        //header --- length --- id
        this.reply = new Message("", null, allocate.getShort(), allocate.getShort());
        return true;
    }
    
    @Override
    public void run() {
        final boolean booleanValue = this.isOpen;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int n = 0;
        
        if (booleanValue==false) {
            this.isOpen = true;
        }
        
        while (true) {
        	if (shuttingDown==true) {
        		break;
        	}
            while (library.BA.enable()==true) {
            	if (shuttingDown==true) {
            		break;
            	}
                                    	
            	Message message = null;
                try {
            	message = this.Queue.take();
                if (message.type == "SHUTDOWN") {
                    break;
                }
                if (this.library == null) {
                    Log.e("GG", "linbrary is null");
                }
                if (this.library.mSocket == null) {
                    Log.e("GG", "linbrary socket is null");
                    //comm_flag=false;
                    n=0;
                }
                else
                {
                	//Log.e("ZZ","socket is not null");
                }
                /*if(comm_flag==false)
                {
                	Log.e("ZZ","library is not connected ");
                }*/
                //(n == 0 || this.library.mSocket != null) && this.library.mSocket.isConnected()
                //Log.e("ZZ","FFF "+n+":"+comm_flag);
                if (n==0 || comm_flag==false) {
                	if(comm_flag==false)
                	{
                		Log.e("ZZ","library is not connected ");
                	}
                	else
                	{
                		comm_flag=false;
                	}
                	
                	//Log.w("GG", "linbrary socket reconnecing");
                    //Thread.sleep(1000L);
                    if (outputStream != null) {
                        inputStream.close();
                        outputStream.close();
                    }
                    
                    if (this.library.mSocket != null) {
                    	Log.w("GG", "linbrary closing socket");
                        this.library.mSocket.close();
                        Thread.sleep(1000);
                    }
                    
                    boolean c=this.library.connect(name);
                    //
                    if(c==true && this.library.mSocket!=null)
                    {
                    inputStream = this.library.mSocket.getInputStream();
                    outputStream = this.library.mSocket.getOutputStream();
                    }
                    n = 1;
                    if(c==false)
                    continue;
                }

                //Log.w("GG", "Writing data " + message.type + ":");
                if (message.bdata == null) {
                	//Log.w("GG", "skipping Writing data " + message.type + ":");
                    continue;
                }
                
                if (outputStream == null) {
                    inputStream = this.library.mSocket.getInputStream();
                    outputStream = this.library.mSocket.getOutputStream();
                    //continue;
                }
                outputStream.write(message.bdata);
                outputStream.flush();
                //Log.w("GG", "completed Writing data " + message.type + ":" + message.id + ":");


                final byte[] array = new byte[message.len];
                int i;
                int read;
                for (i = 0; i < message.len; i += read) {
                	if(inputStream.available()<=0)
                		break;
                    read = inputStream.read(array, i, message.len - i);
                    if (read == -1) {
                        break;
                    }
                }
                ///Log.i("GG", "completed reading " + i);
                boolean b = false;
                
                if (message.type == ROVState.ping) {
                    b = this.parseResponse(array, message);
                }
                
                
                if (message.type == ROVState.front || message.type == ROVState.back || message.type == ROVState.left||message.type == ROVState.right) 
                {
                //    	b = this.parseResponse(array, message);
                }
                

                //Log.w("ZZ", "parse respobse is " + b);
                if (b) {
                    this.cstatus = true;
                    
                    if (current != null) {
                        //Log.w("GG1", "current received reply :" + this.current.type + ":" + this.current.id+":"+next_flag);
                    }
                    if (reply != null) {
                        //Log.e("GG1", "reply received reply :" + this.reply.type + ":" + this.reply.id + ":" + this.reply.len+":"+next_flag);
                    }
                    else {
                        Log.w("GG", "reply is null");
                    }

                    if (this.reply != null && current != null && current.id == reply.len) {
                        current = null;
                        next_flag = true;
                        Log.e("GG1", "completed motion command---------------------------------------------------------------");
                    }
                    this.scount = 0;                    ;
                }
                else
                {
                	Log.w("ZZ", "invalid response ");
                }
                
                }
                catch (Exception ex) {
                    Log.w("GG", "error in sending socket data" + ex.getLocalizedMessage() + ":");
                    ex.printStackTrace();
                    n = 0;
                    //comm_flag=false;
                    continue;
                }
            }
            }
            this.Queue.clear();
            //Log.w("XX", "completed thread execution");
            return;

        }
        
        

    
    public void setStatus() {
        this.cstatus = false;
    }
    
    public void shutDown() throws InterruptedException {
        this.shuttingDown = true;
        while (true) {
            try {
            	if(this.library!=null)
                this.library.mSocket.close();
                this.Queue.put(new Message("SHUTDOWN", null, 0, -1));
            }
            catch (IOException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }
    
    public String validateCommand(final int n) {
        String front;
        if (n == 1) {
            front = ROVState.front;
        }
        else {
            if (n == 2) {
                return ROVState.back;
            }
            if (n == 3) {
                return ROVState.left;
            }
            if (n == 4) {
                return ROVState.right;
            }
            front = null;
            if (n == 0) {
                return ROVState.ping;
            }
        }
        return front;
    }
    
    class BluetoothLibrary
    {
        public BluetoothAdapter BA;
        Activity act;
        private ArrayAdapter<String> btArrayAdapter;
        BluetoothDevice device;
        private ArrayList<BluetoothDevice> mDeviceList;
        private final BroadcastReceiver mReceiver;
        BluetoothSocket mSocket;
        public Set<BluetoothDevice> pairedDevices;
        
        public BluetoothLibrary(final Activity act) {
            this.mSocket = null;
            this.mDeviceList = new ArrayList<BluetoothDevice>();
            this.act = null;
            
            this.mReceiver = new BroadcastReceiver() {
                public void onReceive(final Context context, final Intent intent) {
                    final String action = intent.getAction();
                    if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                        if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE) == 12) {
                            BluetoothLibrary.this.showToast("Enabled");
                        }
                    }
                    else {
                        /*if ("android.bluetooth.adapter.action.DISCOVERY_STARTED".equals(action)) {
                            BluetoothLibrary.access(BluetoothLibrary.this, new ArrayList());
                            return;
                        }
                        if (!"android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action) && "android.bluetooth.device.action.FOUND".equals(action)) {
                            final BluetoothDevice bluetoothDevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                            BluetoothLibrary.this.mDeviceList.add(bluetoothDevice);
                            BluetoothLibrary.this.showToast("Found device " + bluetoothDevice.getName());
                        }*/
                    }
                }
            };
            this.act = act;
        }
        
         void access(final BluetoothLibrary bluetoothLibrary, final ArrayList mDeviceList) {
            bluetoothLibrary.mDeviceList = (ArrayList<BluetoothDevice>)mDeviceList;
        }
        
        private void showToast(final String s) {
            Toast.makeText(this.getApplicationContext(), (CharSequence)s, 0).show();
        }
        
        public void On() {
            //Log.w("GG", "onnnn ");
            this.init();
            //Log.w("GG", "completed onnnn ");
        }
        
        public boolean connect(final String s) {
        	if(library.BA!=null && library.BA.enable())
        	{
            this.device = null;
            if (this.pairedDevices == null) {
                this.getList();
            }
            if (this.pairedDevices != null) {
                for (final BluetoothDevice device : this.pairedDevices) {
                	
                    Log.w("GG", "present devices is " + device.getName() + ":" + s);
                    
                    if (device!=null && device.getName()!=null && device.getName().equals(s)) {
                        this.device = device;
                        break;
                    }
                }
            }
            else {
                Log.w("GG", "cannot find device");
            }
            if (this.device == null) {
                return false;
            }
            Log.w("GG", "connecting to device name  " + this.device.getName());
            while (true) {
                try {
                	//sleep(1000);
                	if(this.mSocket!=null)
                	{
                		this.mSocket.close();
                		this.mSocket=null;
                	}
                    this.mSocket = (BluetoothSocket)this.device.getClass().getMethod("createRfcommSocket", Integer.TYPE).invoke(this.device, 1);
                    Log.w("GG", "Device " + this.device.getUuids()[0].getUuid() + ":" + CommunicationThread.SPP_UUID);
                    if (this.mSocket == null) {
                        Log.w("SS", "socket is null");
                        break;
                    }
                    BA.cancelDiscovery();
                    Log.e("SS", "socket is not null");
                    Thread.sleep(2000);
                    this.mSocket.connect();
                    Thread.sleep(2000);
                    if (this.mSocket.isConnected()) {
                        Log.e("GG", "socket is connected");
                        Log.e("GG", "completed connecting device");
                        comm_flag=true;
                        Log.e("GG","comm flag is true");
                        return true;
                    }
                    else {
                        Log.w("GG", "socket is not connected");
                        break;
                    }
                    
                    
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    break;
              //      continue;
                }
            }
        	}
        	comm_flag=false;
            return false;
             //   continue;
            //}
        }
        
        public Context getApplicationContext() {
            return this.act.getApplicationContext();
        }
        
        public ArrayAdapter<String> getList() {
            this.pairedDevices = (Set<BluetoothDevice>)this.BA.getBondedDevices();
            this.btArrayAdapter = (ArrayAdapter<String>)new ArrayAdapter<String>((Context)this.act, 0);
            for (final BluetoothDevice bluetoothDevice : this.pairedDevices) {
                this.btArrayAdapter.add(bluetoothDevice.getName());
                //Log.w("GG", "device name  " + bluetoothDevice.getName());
            }
            return this.btArrayAdapter;
        }
        
        public boolean init() {
            if (this.BA != null && this.BA.isEnabled()) {
                Log.w("GG", "disabled enabling");
            }
            (this.BA = BluetoothAdapter.getDefaultAdapter()).enable();
            
                try {
                    Thread.sleep(5000L);
                    this.getList();
                    return true;
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return false;
                }                        
        }
        
        public boolean isOn() {
        	if(this.BA.isEnabled() && comm_flag==true)
            return true;
        	else
        	return false;
        }
        
        public void off() {
            this.BA.disable();
        }
        
        public void stop() {
            while (true) {
                try {
                    CommunicationThread.this.shutDown();
                    Thread.sleep(1000L);
                    if(this.mSocket!=null)
                    	this.mSocket.close();
                    	this.BA.disable();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
    }
    
    class Message
    {
        byte[] bdata;
        String data;
        int id;
        int len;
        String type;
        
        Message(final String type, final byte[] array, final int len, final int id) {
            if (array != null && array.length != 0) {
                this.bdata = new byte[array.length];
            }
            this.type = type;
            this.len = len;
            this.id = id;
            if (array != null) {
                this.bdata = array.clone();
            }
        }
    }
}
