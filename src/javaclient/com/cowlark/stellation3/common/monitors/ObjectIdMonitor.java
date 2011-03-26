package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SObject;

public class ObjectIdMonitor extends Monitor<SObject>
{
	private LabelController _controller;
	
	public ObjectIdMonitor(SObject o)
    {
		super(o);
		_controller = Game.Instance.createLabelController("Name");
    }
	
	@Override
	protected void update(SObject object)
	{
		StringBuilder sb = new StringBuilder();
		
		String type = Game.Instance.Static.getString(object.Class.get(), Hash.Name);
		if (type == null)
			type = object.Class.get().toString();
		
		if (object.Name != null)
		{
			sb.append(object.Name.get());
			sb.append(" (");
			sb.append(type);
			sb.append(", #");
		}
		else
		{
			sb.append(type);
			sb.append(" (#");
		}
		
		sb.append(object.Oid);
		sb.append(")");
		
		_controller.setStringValue(sb.toString());
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
