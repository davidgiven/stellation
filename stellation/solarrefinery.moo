rem solarrefinery.moo
rem A refinery that relies on the sun's brightness.
.patch solarrefinery.moo 3 1
notify(player, "solarrefinery.moo");

$god:prop(#0, "solarrefinery", create($refinery, $god));
$solarrefinery.name = "Generic solar refinery";

$god:prop($solarrefinery, "brightnessmultiplier", 1.0);

# --- Deploy ------------------------------------------------------------------

.program $god $solarrefinery:deploy tnt
	result = pass();
	if (result[1] != "")
		return result;
	endif
	this.rate = parent(this).rate;
	{m, a, o} = parent(this).production;
	notify($god, "solarrefinery: base rate "+floatstr(this.rate, 2));
	if (0)
		b = tofloat(this.location.brightness);
		if (b > 0.0)
			b = 1.0;
			this.rate = this.rate / b;
		else
			this.rate = 0.0;
		endif
		this.production = {m*b, a*b, o*b};
	endif
	this.production = {m, a, o};
	notify($god, "solarrefinery: adjusted rate "+floatstr(this.rate, 2));
	return {""};
.

.quit

