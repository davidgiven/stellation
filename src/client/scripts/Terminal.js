(function()
{
    "use strict";
	var terminal;

	var eval_cb = function (command, terminal)
	{
	};
	
	G.Terminal =
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
			
			console.log = Terminal.Print;
		},
		
		Print: function (s)
		{
			s = s.replace(/\[/g, '&#91;').replace(/\]/g, '&#93;');
			terminal.echo(s);
		}
	};
}
)();


