(function()
{
    "use strict";
	var terminal;

	var argify = function (input)
	{
		if (!input)
			return [];
		
		var args = [];
		var i = 0;
		$.each(input.match(/("[^"]*")|(\s+)|([^\s"]+)/g),
			function (_, s)
			{
				var r = /^\s*$/.exec(s);
				if (r)
				{
					i++;
					return;
				}

				if (!args[i])
					args[i] = "";

				r = /^"([^"]*)"$/.exec(s)
				if (r)
				{
					args[i] += r[1];
					return;
				}
				
				args[i] += s;
			}
		);
		
		return args;
	}
	
	var eval_cb = function (command, terminal)
	{
		var args = argify(command);
		if (!args[0])
			return;
		
		var params = {};
		var pcount = -1;
		for (var i=0; i<args.length; i++)
		{
			var s = args[i];
			var r = /^(\w+)=(.*)$/.exec(s);
			if (r)
			{
				if (params[r[1]])
				{
					Terminal.Error("parameter '"+r[1]+"' specified more than once");
					return;
				}
				params[r[1]] = r[2];
			}
			else
			{
				params[pcount] = s;
				pcount++;
			}
		}
		
		var command = params[-1];
		delete params[-1];
		var f = S.Commands[command];
		if (!f)
			Terminal.Error("command '"+command+"' not recognised; try 'help' for a list");
		else
			f(params);
	};
	
	S.Terminal =
	{
		Init: function ()
		{
			terminal = $("#terminal").terminal(eval_cb,
    			{
    				prompt: "stellation> ",
    				height: 400,
    				enabled: false,
    				exit: false,
    				greetings: "",
    				
    				onInit: function (terminal)
    				{
    					terminal.echo("[1;33mWelcome to the Stellation debug console. Try 'help' for help.[0m\n");
    					terminal.echo("\n");
    				}
    			}
			);
			terminal.hide();
			
			var focus = false;
			$(document.documentElement).keypress(
				function (event)
				{
					if (event.which === 96)
					{
						focus = !focus;
						
						terminal.slideToggle("fast");
						terminal.set_command("");
						terminal.focus(focus);
						terminal.attr(
							{
								scrollTop: terminal.attr("scrollHeight")
							}
						);
					}
				}
			);
			
			$(window).scroll(
				function()
				{
					terminal.css(
						{
							top: $("body").attr("scrollTop")
						}
					);
				}
			);
			
			console.log = S.Terminal.Print;
		},
		
		Print: function (s)
		{
			s = s.replace(/\[/g, '&#91;').replace(/\]/g, '&#93;');
			terminal.echo(s);
		},
		
		Error: function (s)
		{
			Terminal.Print("Error: "+s);
		}
	};
}
)();


