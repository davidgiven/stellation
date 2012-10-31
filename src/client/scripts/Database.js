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
    		var superclass = {};
    		if (superclassname)
    			superclass = create_class(superclassname);
    		
    		var f = function() {};
    		f.prototype = {};
    		
    		for (var name in superclass)
    			f.prototype[name] = superclass[name];
    		
    		var c = G.Classes[classname];
    		if (c)
    			for (var name in c)
    				f.prototype[name] = c[name];

    		var s = statics[classname].statics;
    		for (var name in s)
    			f.prototype[name] = s[name];
    		
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

