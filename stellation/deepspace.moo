rem deepspace.moo
rem Represents a point in interstellar space.
.patch deepspace.moo 3 1
notify(player, "deepspace.moo");

$god:prop(#0, "deepspace", create($star, $god));
$deepspace.name = "Interstellar Space";

# --- Constructor -------------------------------------------------------------

.program $god $deepspace:create tnt
	{x, y} = args;
	obj = pass();
	obj.position = {x, y};
	$galaxy.stars = setadd($galaxy.stars, obj);
	return obj;
.

.quit

