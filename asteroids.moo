rem asteroids.moo
rem The various types of asteroids.
rem $Source: /cvsroot/stellation/stellation/asteroids.moo,v $
rem $State: Exp $

.patch asteroids.moo 6 1
notify(player, "asteroids.moo");

$god:prop(#0, "asteroid", $unit:create());
$asteroid.name = "Generic asteroid";

$god:prop(#0, "masteroid", $asteroid:create());
$masteroid.name = "metallic asteroid";

$god:prop(#0, "casteroid", $asteroid:create());
$casteroid.name = "carbonaceous asteroid";

.quit

rem Revision History
rem $Log: asteroids.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

