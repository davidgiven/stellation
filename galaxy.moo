rem galaxy.moo
rem Represents the whole galaxy
rem $Source: /cvsroot/stellation/stellation/galaxy.moo,v $
rem $State: Exp $

.patch galaxy.moo 6 1
notify(player, "galaxy.moo");

$god:prop(#0, "galaxy", create($object, $god));
$galaxy.name = "The Galaxy";

# --- Properties --------------------------------------------------------------

$god:prop($galaxy, "stars", {});
$god:prop($galaxy, "radius", 40.0);
$god:prop($galaxy, "numstars", 400);
#$galaxy.radius = 20.0;
#$galaxy.numstars = 100;

# --- Create random galaxy ----------------------------------------------------

.program $god $galaxy:create_galaxy tnt
	{num} = args;
	s = {};
	while (length(s) < num)
		r = $numutils:random(this.radius);
		theta = $numutils:random(2.0 * $numutils.pi);
		x = r * sin(theta);
		y = r * cos(theta);
		x = $numutils:round(10, x);
		y = $numutils:round(10, y);
		s = setadd(s, {$stringutils:namegen(), x, y});
		suspend(0);
	endwhile
	for i in (s)
		star = create($star, $god);
		star.name = i[1];
		star.name[1] = $stringutils:upper(star.name[1]);
		star.position = {i[2], i[3]};
		star.asteroids = {random(10)+10, random(10)+10};
		star.brightness = random(10)-1;
		this.stars = {@this.stars, star};
		suspend(0);
	endfor
.

# --- Find a space thing in the galaxy ----------------------------------------

.program $god $galaxy:find_star tnt
	{x, y} = args;
	for i in (this.stars)
		{thisx, thisy} = i.position;
		if (((x-thisx)*(x-thisx) + (y-thisy)*(y-thisy)) < 0.01)
			return i;
		endif
	endfor
	return #-1;
.

.program $god $galaxy:find_star_by_name tnt
	{name} = args;
	for i in (this.stars)
		if (i.name == name)
			return i;
		endif
	endfor
	return #-1;
.

notify(player, "Creating galaxy");
$galaxy:create_galaxy($galaxy.numstars);

.quit

rem Revision History
rem $Log: galaxy.moo,v $
rem Revision 1.3  2000/08/02 23:17:27  dtrg
rem Finished off nova cannon. Destroyed my first unit! All seems to work OK.
rem Made fleets disappear automatically when their last unit is removed.
rem Fixed a minor fleet creation bug.
rem Made the title pages look a *lot* better.
rem Added a game statistics page to the overview.
rem Lots of minor formatting issues.
rem
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

