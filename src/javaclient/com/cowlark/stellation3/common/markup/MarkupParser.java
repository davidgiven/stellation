package com.cowlark.stellation3.common.markup;

import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SObject;
import com.google.gwt.text.client.DoubleParser;

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
				switch (MarkupCommand.valueOf(commandsplit[0]))
				{
					case Indent:
					{
						int spaces = Integer.parseInt(commandsplit[1]);
						factory.indent(spaces);
						break;
					}
					
					case Bold:
						factory.emitBoldText(commandsplit[1]);
						break;
						
					case Time:
						factory.emitTime(Long.parseLong(commandsplit[1]));
						break;
						
					case Link:
					{
						String text = commandsplit[1];
						int oid = Integer.parseInt(commandsplit[2]);
						SObject object = (SObject) Game.Instance.Database.get(oid);
						factory.emitLink(text, object);
						break;
					}
					
					case Resources:
					{
						double m = Double.parseDouble(commandsplit[1]);
						double a = Double.parseDouble(commandsplit[2]);
						double o = Double.parseDouble(commandsplit[3]);
						factory.emitResources(m, a, o);
						break;
					}
				}
			}
		}
	}
}
