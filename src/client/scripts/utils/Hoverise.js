(function()
{
	"use strict";
	
	S.Hoverise = function (s)
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
	};
}
)();
