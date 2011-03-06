package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.database.Reader;
import com.cowlark.stellation3.common.database.Transport;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class TransportImpl extends Transport implements RequestCallback
{
	private String _url;
	
	public TransportImpl(String url)
	{
		_url = url;
	}
	
	@Override
	protected void sendRawMessage(String[] data)
	{
		assert(data.length > 0);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length-1; i++)
		{
			sb.append(data[i]);
			sb.append("\u0001");
		}
		sb.append(data[data.length-1]);
		
		try
		{
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, _url);
			rb.setRequestData(sb.toString());
			rb.setCallback(this);
			rb.send();
		}
		catch (RequestException e)
		{
			ioError(e);
		}
	}	

	@Override
	public void onError(Request request, Throwable exception)
	{
		ioError(exception);
	}
	
	private native JsArrayString parseJsonResponse(String value) /*-{
		return eval(value);
	}-*/;
	
	@Override
	public void onResponseReceived(Request request, Response response)
	{
		int code = response.getStatusCode();
		if (code != 200)
			ioError(new Throwable("Unexpected response "+code+" from server"));
		else
		{
			String s = response.getText();
			JsArrayString data = parseJsonResponse(s);
			Reader reader = new JsArrayReader(data);
			processRawMessage(reader);
		}
	}
}
