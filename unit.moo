rem unit.moo
rem Represents a single unit of any type.
rem $Source: /cvsroot/stellation/stellation/unit.moo,v $
rem $State: Exp $

.patch unit.moo 3 1
notify(player, "unit.moo");

$god:prop(#0, "unit", create($object, $god));
$unit.name = "Generic unit";

$god:prop($unit, "time_cost", {1.0, 1.0, 1.0});
$god:prop($unit, "mass", 1000.0);
$god:prop($unit, "build_cost", {100.0, 100.0, 100.0});
$god:prop($unit, "build_time", 1.0);
$god:prop($unit, "description", "Fnord!");
$god:prop($unit, "damage", 0);
$god:prop($unit, "maxdamage", 100);

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

# --- Resource consumption ---------------------------------------------------

.program $god $unit:make_alive tnt
	if (this.pit != 0)
		return;
	endif
	fork pid (toint($tick/10.0))
		this.pit = 0;
		try
			if (this:consume(this.time_cost[1]/10.0,
				this.time_cost[2]/10.0, this.time_cost[3]/10.0))
				this:starve();
			endif
			this:make_alive();
		except v (ANY)
			player = $god;
			$traceback(v);
		endtry
	endfork
	this.pit = pid;
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
	notify($god, tostr(this)+" consumed "+tostr(m)+", "+tostr(a)+", "+tostr(o)+": "+tostr(c));
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

.quit

rem Revision History
rem $Log: unit.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem


