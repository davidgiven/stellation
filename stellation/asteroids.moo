rem asteroids.moo
rem The various types of asteroids.
.patch asteroids.moo 3 1
notify(player, "asteroids.moo");

$god:prop(#0, "asteroid", $unit:create());
$asteroid.name = "Generic asteroid";

$god:prop(#0, "masteroid", $asteroid:create());
$masteroid.name = "metallic asteroid";

$god:prop(#0, "casteroid", $asteroid:create());
$casteroid.name = "carbonaceous asteroid";

.quit

