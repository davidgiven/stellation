(function()
{
	"use strict";
	
	S.Markup = function (s)
	{
		$(s).andSelf().find(".needs-ui-state-hover").each(
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
		
		$(s).andSelf().find(":button").each(
			function ()
			{
				$(this).button();
			}
		);
	};
}
)();
