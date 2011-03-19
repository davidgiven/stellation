package com.cowlark.stellation3.common.monitors;

import java.util.List;
import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.game.Game;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class PaneMonitorAdaptor implements Pane, PaneHandler, HasMonitors
{
	private Pane _pane;
	private PaneHandler _ph;
	private HasMonitors _monitor;
	private boolean _updating;
	
	public PaneMonitorAdaptor(HasMonitors monitor, PaneAspect aspect,
			PaneHandler ph, String title)
    {
		_pane = Game.Instance.showPane(aspect, this, title);
		_ph = ph;
		_monitor = monitor;
		_updating = false;
		
		_monitor.attach(this);
    }
	
	@Override
	public void attach(HasMonitors parent)
	{
		assert(false);
	}
	
	@Override
	public void detach()
	{
		assert(false);
	}
	
	@Override
	public void emitControllers(List<Controller> controllers)
	{
		_monitor.emitControllers(controllers);
	}
	
	@Override
	public void updateAllMonitors()
	{
		if (!_updating)
		{
			_updating = true;
			Scheduler.get().scheduleDeferred(
					new ScheduledCommand()
					{
						@Override
						public void execute()
						{
							Vector<Controller> controllers = new Vector<Controller>();
							emitControllers(controllers);
							_pane.updateControllers(controllers);
							_updating = false;
						}
					});
		}
	}
	
	@Override
	public void cancelPane()
	{
		_pane.cancelPane();
	}
	
	@Override
	public void closePane()
	{
		_pane.closePane();
	}
	
	@Override
	public void onPaneCancelled(Pane d)
	{
		detach();
		if (_ph != null)
			_ph.onPaneCancelled(this);
	}
	
	@Override
	public void onPaneClosed(Pane d)
	{
		detach();
		if (_ph != null)
			_ph.onPaneClosed(this);
	}
	
	@Override
	public void updateControllers(List<Controller> controllers)
	{
		assert(false);
	}
}
