rem ship.moo
rem Represents a single ship.
.patch ship.moo 3 1
notify(player, "ship.moo");

$god:prop(#0, "ship", create($unit, $god));
$ship.name = "Generic ship";

.quit

