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
   camera {location <0, 0, 0> look_at <0, 0, 1> angle 24}

// COMMON GALAXY OPTIONS
   #declare galaxy_colour1 = <1.3, 1.2, .9>;
   #declare galaxy_colour2 = <.4, .8, 1>;
   #declare galaxy_cluster_name = ""

// STARFIELD BACKGROUND
   #declare galaxy_bgstars = 2;
   #declare galaxy_bgnebula = false;
   #declare galaxy_nebula_sphere = false;
   #include "GALAXY.BG"

// STAR OBJECTS
   #declare galaxy_object_scale = .2;
   #declare C = 0; #while (C < 4)
      #declare galaxy_object_name = concat("Star", str(C + 1, 0, 0))
      #declare galaxy_object_position = <7, -10 + C * 4, 0>;
      #include "GALAXY.OBJ"
   #declare C = C + 1; #end

// NEBULAE OBJECTS
   #declare galaxy_object_scale = .15;
   #declare C = 0; #while (C < 6)
      #declare galaxy_object_name = concat("Nebula", str(C + 1, 0, 0))
      #declare galaxy_object_position = <3, -10 + C * 4, 0>;
      #include "GALAXY.OBJ"
   #declare C = C + 1; #end

// GALAXY OBJECTS
   #declare galaxy_object_rotate = 30;
   #declare C = 0; #while (C < 5)
      #declare galaxy_object_name = concat("Galaxy", str(C + 1, 0, 0))
      #declare galaxy_object_position = <-1, -10 + C * 4, 0>;
      #include "GALAXY.OBJ"
   #declare C = C + 1; #end

// COMET OBJECTS
   #declare C = 0; #while (C < 3)
      #declare galaxy_object_name = concat("Comet", str(C + 1, 0, 0))
      #declare galaxy_object_position = <-4.5, -11 + C * 4, 0>;
      #include "GALAXY.OBJ"
   #declare C = C + 1; #end

// METEOR OBJECTS
   #declare C = 0; #while (C < 2)
      #declare galaxy_object_name = concat("Meteor", str(C + 1, 0, 0))
      #declare galaxy_object_position = <-7.5, -11 + C * 4, 0>;
      #include "GALAXY.OBJ"
   #declare C = C + 1; #end
