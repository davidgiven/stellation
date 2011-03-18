package com.cowlark.stellation3.common.markup;

import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SStar;

public class MarkupParser
{
	public static void render(String markup, MarkupFactory factory)
	{
		String[] split = markup.split("\1");
		int i = 0;
		
		while (i < split.length)
		{
			String plaintext = split[i++];
			if (plaintext.length() > 0)
				factory.emitPlainText(plaintext);
			
			if (i == split.length)
				break;
			
			String command = split[i++];
			String[] commandsplit = command.split("\2");
			
			if (commandsplit.length > 0)
			{
				if (commandsplit[0].equals("indent"))
				{
					int spaces = Integer.parseInt(commandsplit[1]);
					factory.indent(spaces);
				}
				else if (commandsplit[0].equals("bold"))
					factory.emitBoldText(commandsplit[1]);
				else if (commandsplit[0].equals("star"))
				{
					int oid = Integer.parseInt(commandsplit[1]);
					SStar star = (SStar) Game.Instance.Database.get(oid);
					String name = commandsplit[2];
					double x = Double.parseDouble(commandsplit[3]);
					double y = Double.parseDouble(commandsplit[4]);
					factory.emitStar(star, name, x, y);
				}
				else if (commandsplit[0].equals("fleet"))
				{
					int oid = Integer.parseInt(commandsplit[1]);
					SFleet fleet = (SFleet) Game.Instance.Database.get(oid);
					String name = commandsplit[2];
					factory.emitFleet(fleet, name);
				}
			}
		}
	}
}
