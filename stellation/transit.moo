rem transit.moo
rem This `star' represents a fleet in transit.
.patch transit.moo 3 1
notify(player, "transit.moo");

$god:prop(#0, "transit", create($star, $god));
$transit.name = "Transit bubble";

# --- Generic transit bubble properties ---------------------------------------

$god:prop($transit, "origin", {0.0, 0.0});
$god:prop($transit, "destination", {0.0, 0.0});
$god:prop($transit, "distance", 0.0);
$god:prop($transit, "eta", 0);

# --- Constructor -------------------------------------------------------------

.program $god $transit:create tnt
	{src, dest, dist, fleet} = args;
	obj = pass();
	obj.origin = src;
	obj.destination = dest;
	obj.distance = dist;
	obj.eta = toint(tofloat(time()) + dist*$tick);
	notify($god, "Fleet "+tostr(fleet)+" in transit to ["+tostr(src[1])+", "+tostr(src[2])+"]; ETA "+$numutils:timetostr(obj.eta));
	fleet:moveto(obj);
	fork pid (toint(dist*$tick))
		try
			obj:arrive();
		except v (ANY)
			player = $god;
			$traceback(v);
		endtry
	endfork
.

# --- Event notification ------------------------------------------------------

.program $god $transit:notify tnt
	{msg} = args;
.

# --- Transit bubble complete -------------------------------------------------

.program $god $transit:arrive tnt
	{x, y} = this.destination;
	star = $galaxy:find_star(x, y);
	if (star == #-1)
		notify($god, "creating new interstellar space thing at ["+floatstr(x, 1)+", "+floatstr(y, 1)+"]");
		star = $deepspace:create(x, y);
	endif
	this:contents()[1]:moveto(star);
	this:destroy();
.

# --- HTML interface ----------------------------------------------------------

.program $god $transit:http_menu tnt
	{c, method, param} = args;
	$htell(c, "<B>From:</B> ["+tostr($numutils:round(10, this.origin[1]))+", "+tostr($numutils:round(10, this.origin[2]))+"]");
	$htell(c, "<BR><B>To:</B> ["+tostr($numutils:round(10, this.destination[1]))+", "+tostr($numutils:round(10, this.destination[2]))+"]");
	$htell(c, "<BR><B>ETA:</B> "+$numutils:timetostr(this.eta));
	$htell(c, "<HR>");
	$htell(c, "Fleets in transit can not be communicated with.");
.

.quit

