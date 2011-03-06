package com.cowlark.stellation3.common.database;

public interface AuthenticationListener
{
	void onAuthenticationSucceeded(int playeroid);
	void onAuthenticationFailed();
}
