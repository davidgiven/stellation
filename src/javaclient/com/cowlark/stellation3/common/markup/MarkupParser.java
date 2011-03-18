package com.cowlark.stellation3.common.markup;

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
				if (commandsplit[0].equals("bold"))
					factory.emitBoldText(commandsplit[1]);
			}
		}
	}
}
