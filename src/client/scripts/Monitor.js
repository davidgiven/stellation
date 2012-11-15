(function()
{
    "use strict";
    
    var teardown = function (element)
    {
		var o = $(element).data("s_object");
		var f = $(element).data("s_callback");
		S.Database.Unwatch(o, f);
    };
    
    /* Register our magic cleanup event. */
    
    $.event.special.s_objectdestroyed =
    {
        teardown:
            function()
            {
        		teardown(this);
        	}
    };

    var templatefiles = {};
    var loadtemplate = function (name, id, cb)
    {
    	var f = function(element)
    	{
    		var e = $(element).find("[id='"+id+"']");
    		cb(e);
    	}
    	
    	var e = templatefiles[name];
    	if (e)
    	{
    		if (e._loaded)
    			f(e);
    		else
    			e._callbacks.add(f);
    		return;
    	}
    	
    	e = {};
    	e._loaded = false;
    	e._callbacks = $.Callbacks();
    	e._callbacks.add(f);
    	templatefiles[name] = e;

    	var filename = "templates/" + name + ".xml"; 
    	$.ajax(
    		{
    			type: "GET",
            	url: filename,
            	dataType: "xml",
            	
            	success:
            		function(xml)
                	{
                		xml._loaded = true;
                		xml._callbacks = e._callbacks;
                		e = xml;
                		templatefiles[name] = e;
            			e._callbacks.fire(e);
                	},
                	
                error:
                	function(_, status, error)
                	{
                		console.log("AJAX error loading "+filename+": "+status);
                	}
    		}
    	);
    };
    
    S.Monitor =
    	function (object, element, callback)
    	{
    		element = $(element).first();
    		
    		var object_changed_cb =
    			function (o)
    			{
    				callback(o, element);
    			};
    			
    		if (element.data("s_object") || element.data("s_callback"))
    			teardown(element);
    		
    		/* Ensure that when the element is destroyed, we unregister the
    		 * watch callback. */
    		
    		element.data("s_object", object);
    		element.data("s_callback", object_changed_cb);
    		element.on("s_objectdestroyed", $.noop);
    			
    		S.Database.Watch(object, object_changed_cb);
    	};

    S.ExpandTemplate = function (object, element, template, events)
	{
    	if (typeof(template) == "string")
    	{
			if (!S.Player.cansee(object))
				template += ".invisible";
    		
    		var dot = template.indexOf(".");
    		if (dot == -1)
    			template = document.getElementById(template);
    		else
    		{
    			var left = template.substring(0, dot);
    			var right = template.substring(dot+1);
    			
    			loadtemplate(left, right,
    				function (e)
    				{
    					S.ExpandTemplate(object, element, e, events);
    				}
    			);
    			return;
    		}
    	}
		var tdom = $(template).contents().clone();
		
		/* Expand any attribute references. Don't add them just yet or else
		 * we'll recurse into them while doing the template expansion. That's
		 * bad. */
		
		var appendations = [];
		$(tdom).findAndSelf("*[s_attr]").each(
			function (_, node)
			{
				var attrname = $(node).attr("s_attr");
				var v = object[attrname];
				if (typeof(v) == "function")
					v = v.call(object, object);
				if (typeof(v) == "string")
					v = document.createTextNode(v);
				
				appendations.push({node: $(node), value: v});
			}
		);
		
		/* Connect up links. */
		
		$(tdom).findAndSelf("*[s_link]").each(
			function (_, node)
			{
				var name = $(node).attr("s_link");
				$(node).click(
					function()
					{
						events[name](object, element);
					}
				);
			}
		);

		/* Add any needed jquery markup. */
				
		S.Markup(tdom, object);

		/* Now add all nodes we thought about appending earlier. */
		
		$.each(appendations,
			function (_, p)
			{
				p.node.append(p.value);
			}
		);
		
		/* Add the DOM to the document. */
		
		$(element).empty();
		$(tdom).appendTo(element);
		
		/* Notify the user that the template has loaded. */
		
		if (events && events._changed)
			events._changed(object, element);
	};
    	
    S.TemplatedMonitor = function (object, element, template, events)
	{
		var object_change_cb =
			function (o)
			{
				S.ExpandTemplate(object, element, template, events);
			}
		
		S.Monitor(object, element, object_change_cb);
	};
})();
