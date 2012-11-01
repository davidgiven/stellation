/*************************************************************************
         LENS EFFECTS INCLUDE FILE FOR PERSISTENCE OF VISION 3.x
**************************************************************************

Created by Chris Colefax, 18 July 1997
Updated 9 August 1998: updated for POV-Ray 3.1,
                       added spots_contract_to_centre option,
                       fixed Domain Error caused by some compilers

See "LnsEfcts.htm" for more information.

*************************************************************************/

// CHECK CAMERA AND LENS OPTIONS
// *****************************
   #declare _LE_tempver = version; #version 3.0;
   #ifndef (camera_location)  #declare camera_location  = <0, 0, 0>; #end
   #ifndef (camera_sky)       #declare camera_sky       = <0, 1, 0>; #end
   #ifndef (camera_look_at)   #declare camera_look_at   = <0, 0, 1>; #end
   #ifdef  (camera_direction) #declare _LE_camscale     = 1 / vlength(camera_direction * <1, 1, 1>);
      #else #ifdef (camera_angle) #declare _LE_camscale = tan(radians(camera_angle) / 2) * 1.5;
         #else #declare _LE_camscale = 1; #end #end
   #ifndef (lens_scale) #declare lens_scale = 1; #end
   #ifndef (layer_separation) #declare layer_separation = 1; #end

// CHECK EFFECT OPTIONS
// ********************
   #ifndef (effect_location) #declare effect_location = <0, 0, 1>; #end
   #ifdef (effect_colour) #declare _LE_colour = effect_colour;
      #else #ifdef (effect_color) #declare _LE_colour = effect_color;
         #else #declare _LE_colour = <1, 1, 1>; #end #end
   #ifdef (source_colour) #declare _LE_srccolour = source_colour;
      #else #ifdef (source_color) #declare _LE_srccolour = source_color;
         #else #declare _LE_srccolour = <1, 1, 1>; #end #end
   #ifndef (effect_scale) #declare _LE_scale = <1, 1, 1>;
      #else #declare _LE_scale = effect_scale * <1, 1, 1>; #end
   #ifndef (effect_rotate)        #declare effect_rotate        = 0;    #end
   #ifndef (effect_brightness)    #declare effect_brightness    = 1;    #end
   #ifndef (effect_intensity)     #declare effect_intensity     = 1;    #end
   #ifndef (effect_always_on_top) #declare effect_always_on_top = true; #end

   #ifndef (glow_type)          #declare glow_type          = 0; #end
   #ifndef (rays_type)          #declare rays_type          = 0; #end
   #ifndef (streak_type)        #declare streak_type        = 0; #end
   #ifndef (spots_type)         #declare spots_type         = 0; #end
   #ifndef (custom_effect_file) #declare custom_effect_file = "" #end

   #ifndef (debug_options) #declare debug_options = false; #end

// CHECK ANIMATION OPTIONS
// ***********************
   #ifndef (effect_shrink)        #declare effect_shrink        = false; #end
   #ifndef (effect_falloff)       #declare effect_falloff       = 60;    #end
   #ifndef (effect_falloff_power) #declare effect_falloff_power = 1;     #end
   #ifndef (effect_spin)          #declare effect_spin          = 0;     #end
   #ifndef (effect_jitter)        #declare effect_jitter        = 0;     #end
   #ifndef (effect_flicker)       #declare effect_flicker       = 0;     #end
   #ifndef (effect_anim_seed)     #declare effect_anim_seed     = 0;     #end

// CAMERA CALCULATIONS
// *******************
   #declare _LE_camskyangle = <degrees(acos(camera_sky.y / vlength(camera_sky))), 0, 0>;
      #if (camera_sky.x != 0 | camera_sky.z != 0) #declare _LE_camskyangle = _LE_camskyangle + (y * degrees(atan2(camera_sky.x, camera_sky.z))); #end
   #declare _LE_camtolook      = (camera_look_at * <1, 1, 1>) - (camera_location * <1, 1, 1>);
   #declare _LE_camtolook      = vrotate (vrotate (_LE_camtolook, -y * _LE_camskyangle.y), -x * _LE_camskyangle.x);
   #declare _LE_camtolookangle = <-degrees(asin(_LE_camtolook.y / vlength(_LE_camtolook))), 0, 0>;
      #if (_LE_camtolook.x != 0 | _LE_camtolook.z != 0) #declare _LE_camtolookangle = _LE_camtolookangle + (y * degrees(atan2(_LE_camtolook.x, _LE_camtolook.z))); #end

// EFFECT CALCULATIONS
// *******************
   #declare _LE_camtoeffect   = (effect_location * <1, 1, 1>) - (camera_location * <1, 1, 1>);
   #declare _LE_camtoeffect   = vrotate (vrotate (_LE_camtoeffect, -y * _LE_camskyangle.y), -x * _LE_camskyangle.x);
   #declare _LE_effectdist    = vlength(_LE_camtoeffect);
   #declare _LE_releffectloc  = vrotate (vrotate (_LE_camtoeffect, -y * _LE_camtolookangle.y), -x * _LE_camtolookangle.x);
   #declare _LE_degfromcentre = degrees(acos(_LE_releffectloc.z / vlength(_LE_releffectloc)));
   #if (_LE_releffectloc.y != 0 | _LE_releffectloc.x != 0)
      #declare _LE_angletohoriz = degrees(atan2(_LE_releffectloc.y, _LE_releffectloc.x));
   #else
      #declare _LE_angletohoriz = 0;
   #end

// RANDOM SEED CALCULATIONS
// ************************
   #ifndef (rays_seed) #declare rays_seed = 0; #end
   #ifndef (rays_flicker) #declare rays_flicker = false; #end
   #if (rays_flicker = false) #declare _LE_raysrand = seed(rays_seed);
      #else #declare _LE_raysrand = seed(rays_seed + clock * 1e5); #end

   #ifndef (streak_seed) #declare _LE_streakrand = seed(0);
      #else #declare _LE_streakrand = seed(streak_seed); #end

   #ifndef (spots_seed) #declare _LE_spotsrand = seed(0);
      #else #declare _LE_spotsrand = seed(spots_seed); #end

// ANIMATION CALCULATIONS
// **********************
   #if (effect_falloff > 90) #warning "effect_falloff must be less than 90 degrees!\r\n" #declare effect_falloff = 90; #end
   #if (_LE_degfromcentre >= effect_falloff) #declare _LE_intensity = 0;
      #else #declare _LE_intensity = pow(1 - (_LE_degfromcentre / effect_falloff), 1 / effect_falloff_power); #end
   #declare _LE_intensity = _LE_intensity * effect_intensity;
   #if (_LE_intensity = 0) #warning "Lens effect is transparent.  No effect created.\r\n"
      #else #if (glow_type + rays_type + streak_type + spots_type + strlen(custom_effect_file) = 0)
         #warning "You have not specified any lens effect components to use!\r\n" #else

   #ifndef (_LE_animseed) #declare _LE_animseed = seed(effect_anim_seed + (clock * 1e5)); #end
   #declare _LE_colour    = vnormalize (_LE_colour * <1, 1, 1>) * sqrt(3) * effect_brightness;
   #declare _LE_colour    = _LE_colour * (1 + ((rand(_LE_animseed) - .5) * effect_flicker));
   #declare _LE_intensity = _LE_intensity * (1 + ((rand(_LE_animseed) - .5) * effect_flicker));
   #declare _LE_scale     = _LE_scale * (1 + ((rand(_LE_animseed) - .5) * effect_flicker));
   #if (effect_shrink != false) #declare _LE_scale = _LE_scale / _LE_effectdist;
      #else #declare _LE_scale = _LE_scale * <_LE_camscale, _LE_camscale, 1>; #end
   #declare _LE_translate = _LE_releffectloc * (1 / _LE_releffectloc.z);
   #declare _LE_translate = _LE_translate + (<rand(_LE_animseed) - .5, rand(_LE_animseed) - .5, 0> * effect_jitter * .2);
   #declare _LE_rotate    = effect_rotate - (clock * 360 * effect_spin);

// LENS CALCULATIONS
// *****************
   #declare _LE_baseobj  = disc {0, z, .999 finish {ambient 1 diffuse 0 reflection 0 phong 0 specular 0 crand 0}}
   #declare _LE_layersep = layer_separation * 1e-3;

   #if (effect_always_on_top != false)
      #ifndef (_LE_lensoffset) #declare _LE_lensoffset = lens_scale * 1e-2; #end
      #declare _LE_lensscale = _LE_lensoffset;
   #else
      #ifndef (_LE_prevdist) #declare _LE_lensscale = _LE_effectdist;
         #else #if (_LE_releffectloc.z = _LE_prevdist)
            #declare _LE_lensscale = _LE_effectdist + _LE_prevlayer;
               #else #declare _LE_lensscale = _LE_effectdist;
   #end #end #end

// CREATE LENS EFFECT
// ******************
   union {
   #if (debug_options != false)
      #debug "// LENS EFFECTS INCLUDE FILE OPTIONS\r\n"
      #debug concat("   #declare effect_colour = <", str(_LE_colour.x, 0, 3), ", ", str(_LE_colour.y, 0, 3), ", ", str(_LE_colour.z, 0, 3), ">;\r\n")
      #debug concat("   #declare source_colour = <", str(_LE_srccolour.x, 0, 3), ", ", str(_LE_srccolour.y, 0, 3), ", ", str(_LE_srccolour.z, 0, 3), ">;\r\n")
      #ifdef (effect_scale) #debug concat("   #declare effect_scale = <", str((effect_scale * <1, 1, 1>).x, 0, 3), ", ", str((effect_scale * <1, 1, 1>).y, 0, 3), ", ", str((effect_scale * <1, 1, 1>).z, 0, 3), ">;\r\n") #end
      #debug concat("   #declare effect_rotate = ", str(effect_rotate, 0, 3), ";\r\n")
      #debug concat("   #declare effect_brightness = ", str(effect_brightness, 0, 3), ";\r\n")
      #debug concat("   #declare effect_intensity = ", str(effect_intensity, 0, 3), ";\r\n")
      #debug "   #declare effect_always_on_top = " #if (effect_always_on_top = false) #debug "false" #else #debug "true" #end #debug ";\r\n"
   #end

// GLOW OPTIONS
// ************
   #ifdef (glow_colour) #declare _LE_gcolour = glow_colour;
      #else #ifdef (glow_color) #declare _LE_gcolour = glow_color;
         #else #declare _LE_gcolour = <1, 1, 1>; #end #end
   #ifndef (glow_scale) #declare _LE_glowscale = _LE_scale;
      #else #declare _LE_glowscale = glow_scale * _LE_scale; #end
   #ifndef (glow_rotate) #declare glow_rotate = 0; #end
   #declare _LE_srotate = z * (_LE_rotate + glow_rotate);
   #ifndef (glow_intensity) #declare _LE_glowint = _LE_intensity;
      #else #declare _LE_glowint = _LE_intensity * glow_intensity; #end

   #if (debug_options != false) #debug "\r\n"
      #debug concat("   #declare glow_type = ", str(glow_type, 0, 0), ";\r\n")
      #debug concat("   #declare glow_colour = <", str(_LE_gcolour.x, 0, 3), ", ", str(_LE_gcolour.y, 0, 3), ", ", str(_LE_gcolour.z, 0, 3), ">;\r\n")
      #debug concat("   #declare glow_rotate = ", str(glow_rotate, 0, 3), ";\r\n")
      #ifdef (glow_scale) #debug concat("   #declare glow_scale = <", str((glow_scale * <1, 1, 1>).x, 0, 3), ", ", str((glow_scale * <1, 1, 1>).y, 0, 3), ", ", str((glow_scale * <1, 1, 1>).z, 0, 3), ">;\r\n") #end
      #debug concat("   #declare glow_intensity = ", str(glow_intensity, 0, 3), ";\r\n")
   #end

   #declare _LE_gcolour = _LE_gcolour * _LE_colour;

// CREATE GLOW
// ***********
   #switch (glow_type) #case (0) #break

      #case (1) object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [1 rgb _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (2) object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.3 rgb <1.1, 1, .8> * _LE_gcolour transmit pow(.3, _LE_glowint)]
            [1 rgb <1.1, .8, .8> * _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (3) object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.45 rgb <.8, 1, 1.1> * _LE_gcolour transmit pow(.6, _LE_glowint)]
            [.46 rgb <.8, 1, 1.1> * _LE_gcolour transmit pow(.5, _LE_glowint)]
            [.47 rgb <.9, 1, 1.1> * _LE_gcolour transmit pow(.6, _LE_glowint)]
            [1 rgb <.9, 1, 1.1> * _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (4) object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.41 rgb <1, .9, .9> * _LE_gcolour transmit pow(.6, _LE_glowint)]
            [.44 rgb <1.1, .8, .8> * _LE_gcolour transmit pow(.5, _LE_glowint)]
            [.46 rgb <1.1, .8, .8> * _LE_gcolour transmit pow(.5, _LE_glowint)]
            [.49 rgb <1, .9, .9> * _LE_gcolour transmit pow(.7, _LE_glowint)]
            [1 rgb _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (5) object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.85 rgb _LE_gcolour transmit 1]
            [.9 rgb _LE_gcolour transmit pow(.9, _LE_glowint)]
            [.92 rgb _LE_gcolour * 1.2 transmit pow(.8, _LE_glowint)]
            [.95 rgb _LE_gcolour transmit pow(.9, _LE_glowint)]
            [1 rgb _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .2 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (6) object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.8 rgb _LE_gcolour transmit 1]
            [.85 rgb <1.2, .3, .3> * _LE_gcolour transmit 1]
            [.91 rgb <1.2, .3, .3> * _LE_gcolour transmit pow(.9, _LE_glowint)]
            [.94 rgb <1.2, 1.2, .3> * _LE_gcolour transmit pow(.9, _LE_glowint)]
            [.96 rgb <.3, 1.2, .6> * _LE_gcolour transmit pow(.9, _LE_glowint)]
            [.97 rgb <.3, .6, 1.2> * _LE_gcolour transmit pow(.9, _LE_glowint)]
            [.98 rgb <1.2, .3, 1.2> * _LE_gcolour transmit pow(.9, _LE_glowint)]
            [1 rgb <1.2, .3, 1.2> * _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .2 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (7) object {_LE_baseobj pigment {onion pigment_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.4 radial color_map {
               [0 rgb <1.1, 1.1, .4> * _LE_gcolour transmit pow(.4, _LE_glowint)]
               [1/3 rgb <.4, 1.1, 1.1> * _LE_gcolour transmit pow(.4, _LE_glowint)]
               [2/3 rgb <1.1, .4, 1.1> * _LE_gcolour transmit pow(.4, _LE_glowint)]
               [1 rgb <1.1, 1.1, .4> * _LE_gcolour transmit pow(.4, _LE_glowint)]}
               rotate x*90]
            [.8 radial color_map {
               [0 rgb <1.1, 1.1, .4> * _LE_gcolour transmit pow(.8, _LE_glowint)]
               [1/3 rgb <.4, 1.1, 1.1> * _LE_gcolour transmit pow(.8, _LE_glowint)]
               [2/3 rgb <1.1, .4, 1.1> * _LE_gcolour transmit pow(.8, _LE_glowint)]
               [1 rgb <1.1, 1.1, .4> * _LE_gcolour transmit pow(.8, _LE_glowint)]}
               rotate x*90]
            [1 rgb _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (8) object {_LE_baseobj pigment {onion pigment_map {
            [0 rgb 1.2 * _LE_srccolour transmit pow(.1, _LE_glowint)]
            [.4 radial color_map {
               [0 rgb 1.1 * _LE_gcolour transmit pow(.4, _LE_glowint)]
               [1 rgb <1, 1, 1.1> * _LE_gcolour transmit pow(.4, _LE_glowint)]}
               sine_wave frequency 7 rotate x * 90]
            [1 rgb <1, .7, 1.2> * _LE_gcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_glowscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #else
         #warning "Invalid glow_type specified.  No glow created.\r\n"
      #end

// RAY OPTIONS
// ***********
   #ifdef (rays_colour) #declare _LE_rcolour = rays_colour; 
      #else #ifdef (rays_color) #declare _LE_rcolour = rays_color;
         #else #declare _LE_rcolour = <1, 1, 1>; #end #end
   #ifndef (rays_scale) #declare _LE_raysscale = _LE_scale;
      #else #declare _LE_raysscale = rays_scale * _LE_scale; #end
   #ifndef (rays_rotate) #declare rays_rotate = 0; #end
   #declare _LE_srotate = z * (_LE_rotate + rays_rotate);
   #ifndef (rays_intensity) #declare _LE_raysint = _LE_intensity;
      #else #declare _LE_raysint = _LE_intensity * rays_intensity; #end
   #ifndef (rays_frequency) #declare rays_frequency = 1; #end
   #if (rays_frequency <= 0) #warning "rays_frequency must be larger than 0!\r\n" #declare rays_frequency = 1; #end

   #if (debug_options != false) #debug "\r\n"
      #debug concat("   #declare rays_type = ", str(rays_type, 0, 0), ";\r\n")
      #debug concat("   #declare rays_colour = <", str(_LE_rcolour.x, 0, 3), ", ", str(_LE_rcolour.y, 0, 3), ", ", str(_LE_rcolour.z, 0, 3), ">;\r\n")
      #debug concat("   #declare rays_rotate = ", str(rays_rotate, 0, 3), ";\r\n")
      #ifdef (rays_scale) #debug concat("   #declare rays_scale = <", str((rays_scale * <1, 1, 1>).x, 0, 3), ", ", str((rays_scale * <1, 1, 1>).y, 0, 3), ", ", str((rays_scale * <1, 1, 1>).z, 0, 3), ">;\r\n") #end
      #debug concat("   #declare rays_intensity = ", str(rays_intensity, 0, 3), ";\r\n")
      #debug concat("   #declare rays_seed = ", str(rays_seed, 0, 0), ";\r\n")
      #debug concat("   #declare rays_frequency = ", str(rays_frequency, 0, 3), ";\r\n")
   #end

// CREATE RAYS
// ***********
   #switch (rays_type) #case (0) #break

      #case (1) #declare _LE_tmpvar = rand(_LE_raysrand) * 3; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion color_map {
               [0 rgb _LE_rcolour transmit pow(.95, _LE_raysint)]
               [1 rgb _LE_rcolour transmit 1]}}
            translate y
            scale <.04, 1, 1> * (1 + (<rand(_LE_raysrand), rand(_LE_raysrand), 1> - .5) * 2)
            rotate z * _LE_tmpvar scale .1 * _LE_raysscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale}
         #declare _LE_tmpvar = _LE_tmpvar + (4 / rays_frequency); #end
         #declare _LE_lensscale = _LE_lensscale + _LE_layersep; #break

      #case (2) #declare _LE_tmpvar = rand(_LE_raysrand) * 10; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion color_map {
               [0 rgb _LE_rcolour transmit pow(.8 + rand(_LE_raysrand) * .1, _LE_raysint)]
               [1 rgb _LE_colour * _LE_rcolour transmit 1]}          
               scallop_wave scale 2}
            translate y scale <.06, 1, 1> * (1 + (rand(_LE_raysrand) - .5) * .6)
            rotate z * _LE_tmpvar scale .15 * _LE_raysscale
            rotate _LE_srotate translate _LE_translate scale _LE_lensscale}
         #declare _LE_tmpvar = _LE_tmpvar + ((20 + rand(_LE_raysrand) * 10) / rays_frequency); #end
         #declare _LE_lensscale = _LE_lensscale + _LE_layersep; #break

      #case (3) #declare _LE_tmpvar = rand(_LE_raysrand) * 10; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion color_map {
               [0 rgb _LE_rcolour transmit pow(.95, _LE_raysint)]
               [1 rgb _LE_rcolour transmit 1]}}
            translate y * 1.1 scale <.1, 1, 1> * (1 + (rand(_LE_raysrand) - .5) * .7)
            rotate z * _LE_tmpvar scale .12 * _LE_raysscale
            rotate _LE_srotate translate _LE_translate scale _LE_lensscale}
         #declare _LE_tmpvar = _LE_tmpvar + (18 / rays_frequency); #end
         #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
      #declare _LE_tmpvar = rand(_LE_raysrand) * 20; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion color_map {
               [0 rgb _LE_rcolour transmit pow(.95, _LE_raysint)]
               [1 rgb _LE_rcolour transmit 1]}}
            translate y * 1.1 scale <.15, 1, 1> * (1 + (rand(_LE_raysrand) - .5) * .7)
            rotate z * _LE_tmpvar scale .12 * _LE_raysscale
            rotate _LE_srotate translate _LE_translate scale _LE_lensscale}
         #declare _LE_tmpvar = _LE_tmpvar + (24 / rays_frequency); #end
         #declare _LE_lensscale = _LE_lensscale + _LE_layersep; #break

      #case (4) #declare _LE_tmpvar = rand(_LE_raysrand) * 30; #while (_LE_tmpvar < 180)
         object {_LE_baseobj pigment {onion color_map {
               [0 rgb _LE_srccolour * _LE_rcolour transmit pow(.8, _LE_raysint)]
               [1 rgb _LE_colour * _LE_rcolour transmit 1]}
               scallop_wave scale 2}
            scale <.2, 1, 1> * (1 + (rand(_LE_raysrand) - .5) * .3)
            rotate z * _LE_tmpvar scale .35 * _LE_raysscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
            } #declare _LE_tmpvar = _LE_tmpvar + (36 / rays_frequency); #end
         #break

      #case (5) #declare _LE_tmpvar = rand(_LE_raysrand) * 5; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion pigment_map {
               [0 rgb _LE_colour * _LE_rcolour * (.3 + <rand(_LE_raysrand), rand(_LE_raysrand), rand(_LE_raysrand)>)
                  transmit pow(.8 + rand(_LE_raysrand) * .15, _LE_raysint)]
               [1 rgb _LE_colour * _LE_rcolour transmit 1]}
               scallop_wave scale 2}
            translate y scale <.03, 1, 1> * (1 + (rand(_LE_raysrand) - .5) * .7)
            rotate z * _LE_tmpvar scale .13 * _LE_raysscale
            rotate _LE_srotate translate _LE_translate scale _LE_lensscale}
         #declare _LE_tmpvar = _LE_tmpvar + (6 / rays_frequency); #end
         #declare _LE_lensscale = _LE_lensscale + _LE_layersep; #break

      #case (6) object {_LE_baseobj pigment {onion pigment_map {
            [0 radial color_map {
               [0 rgb _LE_rcolour transmit pow(.8 _LE_raysint)]
               [.8 rgb _LE_colour * _LE_rcolour transmit 1]}
               sine_wave frequency int(9 * rays_frequency) rotate <90, 0, rand(_LE_raysrand) * 360>]
            [1 rgb _LE_colour * _LE_rcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_raysscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (7) object {_LE_baseobj pigment {onion pigment_map {
            [.2 radial color_map {
               [0 rgb _LE_rcolour transmit pow(.75, _LE_raysint)]
               [.4 rgb _LE_colour * _LE_rcolour transmit 1]
               [.6 rgb _LE_rcolour transmit pow(.95, _LE_raysint)]
               [.8 rgb _LE_colour * _LE_rcolour transmit 1]
               [1 rgb _LE_rcolour transmit pow(.85, _LE_raysint)]}
               triangle_wave frequency int(9 * rays_frequency) rotate <90, 0, rand(_LE_raysrand) * 360>]
            [1 rgb _LE_colour * _LE_rcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_raysscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (8) object {_LE_baseobj pigment {onion pigment_map {
            [.5 radial color_map {
               [0 rgb _LE_rcolour transmit pow(.9, _LE_raysint)]
               [1 rgb _LE_colour * _LE_rcolour transmit 1]}
               triangle_wave frequency int((100 + rand(_LE_raysrand) * 20) * rays_frequency)
               rotate <90, 0, rand(_LE_raysrand) * 360>]
            [1 rgb _LE_colour * _LE_rcolour transmit 1]}
            scallop_wave scale 2}
         scale .3 * _LE_raysscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #else
         #warning "Invalid rays_type specified.  No rays created.\r\n"
      #end

// STREAK OPTIONS
// **************
   #ifdef (streak_colour) #declare _LE_stcolour = streak_colour;
      #else #ifdef (streak_color) #declare _LE_stcolour = streak_color;
         #else #declare _LE_stcolour = <1, 1, 1>; #end #end
   #ifndef (streak_scale) #declare _LE_streakscale = <1, 1, 1>;
      #else #declare _LE_streakscale = streak_scale * <1, 1, 1>; #end
      #declare _LE_streakscale = _LE_streakscale * _LE_scale;
   #ifndef (streak_rotate) #declare streak_rotate = 0; #end
   #ifndef (streak_intensity) #declare _LE_streakint = _LE_intensity;
      #else #declare _LE_streakint = _LE_intensity * streak_intensity; #end
   #ifdef (streak_to_centre) #declare _LE_strtocentre = streak_to_centre;
      #else #ifdef (streak_to_center) #declare _LE_strtocentre = streak_to_center;
         #else #declare _LE_strtocentre = false; #end #end
   #if (_LE_strtocentre = false) #declare _LE_srotate = z * (_LE_rotate + streak_rotate);
      #else #declare _LE_srotate = z * (_LE_angletohoriz + streak_rotate); #end

   #if (debug_options != false) #debug "\r\n"
      #debug concat("   #declare streak_type = ", str(streak_type, 0, 0), ";\r\n")
      #debug concat("   #declare streak_colour = <", str(_LE_stcolour.x, 0, 3), ", ", str(_LE_stcolour.y, 0, 3), ", ", str(_LE_stcolour.z, 0, 3), ">;\r\n")
      #debug concat("   #declare streak_rotate = ", str(streak_rotate, 0, 3), ";\r\n")
      #ifdef (streak_scale) #debug concat("   #declare streak_scale = <", str((streak_scale * <1, 1, 1>).x, 0, 3), ", ", str((streak_scale * <1, 1, 1>).y, 0, 3), ", ", str((streak_scale * <1, 1, 1>).z, 0, 3), ">;\r\n") #end
      #debug concat("   #declare streak_intensity = ", str(streak_intensity, 0, 3), ";\r\n")
      #debug concat("   #declare streak_seed = ", str(streak_seed, 0, 0), ";\r\n")
      #debug "   #declare streak_to_centre = " #if (_LE_strtocentre = false) #debug "false" #else #debug "true" #end #debug ";\r\n"
   #end

// CREATE STREAK
// *************
   #switch (streak_type) #case (0) #break

      #case (1) object {_LE_baseobj pigment {onion color_map {
            [.4 rgb 1.1 * _LE_srccolour * _LE_stcolour transmit pow(.7, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2}
         scale <.5, .04, 1> rotate z * -45
         scale _LE_streakscale rotate _LE_srotate
         translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
         } #break

      #case (2) #declare _LE_tmpobj = object {_LE_baseobj
         pigment {onion color_map {
            [0 rgb 1.1 * _LE_srccolour * _LE_stcolour transmit pow(.1, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2} scale <.01, .4, 1>}
         object {_LE_tmpobj scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         object {_LE_tmpobj scale 1.2 rotate z * -7
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #break

      #case (3) #declare _LE_tmpobj = object {_LE_baseobj
         pigment {onion color_map {
            [.1 rgb 1.1 * _LE_srccolour * _LE_stcolour transmit pow(.1, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2} translate y * .3 scale <.02, .35, 1>}
         object {_LE_tmpobj
            rotate z * ((rand(_LE_streakrand) - .5) * 60)
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         object {_LE_tmpobj
            rotate z * (120 + (rand(_LE_streakrand) - .5) * 60)
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         object {_LE_tmpobj
            rotate z * (240 + (rand(_LE_streakrand) - .5) * 60)
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #break

      #case (4) #declare _LE_tmpobj = object {_LE_baseobj
         pigment {onion color_map {
            [.4 rgb _LE_srccolour * _LE_stcolour transmit pow(.4, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2} scale <.5, .02, 1>}
         object {_LE_tmpobj rotate z * 45
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         object {_LE_tmpobj rotate z * -45
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #break

      #case (5) #declare _LE_tmpvar = 0; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.1 * _LE_srccolour * _LE_stcolour transmit pow(.1, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2}
            translate y * .2 scale <.02, .35, 1> rotate z * _LE_tmpvar
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + 72; #end
         #break

      #case (6) #declare _LE_tmpvar = 0; #while (_LE_tmpvar < 360)
         object {_LE_baseobj pigment {onion color_map {
            [0 rgb 1.1 * _LE_srccolour * _LE_stcolour transmit pow(.1, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2}
            scale <.02, .45, 1> rotate z * _LE_tmpvar
            scale _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + 120; #end #break

      #case (7) object {_LE_baseobj pigment {onion color_map {
               [0 rgb _LE_srccolour * _LE_stcolour transmit pow(.3, _LE_streakint)]
               [1 rgb _LE_colour * _LE_stcolour transmit 1]}
               scallop_wave scale 2}
            scale <1.3, .04, 1> * _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #break

      #case (8) #declare _LE_tmpobj = object {_LE_baseobj
         pigment {onion color_map {
            [0 rgb 1.1 * _LE_srccolour * _LE_stcolour transmit pow(.2, _LE_streakint)]
            [1 rgb _LE_colour * _LE_stcolour transmit 1]}
            scallop_wave scale 2}}
         object {_LE_tmpobj scale <.02, .5, 1> * _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         object {_LE_tmpobj scale <.8, .02, 1> * _LE_streakscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #break

      #else
         #warning "Invalid streak_type specified.  No streak created.\r\n"
      #end

// SPOT OPTIONS
// ************
   #ifdef (spots_colour) #declare _LE_spcolour = spots_colour;
      #else #ifdef (spots_color) #declare _LE_spcolour = spots_color;
         #else #declare _LE_spcolour = <1, 1, 1>; #end #end
   #ifndef (spots_scale) #declare _LE_spotsscale = <1, 1, 1>;
      #else #declare _LE_spotsscale = spots_scale * <1, 1, 1>; #end
      #declare _LE_spotsscale = _LE_spotsscale * _LE_scale / _LE_camscale;
   #ifndef (spots_rotate) #declare spots_rotate = 0; #end
   #ifndef (spots_intensity) #declare _LE_spotsint = _LE_intensity;
      #else #declare _LE_spotsint = _LE_intensity * spots_intensity; #end
   #ifndef (spots_frequency) #declare spots_frequency = 1; #end
   #if (spots_frequency <= 0) #warning "spots_frequency must be larger than 0!\r\n" #declare spots_frequency = 1; #end
   #ifndef (spots_spacing) #declare spots_spacing = 1; #end
   #ifdef (spots_to_centre) #declare _LE_sptocentre = spots_to_centre;
      #else #ifdef (spots_to_center) #declare _LE_sptocentre = spots_to_center;
         #else #declare _LE_sptocentre = true; #end #end
   #ifdef (spots_contract_to_centre) #declare _LE_sptcontract = spots_contract_to_centre;
      #else #ifdef (spots_contract_to_center) #declare _LE_sptcontract = spots_contract_to_center;
         #else #declare _LE_sptcontract = false; #end #end

   #if (_LE_sptocentre = false)
      #declare _LE_srotate = z * (_LE_rotate + spots_rotate);
      #declare _LE_sspacing = spots_spacing;
   #else
      #declare _LE_srotate = z * (_LE_angletohoriz + spots_rotate);
      #if (_LE_sptcontract = false)
         #declare _LE_sspacing = spots_spacing * (1 + vlength(_LE_translate * <1, 1, 0>));
      #else
         #declare _LE_sspacing = pow(spots_spacing * vlength(_LE_translate * <1, 1, 0>) * 5, .5);
      #end
   #end

   #if (debug_options != false) #debug "\r\n"
      #debug concat("   #declare spots_type = ", str(spots_type, 0, 0), ";\r\n")
      #debug concat("   #declare spots_colour = <", str(_LE_spcolour.x, 0, 3), ", ", str(_LE_spcolour.y, 0, 3), ", ", str(_LE_spcolour.z, 0, 3), ">;\r\n")
      #debug concat("   #declare spots_rotate = ", str(spots_rotate, 0, 3), ";\r\n")
      #ifdef (spots_scale) #debug concat("   #declare spots_scale = <", str((spots_scale * <1, 1, 1>).x, 0, 3), ", ", str((spots_scale * <1, 1, 1>).y, 0, 3), ", ", str((spots_scale * <1, 1, 1>).z, 0, 3), ">;\r\n") #end
      #debug concat("   #declare spots_intensity = ", str(spots_intensity, 0, 3), ";\r\n")
      #debug concat("   #declare spots_seed = ", str(spots_seed, 0, 0), ";\r\n")
      #debug concat("   #declare spots_frequency = ", str(spots_frequency, 0, 3), ";\r\n")
      #debug concat("   #declare spots_spacing = ", str(spots_spacing, 0, 3), ";\r\n")
      #debug "   #declare spots_to_centre = " #if (_LE_sptocentre = false) #debug "false" #else #debug "true" #end #debug ";\r\n"
      #debug "   #declare spots_contract_to_center = " #if (_LE_sptcontract = false) #debug "false" #else #debug "true" #end #debug ";\r\n"
      #debug "\r\n\r\n"
   #end

// CREATE SPOTS
// ************
   #switch (spots_type) #case (0) #break

      #case (1) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #declare _LE_tmpcol = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .6 + .5) * _LE_colour * _LE_spcolour;
            #switch (rand(_LE_spotsrand))
               #range (0, .65) pigment {onion color_map {
                  [0 + rand(_LE_spotsrand) * .5 rgb _LE_tmpcol transmit pow(.8 + rand(_LE_spotsrand) * .2, _LE_spotsint)]
                  [.6 + rand(_LE_spotsrand) * .38 rgb _LE_tmpcol transmit pow(.8 + rand(_LE_spotsrand) * .2, _LE_spotsint)]
                  [1 rgb _LE_tmpcol transmit 1]}}
                  scale _LE_spotsscale * (.02 + rand(_LE_spotsrand) * .1)
                  #break
               #range (.65, .9) pigment {onion color_map {
                  [0 rgb _LE_tmpcol * 1.7 transmit pow(.1, _LE_spotsint)]
                  [1 rgb _LE_tmpcol * 1.5 transmit 1]}
                  scallop_wave scale 2}
                  scale _LE_spotsscale * (.005 + rand(_LE_spotsrand) * .01)
                  #break
               #range (.9, 1)
                  pigment {rgb _LE_tmpcol transmit pow(.95, _LE_spotsint)}
                  scale _LE_spotsscale * (.15 + rand(_LE_spotsrand) * .3)
               #end
            translate x * _LE_tmpvar * _LE_sspacing
            scale _LE_camscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + ((.01 + rand(_LE_spotsrand) * .14) / spots_frequency);
         #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #case (2) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #switch (rand(_LE_spotsrand))
               #range (0, .7) pigment {onion color_map {
                  [0 rgb 1.3 * _LE_srccolour * _LE_spcolour transmit pow(.5 + rand(_LE_spotsrand) * .4, _LE_spotsint)]
                  [1 rgb _LE_colour * _LE_spcolour transmit 1]}
                  scallop_wave scale 2}
                  scale _LE_spotsscale * (.05 + rand(_LE_spotsrand) * .1)
                  #break
               #range (.7, 1) pigment {onion color_map {
                  [0 rgb _LE_srccolour * _LE_spcolour * 1.2 transmit pow(.1, _LE_spotsint)]
                  [1 rgb _LE_colour * _LE_spcolour transmit 1]}
                  scallop_wave scale 2}
                  scale _LE_spotsscale * (.01 + rand(_LE_spotsrand) * .02)
               #end
            translate x * _LE_tmpvar * _LE_sspacing
            scale _LE_camscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + ((.05 + rand(_LE_spotsrand) * .05) / spots_frequency);
         #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #case (3) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #switch (rand(_LE_spotsrand))
               #range (0, .5)
                  pigment {gradient y triangle_wave translate y/5 pigment_map {
                     [.4 gradient y triangle_wave translate y/5 pigment_map {
                        [.4 gradient y triangle_wave translate y/5 color_map {
                           [.4 rgb _LE_srccolour * _LE_spcolour transmit pow(.6 + rand(_LE_spotsrand) * .2, _LE_spotsint)]
                           [.5 rgb _LE_colour * _LE_spcolour transmit 1]} rotate z * 120]
                        [.5 rgbt 1]} rotate z * 120]
                     [.5 rgbt 1]} translate <.05, -.1, 0> scale 3.5}
                  rotate z * 15 scale _LE_spotsscale * abs(_LE_tmpvar) * .12
                  #break
               #range (.5, .8)
                  pigment {onion pigment_map {
                     [.5 radial color_map {
                        [0 rgb <1.2, 1.2, .4> * _LE_colour * _LE_spcolour transmit pow(.8, _LE_spotsint)]
                        [1/3 rgb <.4, 1.2, 1.2> * _LE_colour * _LE_spcolour transmit pow(.8, _LE_spotsint)]
                        [2/3 rgb <1.2, .4, 1.2> * _LE_colour * _LE_spcolour transmit pow(.8, _LE_spotsint)]
                        [1 rgb <1.2, 1.2, .4> * _LE_colour * _LE_spcolour transmit pow(.8, _LE_spotsint)]}
                        rotate x*90]
                     [1 rgb _LE_colour * _LE_spcolour transmit 1]}}
                  rotate z * rand(_LE_spotsrand) * 360
                  scale _LE_spotsscale * (.02 + rand(_LE_spotsrand) * .1)
                  #break
               #range (.8, 1)
                  pigment {onion color_map {
                     [0 rgb 1.3 transmit pow(.1, _LE_spotsint)]
                     [1 rgb _LE_colour * _LE_spcolour * 1.3 transmit 1]}
                     scallop_wave scale 2}
                  scale _LE_spotsscale * (.01 + rand(_LE_spotsrand) * .01)
               #end
            rotate -_LE_srotate
            translate x * _LE_tmpvar * _LE_sspacing
            scale _LE_camscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + ((.04 + rand(_LE_spotsrand) * .04) / spots_frequency);
         #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #case (4) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #declare _LE_tmpcol = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .4 + .7) * _LE_colour * _LE_spcolour;
               pigment {gradient x triangle_wave pigment_map {
                  [.4 gradient x triangle_wave color_map {
                     [.4 rgb _LE_tmpcol transmit pow (.8 + rand(_LE_spotsrand) * .1, _LE_spotsint)]
                     [.5 rgb _LE_tmpcol transmit 1]} rotate z * 90]
                  [.5 rgb _LE_tmpcol transmit 1]} scale 3}
               rotate z * 45
               scale <1, .7, 1> * _LE_spotsscale * (abs(_LE_tmpvar) * .15 + rand(_LE_spotsrand) * .02)
               translate x * _LE_tmpvar * _LE_sspacing
               scale _LE_camscale rotate _LE_srotate
               translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
            #if (rand(_LE_spotsrand) < .5) object {_LE_baseobj
               pigment {onion color_map {
                  [0 rgb _LE_tmpcol * 1.2 transmit pow(.3, _LE_spotsint)]
                  [1 rgb _LE_tmpcol * 1.1 transmit 1]}
                  scallop_wave scale 2}
               scale _LE_spotsscale * (.005 + rand(_LE_spotsrand) * .01)
               translate x * (_LE_tmpvar + (rand(_LE_spotsrand) - .5) * .1) * _LE_sspacing
               scale _LE_camscale rotate _LE_srotate
               translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
            #end
            #declare _LE_tmpvar = _LE_tmpvar + ((.1 + rand(_LE_spotsrand) * .02) / spots_frequency);
            #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #case (5) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #declare _LE_tmpcol = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .4 + .7) * _LE_colour * _LE_spcolour;
            #switch (rand(_LE_spotsrand))
               #range (0, .6) pigment {onion color_map {
                  [0 rgb _LE_tmpcol transmit pow(.9, _LE_spotsint)]
                  [1 rgb _LE_colour * _LE_spcolour transmit 1]}}
                  scale _LE_spotsscale * (.05 + rand(_LE_spotsrand) * .06)
                  #break
               #range (.6, 1) pigment {onion color_map {
                  [.7 rgb _LE_colour * _LE_spcolour transmit 1]
                  [.8 rgb _LE_tmpcol * 1.2 transmit pow(.8 + rand(_LE_spotsrand) * .1, _LE_spotsint)]
                  [1 rgb _LE_colour * _LE_spcolour transmit 1]}}
                  scale _LE_spotsscale * (.05 + rand(_LE_spotsrand) * .08)
                  rotate -_LE_srotate
               #end
            translate x * _LE_tmpvar * _LE_sspacing
            scale _LE_camscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + ((.05 + rand(_LE_spotsrand) * .05) / spots_frequency);
         #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break
             
      #case (6) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #declare _LE_tmpcol = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .5 + .7) * _LE_colour * _LE_spcolour;
            #switch (rand(_LE_spotsrand))
               #range (0, .4) pigment {onion pigment_map {
                     [rand(_LE_spotsrand) * .5 rgb _LE_tmpcol transmit pow(.8 + rand(_LE_spotsrand) * .15, _LE_spotsint)]
                     [1 gradient x triangle_wave pigment_map {
                        [.45 gradient x triangle_wave pigment_map {
                           [.45 gradient x triangle_wave color_map {
                              [.45 rgb _LE_tmpcol transmit pow (.8 + rand(_LE_spotsrand) * .1, _LE_spotsint)]
                              [.5 rgb _LE_tmpcol transmit 1]} rotate z * 120]
                           [.5 rgb _LE_tmpcol transmit 1]} rotate z * 120]
                        [.5 rgb _LE_tmpcol transmit 1]} scale 3]}}
                  scale _LE_spotsscale * (.02 + abs(_LE_tmpvar) * .1)
                  #break
               #range (.4, .7)
                  pigment {onion color_map {
                     [0 + rand(_LE_spotsrand) * .5 rgb _LE_tmpcol transmit pow(.9 + rand(_LE_spotsrand) * .05, _LE_spotsint)]
                     [.6 + rand(_LE_spotsrand) * .38 rgb _LE_tmpcol transmit pow(.9 + rand(_LE_spotsrand) * .05, _LE_spotsint)]
                     [1 rgb _LE_tmpcol transmit 1]}}
                  scale _LE_spotsscale * (.05 + rand(_LE_spotsrand) * .1)
                  #break
               #range (.7, 1)
                  pigment {onion color_map {
                     [0 rgb _LE_tmpcol * 1.7 transmit pow(.1, _LE_spotsint)]
                     [1 rgb _LE_tmpcol * 1.5 transmit 1]}
                     scallop_wave scale 2}
                  scale _LE_spotsscale * (.005 + rand(_LE_spotsrand) * .015)
               #end
            translate x * _LE_tmpvar * _LE_sspacing
            scale _LE_camscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + ((.05 + rand(_LE_spotsrand) * .05) / spots_frequency);
         #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #case (7) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #declare _LE_tmpcol = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .6 + .5) * _LE_colour * _LE_spcolour;
               pigment {onion color_map {
                  [.3 rgb _LE_tmpcol * 1.2 transmit pow(.1, _LE_spotsint)]
                  [1 rgb _LE_tmpcol transmit 1]}
                  scallop_wave scale 2}
               scale _LE_spotsscale * (.01 + rand(_LE_spotsrand) * .03)
               translate x * _LE_tmpvar * _LE_sspacing
               scale _LE_camscale rotate _LE_srotate
               translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
            #declare _LE_tmpvar = _LE_tmpvar + ((.005 + rand(_LE_spotsrand) * .2) / spots_frequency);
            #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #case (8) #declare _LE_tmpvar = -1; #while (_LE_tmpvar < 1)
         object {_LE_baseobj
            #declare _LE_tmpcol = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .6 + .5) * _LE_colour * _LE_spcolour;
            #declare _LE_tmpcol2 = (<rand(_LE_spotsrand), rand(_LE_spotsrand), rand(_LE_spotsrand)> * .7 + .6) * _LE_colour * _LE_spcolour;
            #switch (rand(_LE_spotsrand))
               #range (0, .8) pigment {onion color_map {
                  [0 + rand(_LE_spotsrand) * .6 rgb _LE_tmpcol transmit pow(.85 + rand(_LE_spotsrand) * .1, _LE_spotsint)]
                  [.7 + rand(_LE_spotsrand) * .28 rgb _LE_tmpcol2 transmit pow(.8 + rand(_LE_spotsrand) * .15, _LE_spotsint)]
                  [1 rgb _LE_tmpcol transmit 1]}}
                  scale _LE_spotsscale * (.03 + rand(_LE_spotsrand) * .1)
                  #break
               #range (.8, 1) pigment {onion color_map {
                  [0 rgb _LE_tmpcol * 1.7 transmit pow(.1, _LE_spotsint)]
                  [1 rgb _LE_tmpcol * 1.5 transmit 1]}
                  scallop_wave scale 2}
                  scale _LE_spotsscale * (.005 + rand(_LE_spotsrand) * .015)
               #end
            translate x * _LE_tmpvar * _LE_sspacing
            scale _LE_camscale rotate _LE_srotate
            translate _LE_translate scale _LE_lensscale #declare _LE_lensscale = _LE_lensscale + _LE_layersep;}
         #declare _LE_tmpvar = _LE_tmpvar + ((.005 + rand(_LE_spotsrand) * .1) / spots_frequency);
         #if (_LE_tmpvar > -.15 & _LE_tmpvar < .15) #declare _LE_tmpvar = .15; #end
         #end #break

      #else
         #warning "Invalid spots_type specified.  No spots created.\r\n"
      #end

// INCLUDE CUSTOM EFFECT FILE
// **************************
   #if (strlen(custom_effect_file) > 0)
      #if (file_exists(custom_effect_file))
         #include custom_effect_file
         #declare _LE_lensscale = _LE_lensscale + _LE_layersep;
      #else
         #warning concat("Could not find custom_effect_file \"", custom_effect_file, "\"!\r\n")
      #end
   #end

// POSITION LENS EFFECT
// ********************
   rotate _LE_camtolookangle rotate _LE_camskyangle translate camera_location
   no_shadow hollow}

// STORE LAYER OFFSETS FOR SUBSEQUENT EFFECTS
// ******************************************
   #if (effect_always_on_top != false)
      #declare _LE_lensoffset = _LE_lensscale;
   #else
      #declare _LE_prevdist = _LE_releffectloc.z;
      #declare _LE_prevlayer = _LE_lensscale - _LE_effectdist;
   #end

   #end #end

// GET RANDOM SEEDS FOR NEXT EFFECT
// ********************************
   #declare rays_seed = rand(_LE_raysrand) * 1e5;
   #declare streak_seed = rand(_LE_streakrand) * 1e5;
   #declare spots_seed = rand(_LE_spotsrand) * 1e5;

// POSITION CAMERA
// ***************
   camera {
      location camera_location
      sky camera_sky
      #ifdef (camera_direction) direction camera_direction #end
      #ifdef (camera_angle) angle camera_angle #end
      look_at camera_look_at}

   #version _LE_tempver;
