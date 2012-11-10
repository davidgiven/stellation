(function()
{
	"use strict";

	var database = {};
	var prototypes = {};
	var notifications = {};
	var changed = {};
	var change_message_pending = false;
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
			database[oid].Oid = oid;
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
			var outo = new S.ObjectSet();
			$.each(o,
				function (_, subo)
				{
					outo.add(find_object(subo));
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
    		
    		var c = S.Classes[classname];
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
	
	var get_notifications_for = function (o)
	{
		var n = notifications[o.Oid];
		if (!n)
			n = notifications[o.Oid] = new S.CallbackSet();
		return n;
	};
	
	var object_changed = function (o)
	{
		changed[o.Oid] = true;
		if (!change_message_pending)
		{
			change_message_pending = true;
			setTimeout(
				function()
				{
					change_message_pending = false;
					var oldchanged = changed;
					changed = {};
					
					for (var oid in oldchanged)
					{
						var cbs = notifications[oid];
						if (cbs)
							cbs.call(find_object(oid));
					}
				},
				0
			);
		}
	};
	
	S.Universe = find_object(0);
	S.Galaxy = null;
	S.Player = null;
	
	S.Database =
	{
		Reset: function()
		{
			allproperties = {};
			S.Player = null;
		},
	
		SetPlayer: function(p)
		{
			S.Player = p;
		},
		
		GetServerTime: function()
		{
			return servertime;
		},
		
		Object: function(oid)
		{
			return find_object(oid);
		},

		Watch: function(o, f)
		{
			get_notifications_for(o).add(f);
			f(o);
		},
		
		Unwatch: function(o, f)
		{
			get_notifications_for(o).remove(f);
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
					object_changed(o);
					
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
			
			S.Galaxy = S.Universe.Galaxy;
		}
	};
}
)();

