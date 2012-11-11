(function()
{
	"use strict";
	
	S.PreloadImages = function(uris, images, cb)
	{
		var imagecount = uris.length;
		
		$.each(uris,
			function (i, name)
			{
				var image = images[i];
				if (!image)
				{
					image = new Image();
					images[i] = image;
				}
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
	
	S.PreloadImage = function(uri, cb)
	{
		var image = new Image();
		image.onload =
			function()
			{
				cb(image);
			}
		image.src = uri;
	}
}
)();
