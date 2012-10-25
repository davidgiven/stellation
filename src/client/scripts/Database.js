(function()
{
	"use strict";

	var database;
	var servertime;
	var allproperties;
	
	var find_object = function (oid)
	{
		oid = oid|0;
		var o = database[oid];
		if (!o)
		{
			o = { Oid: oid };
			database[oid] = o;
		}
		return o;
	};
	
	var typemapper =
	{
		number: function(o) { return o; },
		string: function(o) { return o; },
		object: function(o) { return find_object(o); },
		
		objectset: function(o)
		{
			var outo = [];
			$.each(o,
				function (_, subo)
				{
					outo.push(find_object(o));
				}
			);
			return outo;
		}
	};
	
	G.Database =
	{
		Reset: function()
		{
			database = {};
			servertime = 0;
			allproperties = {};
		},
	
		GetServerTime: function()
		{
			return servertime;
		},
		
		ParseStatics: function(msg)
		{
			allproperties = {};
			$.each(msg.classes,
				function (_, c)
				{
					$.each(c.properties,
						function (name, type)
						{
							var f = typemapper[type.type];
							if (!f)
								console.log("!!! no typemapper entry for "+type.type);
							else
							{
								allproperties[name] = f;
							}
						}
					);
				}
			);
		},
		
		Synchronise: function(message)
		{
			console.log("applying delta from "+servertime+" to "+message.time);
			
			$.each(message.changed,
				function (oid, properties)
				{
					var o = find_object(oid);
					$.each(properties,
						function (name, value)
						{
							var f = allproperties[name];
							if (!f)
								console.log("!!! unknown property delta for "+name);
							else
								o[name] = f(value);
						}
					);
				}
			);
		}
	};
}
)();

