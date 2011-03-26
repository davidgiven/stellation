package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.LocationController;
import com.cowlark.stellation3.common.controllers.LocationHandler;
import com.cowlark.stellation3.common.model.SObject;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.gwt.ControllerImpl;
import com.cowlark.stellation3.gwt.S;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class LocationControllerImpl extends ControllerImpl
	implements LocationController, ClickHandler
{
	private final LocationHandler _lh;
	private final Anchor _label;
	private SObject _location;
	
	public LocationControllerImpl(LocationHandler lh, String label)
    {
		super(2);
		_lh = lh;
		setCell(0, label);
		
		_label = new Anchor();
		_label.addClickHandler(this);
		
		setCell(1, _label);
    }
	
	@Override
	public SObject getLocation()
	{
	    return _location;
	}
	
	@Override
	public void setLocation(SObject location)
	{
		_location = location;
		
		StringBuilder sb = new StringBuilder();
		if (_location instanceof SStar)
		{
			SStar star = (SStar) _location;
			
			sb.append(_location.Name.get());
		
			sb.append(" [");
			sb.append(S.COORD_FORMAT.format(star.X.get()));
			sb.append(", ");
			sb.append(S.COORD_FORMAT.format(star.Y.get()));
			sb.append("]");
		}
		
		_label.setText(sb.toString());
	}
	
	@Override
	public void onClick(ClickEvent event)
	{
	}
}
