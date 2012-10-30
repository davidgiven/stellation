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
    	},
    	
    	
    	help:
    	{
    		parameters: [],
    		callback: function (params, oncompletion)
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
        			Terminal.Print("Capitalised commands call the server, lower case commands are local.");
        		}
        		
        		if (oncompletion)
        			oncompletion({}); 
        	},
    	},
    	
    	x:
    	{
    		parameters: ["oid"],
    		callback: function (params, oncompletion)
        	{
        		if (params[0] && !params.oid)
        			params.oid = params[0];
        		if (!params.oid)
        		{
        			Terminal.Error("you must supply an object id");
        			return;
        		}
        
        		var o = Database.Object(params.oid);
        		if (!o)
        		{
        			Terminal.Error("Object "+params.oid+" does not exist");
        			return;
        		}
        		
        		var s = "#" + params.oid;
        		if (o.Name)
        			s += (" ("+o.Name+")");
        		s += (" "+o.Class);
        		Terminal.Print(s);
        		
        		$.each(o,
        			function (k, v)
        			{
        				if (o.hasOwnProperty(k))
        					Terminal.Print("  "+k+" = "+v);
        			}
        		);
        		
        		if (oncompletion)
        			oncompletion({}); 
        	}
    	}
    };
    
    G.Commands = {};
    $.each(commands,
    	function (name, command)
    	{
    		if (command.callback)
    			G.Commands[name] = command.callback;
    		else
        		G.Commands[name] =
        			function (message, oncompletion)
        			{
        				if (message[0])
        				{
        					Terminal.Error("command '"+name+"' understands key/value arguments only");
        					return;
        				}
        				
        				var cb = function (reply)
        				{
							if (command.onCompletion)
								command.onCompletion(reply);
							if (oncompletion)
								oncompletion(reply);
        				};
        				
        				if (command.gamecommand)
        				{
        					message.gcmd = name;
        					IO.GameCommand(message, cb);
        				}
        				else
        				{
        					message.cmd = name;
        					IO.RPC(message, cb);
        				}
        			};
    	}
    );
}
)();


