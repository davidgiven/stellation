package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.ObjectSummaryController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SUnit;

public class ObjectSummaryMonitor extends Monitor<SUnit>
{
	private ObjectSummaryController _controller;
	
	public ObjectSummaryMonitor(SUnit o)
    {
		super(o);
		_controller = Game.Instance.createObjectSummaryController();
    }
	
	@Override
	protected void update(SUnit object)
	{
		String name;
		if (object.Name != null)
			name = object.Name.get();
		else
			name = Game.Instance.Static.getString(object.Class.get(), Hash.Name);
		_controller.setNameMarkup(name);
		
		int damage = (int) object.Damage.get();
		int maxdamage = Game.Instance.Static.getInt(object.Class.get(), Hash.MaxDamage);
		
		_controller.setDamage(damage, maxdamage);
		
		_controller.setDetailsMarkup(object.getSummaryDetailsMarkup());
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
