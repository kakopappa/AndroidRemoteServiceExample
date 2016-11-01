package com.example.servicetest.service;

import com.example.servicetest.service.IRemoteServiceCallback;

interface IRemoteService {
	boolean registerCallback(IRemoteServiceCallback callback);
	boolean unregisterCallback(IRemoteServiceCallback callback);
	String getMessage();
}