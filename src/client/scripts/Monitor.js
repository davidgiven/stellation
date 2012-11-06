(function()
{
    "use strict";
    
    /* Register our magic cleanup event. */
    
    $.event.special.s_objectdestroyed =
    {
        teardown:
            function()
            {
        		var o = $(this).data("s_object");
        		var f = $(this).data("s_callback");
        		S.Database.Unwatch(o, f);
        	}
    };

    S.Monitor =
    	function (object, element, callback)
    	{
    		var object_changed_cb =
    			function (o)
    			{
    				callback(o, element);
    			};
    			
    		if (element.data("s_object") || element.data("s_callback"))
    			throw "element "+element+" is already a monitor";
    		
    		/* Ensure that when the element is destroyed, we unregister the
    		 * watch callback. */
    		
    		element.data("s_object", object);
    		element.data("s_callback", object_changed_cb);
    		element.on("s_objectdestroyed", $.noop);
    			
    		S.Database.Watch(object, object_changed_cb);
    	};

    S.TemplatedMonitor =
    	function (object, element, template, events)
    	{
    		var object_change_cb =
    			function (o)
    			{
            		var ts = document.getElementById(template).innerHTML;
            		var tdom = $(ts);
            		
    	    		/* Expand any text in the template. */
    	    		
            		var textnodes = tdom.andSelf().contents().filter(
            			function()
            			{
            				return this.nodeType == 3;
            			}
            		);
            		
            		$.each(textnodes,
            			function (_, node)
            			{
            				node.nodeValue = node.nodeValue.replace(
            					/{{([^}:]*:)?([^}]+)}}/g,
            	    			function (m, a, b)
            	    			{
            	    				var v = "" + object[b];
            	    				return v.escapeHTML();
            	    			}
            				);
            			}
    	    		);
    	    		
            		/* Connect up links. */
            		
            		$.each(tdom.find("*[s_link]"),
            			function (_, node)
            			{
            				var name = $(node).attr("s_link");
            				$(node).click(
            					function()
            					{
            						events[name](o, element);
            					}
            				);
            			}
            		);
            		
    	    		element.empty();
    	    		element.append(tdom);
    			}
    		
    		S.Monitor(object, element, object_change_cb);
    	};
})();
