rem deepspace.moo
rem Represents a point in interstellar space.
rem $Source: /cvsroot/stellation/stellation/deepspace.moo,v $
rem $State: Exp $

.patch deepspace.moo 6 1
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

rem Revision History
rem $Log: deepspace.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

