package com.cowlark.stellation3.common.controllers;

public interface DialogueHandler
{
	public void onDialogueCancelled(Dialogue d);
	public void onDialogueClosed(Dialogue d);
}
