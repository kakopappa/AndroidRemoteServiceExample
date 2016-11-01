package com.example.servicetest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class TestService extends Service{
	
	public static final String INTENT_ACTION = "intent.action.bhc.test.service";
	private static final int MSG_WORK = 1;
	
	final RemoteCallbackList<IRemoteServiceCallback> callbacks = new RemoteCallbackList<IRemoteServiceCallback>();
	
	private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
		
		@Override
		public boolean unregisterCallback(IRemoteServiceCallback callback)
				throws RemoteException {
			boolean flag = false;
			
			if(callback != null){
				flag = callbacks.register(callback);
			}
			
			return flag;
		}
		
		@Override
		public boolean registerCallback(IRemoteServiceCallback callback)
				throws RemoteException {
			boolean flag = false;
			
			if(callback != null){
				flag = unregisterCallback(callback);
			}
			
			return flag;
		}

		@Override
		public String getMessage() throws RemoteException {
			return "Hello World!";
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("BHC_TEST", "onBind..");
		
		if(intent.getAction().equals(INTENT_ACTION)){
			Log.d("BHC_TEST", "action is equals.. :: " + intent.getAction());
			return mBinder;
		}
		Log.d("BHC_TEST", "action is not equals..");
		return null;
	}
	
	
	@Override
	public void onCreate() {
		Log.d("BHC_TEST", "Service is onCrreate..");
		
		handler.sendEmptyMessage(MSG_WORK);
		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		Log.d("BHC_TEST", "Service is onDestory..");
		
		handler.removeMessages(MSG_WORK);
		
		super.onDestroy();
	}
	
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case MSG_WORK:
				
				int N = callbacks.beginBroadcast();
				
				for(int i = 0; i < N; i++){
					try {
						callbacks.getBroadcastItem(i).valueChanged(System.currentTimeMillis());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Log.d("BHC_TEST", "Handler work.. :: callbacks clients count is " + N);
				callbacks.finishBroadcast();
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(MSG_WORK);
				
				break;

			default:
				break;
			}
			return false;
		}
	});
	
	

}
