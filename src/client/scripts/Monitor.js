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
		var ts = document.getElementById(template).innerHTML;
		var tdom = $(ts);
		
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
				
		S.Markup(tdom);

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
	};
    	
    S.TemplatedMonitor = function (object, element, template, events)
	{
		var object_change_cb =
			function (o)
			{
				S.ExpandTemplate(object, element, template, events);
	    		
	    		/* Do any custom code. */
	    		
	    		if (events && events._changed)
	    			events._changed(o, element);
			}
		
		S.Monitor(object, element, object_change_cb);
	};
})();
