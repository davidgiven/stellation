rem unit.moo
rem Represents a single unit of any type.
rem $Source: /cvsroot/stellation/stellation/unit.moo,v $
rem $State: Exp $

.patch unit.moo 6 1
notify(player, "unit.moo");

$god:prop(#0, "unit", create($object, $god));
$unit.name = "Generic unit";

$god:prop($unit, "time_cost", {1.0, 1.0, 1.0});
$god:prop($unit, "mass", 1000.0);
$god:prop($unit, "build_cost", {100.0, 100.0, 100.0});
$god:prop($unit, "build_time", 1.0);
$god:prop($unit, "description", "Fnord!");
$god:prop($unit, "damage", 0.0);
$god:prop($unit, "maxdamage", 100.0);

$god:prop($unit, "pit", 0);

# --- Constructor & destructor ------------------------------------------------

# Start the periodic timer that handles the resource consumption for the unit.

.program $god $unit:initialize tnt
	pass();
.

# Ensure the periodic timer is stopped before destroying the object.

.program $god $unit:destroy tnt
	this:make_dead();
	pass();
.

# --- Initialisation ----------------------------------------------------------

# Used to set the stats in stats.moo.

.program $god $unit:setstats tnt
	{mass, time_cost, build_cost, build_time, maxdamage} = args;
	this.mass = mass;
	this.time_cost = time_cost;
	this.build_cost = build_cost;
	this.build_time = build_time;
	this.maxdamage = maxdamage;
.

# --- Property fetchers -------------------------------------------------------

.program $god $unit:time_cost tnt
	return this.time_cost;
.

.program $god $unit:mass tnt
	return this:restmass();
.

.program $god $unit:restmass tnt
	return this.mass;
.

.program $god $unit:build_cost tnt
	return this.build_cost;
.

.program $god $unit:build_time tnt
	return this.build_time;
.

.program $god $unit:description tnt
	return this.description;
.

# --- Is this unit visible to the player? ------------------------------------

.program $god $unit:visible tnt
	{?p=player} = args;
	star = this.location;
	while (!star:descendentof($star))
		star = star.location;
	endwhile
	if (star in p:starsystems())
		return 1;
	endif
	return 0;
.

# --- Resource consumption ---------------------------------------------------

.program $god $unit:make_alive tnt
	if (this.pit != 0)
		return;
	endif
	fork pid (toint($tick/10.0))
		this:tick();
	endfork
	this.pit = pid;
.

.program $god $unit:tick tnt
	this.pit = 0;
	try
		c = this:time_cost();
		if (this:consume(c[1]/10.0, c[2]/10.0, c[3]/10.0))
			this:starve();
		endif
		this:make_alive();
	except v (ANY)
		player = $god;
		$traceback(v);
	endtry
.

.program $god $unit:make_dead tnt
	if (this.pit != 0)
		kill_task(this.pit);
		this.pit = 0;
	endif
.
	
.program $god $unit:consume tnt
	{m, a, o} = args;
	if ((m == 0.0) && (a == 0.0) && (o == 0.0))
		return 0;
	endif
	c = this.location:consume(m, a, o);
/*	notify($god, tostr(this)+" consumed "+tostr(m)+", "+tostr(a)+", "+tostr(o)+": "+tostr(c)); */
	return c;
.
	
.program $god $unit:starve tnt
	star = this.location;
	while (!star:descendentof($star))
		star = star.location;
	endwhile
	player:notify("<B>"+this.name+"</B> has been lost due to lack of resources.", star);
	notify($god, this.name+" lost");
	this:destroy();
.

# --- Attack unit -------------------------------------------------------------

.program $god $unit:attack tnt
	{damage} = args;
	this.damage = this.damage + damage;
	/* notify($god, this:name()+" hit for "+floatstr(damage, 1)+" leaving "+floatstr(damage, 1)); */
	this.owner:seehostileplayer(player);
	if (this.damage > this.maxdamage)
		star = this.location;
		while (!star:descendentof($star))
			star = star.location;
		endwhile
		star:notify("attack has destroyed <B>"+this.name+"</B> belonging to <B>"+this.owner:name()+"</B>.");
		/* notify($god, this.name+" destroyed"); */
		this:destroy();
	endif
.

# --- Transfer unit from one fleet to another ---------------------------------

.program $god $unit:transferto tnt
	{target} = args;
	if (this.location.location != target.location)
		return {"You can't transfer units between fleets that aren't in the same star system."};
	endif
	this:moveto(target);
	return {""};
.

.program $god $unit:http_transfer tnt
	{c, method, param} = args;
	{objnum, cmd, ?target=""} = $http_server:parseparam(param, {"objnum", "cmd", "target"});
	target = toobj(target);
	if (!target:descendentof($fleet) || (target.owner != player))
		return $http_server:formsyntax(c);
	endif
	result = this:transferto(target);
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- Find hostile & friendly fleets ------------------------------------------

.program $god $unit:hostilefleets tnt
	star = this.location;
	while (!star:descendentof($star))
		star = star.location;
	endwhile
	f = {};
	enemies = player:enemyplayers();
	for i in (star:contents())
		if (i:descendentof($fleet))
			if (i.owner in enemies)
				f = {@f, i};
			endif
		endif
	endfor
	return f;
.

# --- HTML operations ---------------------------------------------------------

.program $god $unit:http_info tnt
	{c} = args;
.

.program $god $unit:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "transfer")
		return this:http_transfer(c, method, param);
	endif
.

.program $god $unit:http_menu_notowned tnt
	{c, method, param} = args;
	$htell(c, "No further information available.");
.

.quit

rem Revision History
rem $Log: unit.moo,v $
rem Revision 1.6  2000/08/07 20:12:38  dtrg
rem Formatting fixes.
rem
rem Revision 1.5  2000/08/05 22:44:08  dtrg
rem Many minor bug fixes.
rem Better object visibility testing --- less scope for cheating.
rem
rem Revision 1.4  2000/08/02 23:17:27  dtrg
rem Finished off nova cannon. Destroyed my first unit! All seems to work OK.
rem Made fleets disappear automatically when their last unit is removed.
rem Fixed a minor fleet creation bug.
rem Made the title pages look a *lot* better.
rem Added a game statistics page to the overview.
rem Lots of minor formatting issues.
rem
rem Revision 1.3  2000/08/01 22:06:04  dtrg
rem Owned stars are now showed in yellow again.
rem Fixed viewing other people's units; all the tracebacks should have gone.
rem Various minor bug fixes and formatting changes.
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


