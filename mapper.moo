rem mapper.moo
rem Graphics renderer
rem $Source: /cvsroot/stellation/stellation/mapper.moo,v $
rem $State: Exp $

.patch mapper.moo 3 1
notify(player, "mapper.moo");

$god:prop(#0, "mapper", create($object, $god));
$mapper.name = "Mapper";

$god:prop($mapper, "GRID", 0);
$god:prop($mapper, "STAR", 1);
$god:prop($mapper, "SHIFT", 2);

# --- Top-level routine -------------------------------------------------------

.program $god $mapper:render tnt
	{c, @args} = args;
	if (c != 0)
		if (player.mapmode == "text")
			$htell(c, "<PRE>");
			for i in (this:render_text(@args))
				$htell(c, i);
			endfor
			$htell(c, "</PRE>");
		elseif (player.mapmode == "gif")
			x = this:render_gif(@args);
			$htell(c, "<A HREF=\""+x+"\"><IMG SRC=\""+x+"\" WIDTH="+tostr(player.mapwidth)+" HEIGHT="+tostr(player.mapheight)+"></A>");
		else
			$htell(c, "[Invalid map mode "+player.mapmode+"]");
		endif
	else
		if (player.mapmode == "text")
			for i in (this:render_text(@args))
				notify(player, i);
			endfor
		elseif (player.mapmode == "gif")
			$htell(c, this:render_gif(@args));
		else
			notify(player, "[Invalid map mode "+player.mapmode+"]");
		endif
	endif
.

# --- Text renderer -----------------------------------------------------------

.program $god $mapper:render_text tnt
	w = player.mapwidth;
	h = player.mapheight;
	a = player.mapaspect;
	buf = {};
	l = "+";
	for x in [2..w-1]
		l = l + "-";
	endfor
	l = l + "+";
	buf = {l, l};
	l = "|";
	for x in [2..w-1]
		l = l + " ";
	endfor
	l = l + "|";
	for y in [2..h-1]
		buf = {buf[1], l, @buf[2..$]};
	endfor
	ox = tofloat(w)/2.0;
	oy = tofloat(h)/2.0;
	for i in (args)
		if (i[1] == $mapper.SHIFT)
			ox = ox + i[2];
			oy = oy + i[2]*a;
		elseif (i[1] == $mapper.STAR)
			x = i[2] + ox;
			y = i[3]*a + oy;
			buf = this:plot_text(buf, x, y, w, h, "* "+i[4]);
		elseif (i[1] == $mapper.GRID)
			x = i[2] + ox;
			y = i[3]*a + oy;
			xs = i[4];
			ys = i[4]*a;
			for i in [-20..20]
				for j in [-20..20]
					buf = this:plot_text(buf, x+tofloat(i)*xs, y+tofloat(j)*ys, w, h, ".");
				endfor
				suspend(0);
			endfor
		else
			return {"[Invalid map command "+toliteral(i)};
		endif
		suspend(0);
	endfor
	return buf;
.

.program $god $mapper:plot_text tnt
	{buf, x, y, w, h, s} = args;
	if ((x > 1.0) && (y > 1.0) &&
	    (x < tofloat(w)) && (y < tofloat(h)))
		x = toint(x);
		y = toint(y);
		l = length(s);
		if ((x+l) > w)
			l = w - x;
			s = s[1..l];
		endif
		buf[y][x..x+l-1] = s;
	endif
	return buf;
.

# --- GIF renderer ------------------------------------------------------------

.program $god $mapper:render_gif tnt
	buf = $server_options.gdrender_url+"?";
	w = player.mapwidth;
	h = player.mapheight;
	buf = buf + "width." + tostr(w) + ".";
	buf = buf + "height." + tostr(h) + ".";
	buf = buf + "colour.0.0.80.";
	buf = buf + "fbox.0.0."+tostr(w-1)+"."+tostr(h-1)+".";
	ox = tofloat(w)/2.0;
	oy = tofloat(h)/2.0;
	for i in (args)
		if (i[1] == $mapper.SHIFT)
			ox = ox + i[2];
			oy = oy + i[2];
		elseif (i[1] == $mapper.STAR)
			x = i[2] + ox;
			y = i[3] + oy;
			buf = buf + "arc."+tostr(toint(x))+"."+tostr(toint(y))+".8.8.0.360.";
			buf = buf + "font.s.";
			buf = buf + "text."+tostr(toint(x)+6)+"."+tostr(toint(y)-6)+"."+i[4]+".";
		elseif (i[1] == $mapper.GRID)
			x = i[2] + ox;
			y = i[3] + oy;
			xs = i[4];
			ys = i[4];
			buf = buf + "colour.100.100.100.";
			buf = buf + "grid."+tostr(toint(x))+"."+tostr(toint(y))+"."+tostr(toint(xs))+"."+tostr(toint(ys))+"."+tostr(toint(tofloat(w)/xs))+"."+tostr(toint(tofloat(h)/ys))+".";
			x = toint(x);
			y = toint(y);
			buf = buf + "colour.255.0.0.";
			buf = buf + "line."+tostr(x-w)+"."+tostr(y)+"."+tostr(x+w)+"."+tostr(y)+".";
			buf = buf + "line."+tostr(x)+"."+tostr(y-h)+"."+tostr(x)+"."+tostr(y+h)+".";
			buf = buf + "colour.255.255.255.";
		else
			return "[Invalid map command "+toliteral(i)+"]";
		endif
		suspend(0);
	endfor
	buf = buf + "colour.255.255.255.";
	buf = buf + "box.0.0."+tostr(w-1)+"."+tostr(h-1)+".";
	return buf+".";
.

.quit

rem Revision History
rem $Log: mapper.moo,v $
rem Revision 1.2  2000/07/30 00:00:40  dtrg
rem Took out the nasty text-mode map and replaced it with a gdrender GIF
rem based one.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem

