rem galaxy.moo
rem Represents the whole galaxy
rem $Source: /cvsroot/stellation/stellation/galaxy.moo,v $
rem $State: Exp $

.patch galaxy.moo 3 1
notify(player, "galaxy.moo");

$god:prop(#0, "galaxy", create($object, $god));
$galaxy.name = "The Galaxy";

# --- Properties --------------------------------------------------------------

$god:prop($galaxy, "stars", {});
$god:prop($galaxy, "radius", 40.0);
$god:prop($galaxy, "numstars", 400);
$galaxy.radius = 20.0;
$galaxy.numstars = 100;

# --- Create random galaxy ----------------------------------------------------

.program $god $galaxy:create_galaxy tnt
	{num} = args;
	for i in [1..num]
		if ((i % 10) == 0)
			notify(player, tostr(i)+"/"+tostr(num));
		endif
		while (1)
			r = $numutils:random(this.radius);
			theta = $numutils:random(2.0 * $numutils.pi);
			x = r * sin(theta);
			y = r * cos(theta);
			x = $numutils:round(10, x);
			y = $numutils:round(10, y);
			name = $stringutils:namegen(i);
			if (this:find_star(x, y) == #-1)
			    	break;
			endif
		endwhile
		star = create($star, $god);
		star.name = name;
		star.name[1] = $stringutils:upper(name[1]);
		star.position = {x, y};
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
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

