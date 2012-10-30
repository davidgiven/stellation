(function()
{
	"use strict";

	var database = {};
	var prototypes = {};
	var servertime = 0;
	var statics;
	var allproperties;
	var classes = {};
	
	var find_object = function (oid)
	{
		if (!database[oid])
		{
			var c = function() {};
			c.prototype = prototypes[oid] = {};
			database[oid] = new c();
		}
		
		return database[oid];
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
					outo.push(find_object(subo));
				}
			);
			return outo;
		}
	};
	
	var create_class = function (classname)
	{
		if (!classes[classname])
		{
    		var superclassname = statics[classname].superclass;
    		var superclass = null;
    		if (superclassname)
    			superclass = create_class(superclassname);
    		
    		var f = function() {};
    		if (superclass)
    			f.prototype = superclass;
    		else
    			f.prototype = {};
    		
    		if (G.Classes[classname])
    			$.each(G.Classes[classname],
    				function (name, value)
    				{
    					f.prototype[name] = value;
    				}
    			);
    		
    		$.each(statics[classname].statics,
    			function (name, value)
    			{
    				f.prototype[name] = value;
    			}
    		);
    		
    		classes[classname] = new f();
		}
		return classes[classname];
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
		
		Object: function(oid)
		{
			return database[oid];
		},

		ParseStatics: function(msg)
		{
			allproperties = {};
			statics = msg.classes;
			$.each(statics,
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
			servertime = message.time;
			
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
					
					if (properties.Class)
					{
						var c = create_class(properties.Class);
						var p = prototypes[oid];
						
						/* Yes, this is correct --- we want to iterate over
						 * all properties in both the class and its
						 * superclasses. */
						
						for (var name in c)
							p[name] = c[name];
					}
				}
			);
		}
	};
}
)();

