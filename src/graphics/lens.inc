/*************************************************************************
    PREDEFINED LENS EFFECTS INCLUDE FILE FOR PERSISTENCE OF VISION 3.x
**************************************************************************

Created by Chris Colefax, 18 July 1997
Updated 9 August 1998: updated for POV-Ray 3.1

NOTE: This file requires that LnsEfcts.inc be properly installed
See "LnsEfcts.htm" for more information.

*************************************************************************/

// CHECK LENS EFFECT TYPE
// **********************
   #ifndef (effect_type) #declare effect_type = ""
      #else #declare effect_type = strupr(effect_type) #end
   #ifndef (options_only) #declare options_only = false; #end
   #declare _LE_created = false;

// RESET LENS EFFECT OPTIONS
// *************************
   #declare glow_type = 0;
   #declare glow_colour = <1, 1, 1>;
   #declare glow_scale = <1, 1, 1>;
   #declare glow_rotate = 0;
   #declare glow_intensity = 1;

   #declare rays_type = 0;
   #declare rays_colour = <1, 1, 1>;
   #declare rays_scale = <1, 1, 1>;
   #declare rays_rotate = 0;
   #declare rays_intensity = 1;
   #declare rays_seed = 0;
   #declare rays_frequency = 1;

   #declare streak_type = 0;
   #declare streak_colour = <1, 1, 1>;
   #declare streak_scale = <1, 1, 1>;
   #declare streak_rotate = 0;
   #declare streak_intensity = 1;
   #declare streak_seed = 0;
   #declare streak_to_centre = false;

   #declare spots_type = 0;
   #declare spots_colour = <1, 1, 1>;
   #declare spots_scale = <1, 1, 1>;
   #declare spots_rotate = 0;
   #declare spots_intensity = 1;
   #declare spots_seed = 0;
   #declare spots_to_centre = true;
   #declare spots_frequency = 1;
   #declare spots_spacing = 1;

   #declare custom_effect_file = ""

// SET LENS EFFECT OPTIONS
// ***********************
   #if (strcmp(effect_type, "CAMERA") = 0)
      #declare glow_type = 4;
      #declare glow_colour = <1.1, .3, .4>;
      #declare rays_type = 1;
      #declare spots_type = 1;
      #declare spots_colour = <1.3, 1, .8>;
      #declare spots_intensity = 1.5;
      #declare spots_seed = 60;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "CAMERA2") = 0)
      #declare glow_type = 3;
      #declare glow_colour = <.9, 1, 1.1>;
      #declare glow_scale = <1, 1, 1> * 1.2;
      #declare rays_type = 1;
      #declare rays_frequency = .5;
      #declare streak_type = 2;
      #declare streak_intensity = .2;
      #declare spots_type = 1;
      #declare spots_colour = <1.1, 1.2, 1.3>;
      #declare spots_intensity = 1.7;
      #declare spots_seed = 45;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "CANDLE") = 0)
      #declare glow_type = 5;
      #declare glow_colour = <1.2, 1.2, .6>;
      #declare glow_scale = <1, 1, 1> * .6;
      #declare glow_intensity = .5;
      #declare rays_type = 1;
      #declare rays_colour = <1.1, 1.1, .5>;
      #declare rays_scale = <1, 1, 1> * .6;
      #declare rays_intensity = 2;
      #declare rays_frequency = .4;
      #declare streak_type = 1;
      #declare streak_colour = <1.3, 1.2, .6>;
      #declare streak_scale = <1, 1, 1> * .7;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "DIAMOND") = 0)
      #declare glow_type = 2;
      #declare glow_colour = <.7, .3, 1.1>;
      #declare rays_type = 7;
      #declare spots_type = 4;
      #declare spots_colour = <1.1, .5, 1.2>;
      #declare spots_frequency = 1.4;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "HEADLIGHT") = 0)
      #declare glow_type = 5;
      #declare glow_colour = <.3, 1, 1.6>;
      #declare glow_scale = <1, .75, 1>;
      #declare glow_intensity = 1.5;
      #declare rays_type = 6;
      #declare rays_intensity = .5;
      #declare streak_type = 7;
      #declare streak_colour = <.6, .9, 1.4>;
      #declare streak_scale = <.8, .5, 1>;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "HEADLIGHT2") = 0)
      #declare glow_type = 5;
      #declare glow_colour = <1, 1.4, .9>;
      #declare glow_scale = <1.4, .8, 1>;
      #declare rays_type = 2;
      #declare rays_scale = <.6, .4, .4>;
      #declare streak_type = 7;
      #declare streak_colour = <1.3, 1, .8>;
      #declare streak_scale = <1, 1, 1> * .6;
      #declare spots_type = 5;
      #declare spots_colour = <1.5, 1.3, 1>;
      #declare spots_scale = <1.4, .8, 1>;
      #declare spots_frequency = 1.5;
      #declare spots_seed = 10;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "RAINBOW2") = 0)
      #declare glow_type = 7;
      #declare rays_type = 5;
      #declare streak_type = 3;
      #declare streak_scale = <1, 1, 1> * .7;
      #declare spots_type = 3;
      #declare spots_intensity = 1.3;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SOFTGLOW") = 0)
      #declare glow_type = 1;
      #declare glow_colour = <1.2, 1, .5>;
      #declare rays_type = 4;
      #declare rays_colour = <1.2, 1.1, .7>;
      #declare spots_type = 2;
      #declare spots_colour = <1.2, 1.1, .7>;
      #declare spots_seed = 80;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SPARKLE") = 0)
      #declare glow_type = 2;
      #declare glow_colour = <1, .9, .7>;
      #declare glow_scale = <1, 1, 1> * .3;
      #declare rays_type = 3;
      #declare rays_scale = <1, 1, 1> * .4;
      #declare streak_type = 5;
      #declare streak_scale = <1, 1, 1> * .4;
      #declare streak_rotate = -15;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SPARKLE2") = 0)
      #declare glow_type = 5;
      #declare glow_scale = <1, 1, 1> * .6;
      #declare rays_type = 2;
      #declare rays_scale = <1, 1, 1> * .6;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SPOTLIGHT") = 0)
      #declare glow_type = 1;
      #declare glow_scale = <1, 1, 1> * .4;
      #declare rays_type = 2;
      #declare rays_scale = <1, 1, 1> * .5;
      #declare streak_type = 4;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SPOTLIGHT2") = 0)
      #declare glow_type = 1;
      #declare glow_colour = <.6, 1, 1.4>;
      #declare glow_scale = <1, 1, 1> * .5;
      #declare rays_type = 6;
      #declare rays_colour = <.6, 1, 1.4>;
      #declare rays_intensity = 3;
      #declare rays_frequency = 7/9;
      #declare spots_type = 8;
      #declare spots_colour = <.6, 1, 1.4>;
      #declare spots_scale = <1, 1, 1> * .4;
      #declare spots_seed = 20;
      #declare spots_frequency = 1.5;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "STAR") = 0)
      #declare glow_type = 1;
      #declare glow_colour = <.6, .9, 1.2>;
      #declare glow_scale = <1, 1, 1> * .3;
      #declare rays_type = 3;
      #declare rays_scale = <1, 1, 1> * .5;
      #declare rays_frequency = .6;
      #declare rays_intensity = 3;
      #declare streak_type = 6;
      #declare streak_colour = <.8, .9, 1.1>;
      #declare streak_scale = <1, 1, 1> * .5;
      #declare streak_rotate = -15;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "STAR2") = 0)
      #declare glow_type = 1;
      #declare glow_colour = <.8, 1, 1.2>;
      #declare glow_scale = <1, 1, 1> * .5;
      #declare rays_type = 2;
      #declare rays_scale = <.5, .7, 1>;
      #declare rays_frequency = 1.5;
      #declare streak_type = 8;
      #declare streak_colour = <.9, 1, 1.1>;
      #declare streak_scale = <1, 1, 1> * .6;
      #declare streak_rotate = 90;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SUN") = 0)
      #declare glow_type = 4;
      #declare glow_colour = <1.5, 1.1, .3>;
      #declare glow_scale = <1, 1, 1> * 1.3;
      #declare rays_type = 3;
      #declare rays_frequency = 1.5;
      #declare spots_type = 6;
      #declare spots_colour = <1.3, .9, .3>;
      #declare spots_frequency = 1.4;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SUN2") = 0)
      #declare glow_type = 2;
      #declare glow_colour = <1.1, .6, .2>;
      #declare rays_type = 1;
      #declare rays_colour = <1.1, .8, .2>;
      #declare rays_scale = <1, 1, 1> * 1.2;
      #declare rays_intensity = 2;
      #declare rays_seed = 4;
      #declare streak_type = 7;
      #declare streak_colour = <1.1, .7, .2>;
      #declare streak_scale = <1, 1, 1> * .8;
      #declare streak_to_centre = true;
      #declare spots_type = 5;
      #declare spots_colour = <1.1, .8, .2>;
      #declare spots_scale = <1, 1, 1> * .8;
      #declare spots_intensity = .5;
      #declare spots_seed = 4;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "SUN3") = 0)
      #declare glow_type = 5;
      #declare glow_colour = <1.5, 1.2, .3>;
      #declare glow_scale = <1, 1, 1> * 1.3;
      #declare glow_intensity = 1.5;
      #declare rays_type = 7;
      #declare spots_type = 1;
      #declare spots_colour = <1.5, 1.2, .3>;
      #declare spots_frequency = 2;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "UNDERSEA") = 0)
      #declare glow_type = 1;
      #declare glow_colour = <.2, 1.3, .8>;
      #declare rays_type = 8;
      #declare streak_type = 8;
      #declare streak_colour = <.6, 1.1, .9>;
      #declare streak_scale = <1, 1, 1> * .8;
      #declare streak_to_centre = true;
      #declare streak_intensity = .7;
      #declare _LE_created = true; #end

   #if (strcmp(effect_type, "RANDOM") = 0)
      #ifndef (effect_seed) #declare effect_seed = 0; #end
      #declare _LE_rseed = seed(effect_seed);
      #declare effect_colour = <rand(_LE_rseed), rand(_LE_rseed), rand(_LE_rseed)>;
      #declare glow_type = int(rand(_LE_rseed) * 7.9) + 1;
      #declare glow_scale = <1, 1, 1> * (.7 + rand(_LE_rseed) * .6);
      #declare glow_intensity = .7 + rand(_LE_rseed) * .6;
      #declare rays_type = int(rand(_LE_rseed) * 8.9);
      #declare rays_scale = <1, 1, 1> * (.7 + rand(_LE_rseed) * .6);
      #declare rays_intensity = .7 + rand(_LE_rseed) * .6;
      #declare rays_frequency = .8 + rand(_LE_rseed) * 1.2;
      #declare rays_seed = int(rand(_LE_rseed) * 10000);
      #declare streak_type = int(rand(_LE_rseed) * 8.9);
      #declare streak_scale = <1, 1, 1> * (.5 + rand(_LE_rseed));
      #declare streak_intensity = .7 + rand(_LE_rseed) * .6;
      #declare streak_rotate = int(rand(_LE_rseed) * 7.9) * 15;
      #declare streak_seed = int(rand(_LE_rseed) * 10000);
      #declare streak_to_centre = int(rand(_LE_rseed) * 1.25);
      #declare spots_type = int(rand(_LE_rseed) * 8.9);
      #declare spots_scale = <1, 1, 1> * (.7 + rand(_LE_rseed) * .6);
      #declare spots_intensity = .7 + rand(_LE_rseed) * .6;
      #declare spots_frequency = .8 + rand(_LE_rseed) * 1.2;
      #declare spots_seed = int(rand(_LE_rseed) * 10000);
      #declare _LE_created = true; #end

   #if (_LE_created = false)
      #if (strcmp(effect_type, "RAINBOW") != 0)
         #warning "You have not specified a correct effect_type!  Using \"Rainbow\" effect.\r\n" #end
      #declare glow_type = 6;
      #declare glow_scale = <1, 1, 1> * 1.2;
      #declare rays_type = 3;
      #declare streak_type = 6;
      #declare streak_intensity = .3;
      #declare spots_type = 8;
      #declare spots_seed = 10;
      #declare spots_frequency = .8;
      #declare _LE_created = true; #end

// CREATE LENS EFFECT
// ******************
   #if (options_only = false)
      #include "LNSEFCTS.INC"
      global_settings {max_trace_level 20}
   #end
