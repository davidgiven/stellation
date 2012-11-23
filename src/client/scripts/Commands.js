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
    			function (msg)
    			{
    				S.Database.SetPlayer(S.Database.Object(msg.oid));
    				console.log("auth complete, oid="+msg.oid);
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
    	
    	ChangeFleetTarget:
    	{
    		gamecommand: true,
    		parameters: ["oid", "x", "y"]
    	},
    	
    	Jump:
    	{
    		gamecommand: true,
    		parameters: ["oid"]
    	},
    	
    	CargoshipLoadUnload:
    	{
    		gamecommand: true,
    		parameters: ["oid", "m", "a", "o"]
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
        				S.Terminal.Print("There's no help for '"+n+"' --- try 'help' on its own for a list.");
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
        				
        				S.Terminal.Print(s);
        			}
        		}
        		else
        		{
        			S.Terminal.Print("The following commands are understand. Do 'help <command>' for more info:");
        			$.each(commands,
        				function (name, command)
        				{
        					var s = "  ";
        					s += name;
        					if (command.shortHelp)
        						s += (": " + shortHelp);
        					S.Terminal.Print(s);
        				}
        			);
        			S.Terminal.Print("Capitalised commands call the server, lower case commands are local.");
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
        			S.Terminal.Error("you must supply an object id");
        			return;
        		}
        
        		var o;
        		if (params.oid === "me")
        			o = S.Player;
        		else
        			o = S.Database.Object(params.oid);
        		if (!o)
        		{
        			S.Terminal.Error("Object "+params.oid+" does not exist");
        			return;
        		}
        		
        		var s = "#" + params.oid;
        		if (o.Name)
        			s += (" ("+o.Name+")");
        		s += (" "+o.Class);
        		S.Terminal.Print(s);
        		
        		$.each(o,
        			function (k, v)
        			{
        				if (o.hasOwnProperty(k))
        				{
        					var s;
        					
        					if (v instanceof S.ObjectSet)
        					{
        						var ss = [];
        						$.each(v,
        							function (e)
    								{
    									ss.push("#" + e.Oid);
    								}
        						);
        						s = "[" + ss.join(", ") + "]";
        					}
        					else if (v instanceof Object)
        						s = "#" + v.Oid;
        					else
        						s = v;
        						
        					
        					S.Terminal.Print("  "+k+" = "+s);
        				}
        			}
        		);
        		
        		if (oncompletion)
        			oncompletion({}); 
        	}
    	}
    };
    
    S.Commands = {};
    $.each(commands,
    	function (name, command)
    	{
    		if (command.callback)
    			S.Commands[name] = command.callback;
    		else
        		S.Commands[name] =
        			function (message, oncompletion)
        			{
        				if (message[0])
        				{
        					S.Terminal.Error("command '"+name+"' understands key/value arguments only");
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
        					S.IO.GameCommand(message, cb);
        				}
        				else
        				{
        					message.cmd = name;
        					S.IO.RPC(message, cb);
        				}
        			};
    	}
    );
}
)();


