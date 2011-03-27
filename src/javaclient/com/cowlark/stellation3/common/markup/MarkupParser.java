package com.cowlark.stellation3.common.markup;

import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SObject;

public class MarkupParser
{
	public static void render(String markup, HasMarkup factory)
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
				String cmd = commandsplit[0];
				if (cmd.equals("indent"))
				{
					int spaces = Integer.parseInt(commandsplit[1]);
					factory.indent(spaces);
				}
				else if (cmd.equals("bold"))
					factory.emitBoldText(commandsplit[1]);
				else if (cmd.equals("time"))
					factory.emitTime(Long.parseLong(commandsplit[1]));
				else if (cmd.equals("link"))
				{
					String text = commandsplit[1];
					int oid = Integer.parseInt(commandsplit[2]);
					SObject object = (SObject) Game.Instance.Database.get(oid);
					factory.emitLink(text, object);
				}
			}
		}
	}
}
