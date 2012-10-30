// GALAXY INCLUDE FILE: SAMPLE GALAXY OBJECTS
// ******************************************
// This scene shows all of the twenty galaxy objects created by
// Galaxy.obj.  They are grouped by row (Stars, Nebulae, Galaxies,
// Comets, and Meteors at the bottom), numbered from left to right
// (starting at 1).  For more information on using these objects,
// see Galaxy.txt
//
// Recommended resolution: 800 x 600 or larger, no anti-aliasing

// NARROW ANGLE CAMERA (TO PREVENT DISTORTION)
camera {
	location <0, 0, 0>
	look_at <0, 0, 1>
	angle 24
	right x
}

// COMMON GALAXY OPTIONS
#declare galaxy_colour1 = <1.3, 1.2, .9>;
#declare galaxy_colour2 = <.4, .8, 1>;
#declare galaxy_cluster_name = ""

// STARFIELD BACKGROUND
#declare galaxy_bgstars = false;
#declare galaxy_bgnebula = false;
#declare galaxy_nebula_sphere = false;
#include "res/galaxy/GALAXY.BG"

// GALAXY OBJECTS
#declare galaxy_object_scale = 1;
#declare galaxy_object_rotate = 30;
#declare galaxy_object_name = "Galaxy4";
#declare galaxy_object_position = <0, 0, 0>;
#declare galaxy_colour_turb = 1;
#include "res/galaxy/GALAXY.OBJ"
