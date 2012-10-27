(function()
{
    "use strict";
    
    var commands =
    {
    	GetStatics:
    	{
    		parameters: [],
    	},
    	
    	Authenticate:
    	{
    		parameters: ["email", "password"],
    		onCompletion:
    			function ()
    			{
    				console.log("auth complete");
    			}
    	},
    	
    	CreatePlayer:
    	{
    		parameters: ["email", "password", "name", "empire"]
    	},
    	
    	Ping:
    	{
    		gamecommand: true,
    		parameters: []
    	}
    };
    
    G.Commands =
    {
    	help: function (params, oncompletion)
    	{
    		if (params[0])
    		{
    			var n = params[0];
    			var c = commands[n];
    			if (!c)
    				Terminal.Print("There's no help for '"+n+"' --- try 'help' on its own for a list.");
    			else
    			{
    				var s = "Syntax: ";
    				s += n;
    				s += " ";
    				
    				$.each(c.parameters,
    					function (_, name)
    					{
    						s += (name + "=<parameter> ");
    					}
    				);
    				
    				Terminal.Print(s);
    			}
    		}
    		else
    		{
    			Terminal.Print("The following commands are understand. Do 'help <command>' for more info:");
    			$.each(commands,
    				function (name, command)
    				{
    					var s = "  ";
    					s += name;
    					if (command.shortHelp)
    						s += (": " + shortHelp);
    					Terminal.Print(s);
    				}
    			);
    		}
    		
    		if (oncompletion)
    			oncompletion({}); 
    	},
    };
    
    $.each(commands,
    	function (name, command)
    	{
    		G.Commands[name] =
    			function (message, oncompletion)
    			{
    				if (message[0])
    				{
    					Terminal.Error("command '"+name+"' understands key/value arguments only");
    					return;
    				}
    				
    				if (oncompletion)
    				{
    					oncompletion =
    						function (reply)
    						{
    							if (command.onCompletion)
    								command.onCompletion(reply);
    							oncompletion(reply);
    						};
    				}
    				else
    					oncompletion = command.onCompletion;
    				
    				if (command.gamecommand)
    				{
    					message.gcmd = name;
    					IO.GameCommand(message, oncompletion);
    				}
    				else
    				{
    					message.cmd = name;
    					IO.RPC(message, oncompletion);
    				}
    			};
    	}
    );
}
)();


