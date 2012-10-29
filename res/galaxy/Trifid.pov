// GALAXY INCLUDE FILE: CUSTOM GALAXY SCENE
// ****************************************
// This scene shows how Galaxy.obj and Galaxy.sf can be used to build
// custom galaxy scenes, piece by piece.
//
// Recommended resolution: 640 x 480, anti-aliasing on

// STARFIELD
   #declare star_count = 2000;
   #declare star_scale = .5;
   #include "GALAXY.SF"

   #declare galaxy_seed = 1;
   #declare star_count = 500;
   #declare star_type = 3;
   #declare star_colour = <1, .9, .7>;
   #declare star_scale = 1.5;
   #include "GALAXY.SF"

// PINK NEBULA
   #declare galaxy_colour1 = <1.2, 1, 1.1>;
   #declare galaxy_colour2 = <1, .3, .6>;
   #declare galaxy_pattern_origin = x * 1;
   #declare galaxy_turb_origin = x * -4;
   #declare galaxy_object_name = "Nebula3"
   #declare galaxy_object_scale = 1.75;
   #declare galaxy_object_position = <-5, 5, 0>;
   #declare galaxy_cluster_name = ""
   #include "GALAXY.OBJ"

// BLUE NEBULA
   #declare galaxy_colour1 = <.5, .9, 1.2>;
   #declare galaxy_colour2 = <.1, .3, .5>;
   #declare galaxy_pattern_origin = x * -20;
   #declare galaxy_object_name = "Nebula2"
   #declare galaxy_object_scale = 1.2;
   #declare galaxy_object_position = <10, -12, 0>;
   #include "GALAXY.OBJ"

// LARGE STAR
   #declare galaxy_object_name = "Star1"
   #declare galaxy_colour1 = <1.5, 1.5, 1.5>;
   #declare galaxy_object_scale = 1;
   #declare galaxy_object_position = <17, -10, 0>;
   #include "GALAXY.OBJ"
