// GALAXY INCLUDE FILE: DYNAMIC OPTIONS ANIMATION
// **********************************************
// This animation shows the effect of various galaxy options on the
// resulting galaxy scene.
//
// Recommended frames: 50 to 100, cyclic animation

// BACKGROUND STARFIELD AND MILKY WAY
   #declare galaxy_bgstars = 5;
   #declare galaxy_bgnebula = 6;
   #declare galaxy_nebula_sphere = false;
   #include "GALAXY.BG"

// ANIMATED NEBULA
   #declare galaxy_nebula_sphere = 1;
   #declare galaxy_colour1 = <1, .5, .4> + <0, .5, .2> * (sin((clock - .25) * pi) + 1) / 2;
   #declare galaxy_colour2 = <0, .5, 1> + <1, -.3, -.3> * (sin((clock - .25) * pi) + 1) / 2;
   #declare galaxy_pattern_scale = 1 + .4 * sin(clock * 2 * pi);
   #declare galaxy_pattern_origin = <-10, 10, 10> + vrotate (z * .2, <45 * sin(clock * pi * 4), clock * 360, 0>);
   #include "GALAXY.BG"
