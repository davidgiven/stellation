(function()
{
	"use strict";
	
	S.Markup = function (s, object, events)
	{		
		/* Replace 'help' elements with their actual representation. */
		
		$(s).andSelf().find("help").each(
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
		
		$(s).andSelf().find("menubutton").each(
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
		
		$(s).andSelf().find("[class~=needs-ui-state-hover]").each(
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
		
		$(s).andSelf().find("[class~=pane]").each(
			function()
			{
				$(this).addClass("ui-dialog ui-widget-content ui-corner-all");
			}
		);

		/* All buttons become, er, buttons. */
		
		$(s).andSelf().find("input[type=button]").each(
			function ()
			{
				$(this).button();
			}
		);
		
		/* Popup menus. */
		
		$(s).andSelf().find("[class~=menu]").menu(
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
	};
}
)();
