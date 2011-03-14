#include "CIE.inc"

camera
{
	right x*image_width/image_height
}

#declare temperature = clock;

#declare glowsizecurve = spline
{
	natural_spline
	4000, 5
	7000, 3
	12000, 1
};

#declare rayssizecurve = spline
{
	natural_spline
	4000, 2
	7000, 3
	12000, 5
};

#declare streaksizecurve = spline
{
	natural_spline
	4000, 0.3
	12000, 2
};

#declare brightnesscurve = spline
{
	natural_spline
	4000, 1
	12000, 2
};
 
#declare glowsize = glowsizecurve(temperature).x;
#declare rayssize = rayssizecurve(temperature).x;
#declare streaksize = streaksizecurve(temperature).x;
#declare brightnessmod = brightnesscurve(temperature).x;

#declare effect_location = <0, 0, 9>;
#declare effect_scale = 0.25;

#declare glow_type = 8;
#declare glow_colour = Blackbody(temperature);
#declare glow_scale = glowsize;
#declare rays_type = 5;
#declare rays_colour = glow_colour * brightnessmod;
#declare rays_scale = rayssize*<.6, .4, .4>;
#declare streak_type = 8;
#declare streak_colour = Daylight(temperature) * brightnessmod;
#declare streak_scale = <1, 1, 1> * streaksize;
//#declare spots_type = 5;
//#declare spots_colour = <1.5, 1.3, 1>;
//#declare spots_scale = <1.4, .8, 1>;
//#declare spots_frequency = 1.5;
//#declare spots_seed = 10;
#include "lnsefcts.inc"
