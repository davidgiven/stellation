(function()
{
	"use strict";
	
	S.Markup = function (s, object, events)
	{		
		/* Replace 'help' elements with their actual representation. */
		
		$(s).findAndSelf("help").each(
			function ()
			{
				var element = this;
				
				var newnode = $("<a href='#' class='inline-icon ui-state-default ui-corner-all needs-ui-state-hover help-button'>?</a>");
				$(element).replaceWith(newnode);
				$(newnode).tooltip(
					{
						items: "a",
						content: function()
						{
							var e = $("<div/>");
							
							var template = $(element).attr("template");
							if (!template)
								template = element;
							
							S.ExpandTemplate(object, e, template, {});
							return e;
						}
					}
				);
			}
		);
		
		/* Menu buttons. */
		
		$(s).findAndSelf("menubutton").each(
			function()
			{
				var element = this;
				
				var newnode = $("<a href='#' class='inline-icon ui-state-default ui-corner-all needs-ui-state-hover menu-button'>⚙</a>");
				$(element).replaceWith(newnode);
				
				var menu = $(element).find("> UL");
				$("#page").append(menu);
				$(menu).addClass("menu");
				$(menu).hide();
				$(menu).menu(
					{
						select: function(event, ui)
						{
							var e = $(ui.item).find("a");
							var link = e.attr("s_link");
							events[link](object, e); 
						}
					}
				);
				
				var hidden = true;
				var showmenu = function()
				{
					if (!hidden)
						return;
					hidden = false;
						
					$(menu).show();
					$(menu).position(
						{
							of: newnode,
							at: "right bottom",
							my: "right top"
						}
					);
					
					var e = $(menu).find("a:first");
					e.focus();
				};
				
				var hidemenu = function()
				{
					if (hidden)
						return;
					hidden = true;
					
					$(menu).hide();
					newnode.focus();
				};
				
				newnode.click(showmenu);
				menu.mouseleave(hidemenu);
				newnode.hover(showmenu);
				menu.focusout(hidemenu);
			}
		);

		/* Add hover controls to anything which asks for it. */
		
		$(s).findAndSelf("[class~=needs-ui-state-hover]").each(
    		function ()
    		{
    			$(this).hover(
    		        function ()
    		        {
    		            $(this).addClass('ui-state-hover');
    		        },
    		        function () {
    		            $(this).removeClass('ui-state-hover');
    		        }
    		    );
    		}
    	);
		
		/* Add pane style to anything which is a pane. */
		
		$(s).findAndSelf("[class~=pane]").each(
			function()
			{
				$(this).addClass("ui-dialog ui-widget-content ui-corner-all");
			}
		);

		/* All buttons become, er, buttons. */
		
		$(s).findAndSelf("input[type=button]").each(
			function ()
			{
				$(this).button();
			}
		);
		
		/* Popup menus. */
		
		$(s).findAndSelf("[class~=menu]").menu(
			{
				position:
				{
					my: "right top",
					at: "right bottom"
				},
				
				icons:
				{
					submenu: "ui-icon-triangle-1-s"
				}
			}
		);
		
		/* Drag-and-drop senders. */
		
		$(s).findAndSelf("*[s_drag]").each(
			function (_, node)
			{
				var scope = $(node).attr("s_drag");
				$(node).draggable(
        			{
        				helper:
        					function(event)
        					{
        						var t = $("<div class='drag-unit'><table/></div>");
        						t.find("table").append($(node).clone());
        						t.appendTo("#page");
        						t.data("object", object);
        						return t;
        					},
        					
    					cursorAt:
    					{
    				        left: -5,
    				        bottom: 5
    					},
    					
    					cursor: "move",
    					distance: 10,
    					delay: 100,
    					scope: scope,
    					revert: "invalid"
        			}
        		);
			}
		);

		/* Drag-and-drop receivers. */
		
		$(s).findAndSelf("droparea").each(
			function()
    		{
    			var element = this;
    			
    			var newnode = $("<div class='droparea ui-corner-all'><div class='droplabel'/></div>");
    			$(element).replaceWith(newnode);
    			
    			newnode.find(".droplabel").text($(element).attr("label"));
    			
    			newnode.droppable(
    				{
    					scope: $(element).attr("scope"),
    				    hoverClass: 'hover',
    				    tolerance: 'pointer',
    				    
    				    drop:
    				    	function (event, ui)
    				    	{
    				    		var o = ui.helper.data("object");
    							var event = $(element).attr("s_drop");
    							events[event](object, newnode, o); 
    				    	}
    				}
    			);
    		}
		);
	};
}
)();
