rem bomber.moo
rem Cargo ship.
rem $Source: /cvsroot/stellation/stellation/bomber.moo,v $
rem $State: Exp $

.patch bomber.moo 6 1
notify(player, "bomber.moo");

$god:prop(#0, "bomber", create($ship, $god));
$bomber.name = "RAM bomber";

$bomber.description = "Relativistic antimatter bombers do damage to individually targeted stationary units by firing short pulses of high-speed antimatter at them. They allow you to do specified amounts of damage to a particular unit, whether yours or anyone else's.";

$god:prop($bomber, "firecost", 10.0);
$god:prop($bomber, "lastfire", 0.0);

# --- Hit someone! ------------------------------------------------------------

.program $god $bomber:bomb tnt
	{unit, amount} = args;
	if (!(unit in this:location():location():contents()))
		return {"That unit is not within range."};
	endif
	if (unit:descendentof($fleet))
		return {"You can't attack a fleet with this unit."};
	endif
	if (amount < 0.0)
		return {"Negative values make no sense here."};
	endif
	if (this:location():consume(0.0, amount, 0.0))
		return {"Insufficient antimatter is available."};
	endif
	this:location():location():notify("<B>"+unit:name()+"</B> belonging to <B>"+unit.owner:name()+"</B> has been hit for <B>"+floatstr(amount / this.firecost, 1)+"</B> units of damage.");
	return unit:attack(amount / this.firecost);
.

.program $god $bomber:http_bomb tnt
	{c, method, param} = args;
	{objnum, cmd, amount, ?unit=#0} = $http_server:parseparam(param, {"objnum", "cmd", "amount", "unit"});
	if (unit == #0)
		return $http_server:error(c, "You have to specify a target!", "/player/unit?objnum="+tostr(tonum(this)));
	endif
	amount = tofloat(amount);
	unit = toobj(unit);
	result = this:bomb(unit, amount);
	if (result[1] != "")
		return $http_server:error(c, "Failed! "+result[1], "/player/unit?objnum="+tostr(tonum(this)));
	endif
	return player:redirect(c, this);
.

# --- HTML operations ---------------------------------------------------------

.program $god $bomber:http_menu tnt
	{c, method, param} = args;
	{objnum, ?cmd=""} = $http_server:parseparam(param, {"objnum", "cmd"});
	if (cmd == "bomb")
		return this:http_bomb(c, method, param);
	endif
	$http_server:startform(c, "/player/unit", objnum, "bomb");
	$htell(c, "<TABLE COLS=4>");
	$htell(c, "<TR><TH WIDTH=1></TH><TH>Unit</TH><TH>Owner</TH><TH>Damage</TH></TR>");
	j = 0;
	for i in (this:location():location():contents())
		if (!i:descendentof($fleet))
			$htell(c, "<TR><TD WIDTH=1><INPUT TYPE=radio NAME=\"unit\" VALUE=\""+tostr(toint(i))+"\"></TD>");
			$htell(c, "<TD>"+i:name()+"</TD>");
			$htell(c, "<TD>"+i.owner:name()+"</TD>");
			$htell(c, "<TD>"+floatstr(i.damage, 1)+"/"+floatstr(i.maxdamage, 1)+"</TD>");
			$htell(c, "</TR>");
			j = j + 1;
		endif
	endfor
	$htell(c, "</TABLE>");
	$htell(c, tostr(j)+" units within range.");
	$htell(c, "<BR>Amount of antimatter to expend:");
	$htell(c, "<INPUT NAME=\"amount\" VALUE=\""+floatstr(this.lastfire, 1)+"\" SIZE=6>");
	$htell(c, "<BR>(Each unit of damage requires");
	$htell(c, floatstr(this.firecost, 1));
	$htell(c, "units of antimatter)<BR>");
	$htell(c, "<INPUT TYPE=submit VALUE=\"Fire\"><BR>");
	$http_server:endform(c);
.

.quit

rem Revision History
rem $Log: bomber.moo,v $
rem Revision 1.1  2000/09/09 22:36:34  dtrg
rem First working version.
rem
