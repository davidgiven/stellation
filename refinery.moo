rem refinery.moo
rem Represents a generic refinery.
rem $Source: /cvsroot/stellation/stellation/refinery.moo,v $
rem $State: Exp $

.patch refinery.moo 6 1
notify(player, "refinery.moo");

$god:prop(#0, "refinery", create($unit, $god));
$refinery.name = "Generic refinery";

$god:prop($refinery, "asteroids", {0, 0});
$god:prop($refinery, "rate", 0.1);
$god:prop($refinery, "production", {0.0, 0.0, 0.0});

$god:prop($refinery, "buffer", 0.0);
$god:prop($refinery, "deployed", 0);

# --- Resource consumption and production -------------------------------------

# Only deployed units consume resources.

.program $god $refinery:consume tnt
	{m, a, o} = args;
	if (this.deployed)
		if (this.buffer < (this.rate/10.0))
			if (this:eatasteroid())
				this.deployed = 0;
				this.location:notify(this.name+" has automatically mothballed itself because it ran out of asteroids to process.");
				return pass(@args);
			endif
		endif
		this.buffer = this.buffer - (this.rate / 10.0);
		m = m - this.production[1];
		a = a - this.production[2];
		o = o - this.production[3];
		return pass(m, a, o);
	endif
	return 0;
.

# --- Consume an asteroid -----------------------------------------------------

.program $god $refinery:eatasteroid tnt
	star = this.location;
	{dm, dc} = this.asteroids;
	if ((dm == 0) && (dc == 0))
		this.buffer = this.buffer + 1.0;
		return 0;
	endif
	{m, c} = star.asteroids;
	m = m - dm;
	c = c - dc;
	if ((m < 0) || (c < 0))
		return 1;
	endif
	this.buffer = this.buffer + 1.0;
	star.asteroids = {m, c};
	return 0;
.

# --- Deploy/mothball refinery ------------------------------------------------

.program $god $refinery:deployed tnt
	return this.deployed;
.

.program $god $refinery:deploy tnt
	if (!this.location:descendentof($star))
		return {"The "+this.name+" is not in a star system."};
	endif
	if (this.deployed)
		return {"The "+this.name+" is already deployed."};
	endif
	this.location:notify(this.name+" deployed.");
	this.deployed = 1;
	return {""};
.

.program $god $refinery:mothball tnt
	if (!this.location:descendentof($star))
		return {"The "+this.name+" is not in a star system."};
	endif
	if (!this.deployed)
		return {"The "+this.name+" is already mothballed."};
	endif
	this.location:notify(this.name+" mothballed.");
	this.deployed = 0;
	return {""};
.

# =============================================================================
#                                 HTML INTERFACE
# =============================================================================

# --- Deploy a refinery --------------------------------------------------------

.program $god $refinery:http_deploy tnt
	{c, method, param} = args;
	{objnum} = $http_server:parseparam(param, {"objnum"});
	result = this:deploy();
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- ...and mothball it ------------------------------------------------------

.program $god $refinery:http_mothball tnt
	{c, method, param} = args;
	{objnum} = $http_server:parseparam(param, {"objnum"});
	result = this:mothball();
	if (result[1] != "")
		$http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(toint(this)));
	else
		player:redirect(c, this);
	endif
.

# --- HTTP operations ---------------------------------------------------------

.program $god $refinery:http_info tnt
	{c} = args;
	if (!this.deployed)
		return;
	endif
	$htell(c, "(running)");
.

.program $god $refinery:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "deploy")
		return this:http_deploy(c, method, param);
	elseif (cmd == "mothball")
		return this:http_mothball(c, method, param);
	endif
	if (this.deployed == 0)
		$htell(c, "This unit must be deployed before it can be used.");
		$htell(c, "<BR><HR>");
		$htell(c, "<FORM ACTION=\"/player/unit\"><INPUT TYPE=hidden NAME=\"objnum\" VALUE=\""+tostr(toint(this))+"\"><INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"deploy\"><INPUT TYPE=submit VALUE=\"Deploy\"></FORM>");
		return;
	endif
	if (this.asteroids != {0, 0})
		$htell(c, "<B>Consumes:</B>");
		if (this.asteroids[1] > 0)
			$htell(c, tostr(this.asteroids[1])+" metallic");
		endif
		if (this.asteroids[2] > 0)
			$htell(c, tostr(this.asteroids[2])+" carbonaceous");
		endif
		$htell(c, "<BR><B>Every:</B> "+floatstr(1.0/this.rate, 3)+" hours");
		$htell(c, "<BR><B>Producing:</B>");
		$htell(c, $http_server:reslist(@this.production));
		$htell(c, "every 0.100 hours");
		$htell(c, "<BR><B>Internal stocks:</B>");
		$htell(c, floatstr(this.buffer*100.0, 1)+"% full");
	else
		$htell(c, "<BR><B>Produces:</B>");
		$htell(c, $http_server:reslist(@this.production));
		$htell(c, "every 0.100 hours");
	endif
	$http_server:startform(c, "/player/unit", objnum, "mothball");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Mothball\">");
	$http_server:endform(c);
.

.quit

rem Revision History
rem $Log: refinery.moo,v $
rem Revision 1.3  2000/08/05 22:44:08  dtrg
rem Many minor bug fixes.
rem Better object visibility testing --- less scope for cheating.
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

