(function()
{
	"use strict";
	
	S.PreloadImages = function(uris, images, cb)
	{
		var imagecount = uris.length;
		
		$.each(uris,
			function (i, name)
			{
				var image = new Image();
				images[i] = image;
				image.onload =
					function()
					{
						imagecount--;
						if (imagecount == 0)
							cb();
					};
				image.src = name;
			}
		);
	}
}
)();
