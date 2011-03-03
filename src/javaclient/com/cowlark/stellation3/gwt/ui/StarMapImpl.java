package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.game.StarMap;
import com.google.gwt.user.client.ui.RequiresResize;
import com.hydro4ge.raphaelgwt.client.PathBuilder;
import com.hydro4ge.raphaelgwt.client.Raphael;

public class StarMapImpl extends Raphael implements StarMap, RequiresResize
{
	public StarMapImpl(int width, int height)
    {
		super(width, height);
    }
	
	@Override
	public void onResize()
	{
	    int w = getOffsetWidth();
	    int h = getOffsetHeight();
		setSize(w, h);
		redraw();
	}
	
	@Override
	protected void onLoad()
	{
	    super.onLoad();
	    
	    redraw();
	}
	
	private void redraw()
	{
	    int w = getOffsetWidth();
	    int h = getOffsetHeight();
	    
		clear();
	    PathBuilder pb = new PathBuilder();
	    pb.M(0, 0)
	        .m(0, 0)
	        .l(w, h)
	        .z();
	    Path p = new Path(pb);
	}
}
