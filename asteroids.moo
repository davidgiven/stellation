rem asteroids.moo
rem The various types of asteroids.
rem $Source: /cvsroot/stellation/stellation/asteroids.moo,v $
rem $State: Exp $

.patch asteroids.moo 3 1
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
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

