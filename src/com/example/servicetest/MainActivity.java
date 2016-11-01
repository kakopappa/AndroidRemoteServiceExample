package com.example.servicetest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.servicetest.service.IRemoteService;
import com.example.servicetest.service.IRemoteServiceCallback;
import com.example.servicetest.service.TestService;

public class MainActivity extends Activity {
	 
	IRemoteServiceCallback mCallbcak = new IRemoteServiceCallback.Stub() {
		
		@Override
		public void valueChanged(long value) throws RemoteException {
			Log.i("BHC_TEST", "Activity Callback value : " + value);
		}
	};
	
	IRemoteService mService;
	ServiceConnection mConntection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
			if(mService != null){
				try {
					mService.unregisterCallback(mCallbcak);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			
			if(service != null){
				mService = IRemoteService.Stub.asInterface(service);
				try {
					mService.registerCallback(mCallbcak);
					String message = mService.getMessage();
					Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	private void startServiceBind(){
		startService(new Intent(this, TestService.class));
		bindService(new Intent(TestService.INTENT_ACTION), mConntection, Context.BIND_AUTO_CREATE);
	}
	
	private void stopServiceBind(){
		unbindService(mConntection);
		stopService(new Intent(this, TestService.class));
	}	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	startServiceBind();
    	super.onResume();
    }
    
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	stopServiceBind();
    	super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
