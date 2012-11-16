(function()
{
	"use strict";
	
	S.Markup = function (s, object)
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
		
		/* All buttons become, er, buttons. */
		
		$(s).andSelf().find("input[type=button]").each(
			function ()
			{
				$(this).button();
			}
		);
	};
}
)();
