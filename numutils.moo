rem numutils.moo
rem Numerical utilities.
rem $Source: /cvsroot/stellation/stellation/numutils.moo,v $
rem $State: Exp $

.patch numutils.moo 6 1
notify(player, "numutils.moo");

$god:prop(#0, "numutils", create($object, $god));
$numutils.name = "Numerical Utilities";

$god:prop($numutils, "pi", 0.0);
$numutils.pi = 2.0 * acos(0.0);

# --- Random number -----------------------------------------------------------

.program $god $numutils:random tnt
	{max} = args;
	i = tofloat(random(10000)) / 10000.0;
	return max * i;
.

# --- Round a number to a certain number of decimal places --------------------

.program $god $numutils:round tnt
	{factor, num} = args;
	i = trunc(num * tofloat(factor));
	return i / tofloat(factor);
.

# --- Turn an int into a string, with left padding ----------------------------

.program $god $numutils:tostrpadded tnt
	{n, pad} = args;
	n = floatstr(1.0, pad)+tostr(n);
	return n[$-(pad-1)..$];
.

# --- Emit a time, as a string ------------------------------------------------

.program $god $numutils:timetostr tnt
	{t} = args;
	t = tofloat(t)/3600.0;
	t = toint(t*1000.0);
	t3 = t % 1000;
	t = t / 1000;
	t2 = t % 1000;
	t = t / 1000;
	return tostr(t)+"."+$numutils:tostrpadded(t2, 3)+"."+$numutils:tostrpadded(t3, 3);
.

# --- Sorts a list ------------------------------------------------------------

# This may not be the best place to put this; but it's here or create a new
# object, and I can't be bothered. These methods sort a list.
#
# Each item in the list should be a list itself; the sort is keyed on the first
# item. It can be of any type provided they're all the *same* type and they
# can be compared.
#
# The algorithm is a bit strange. I'm trying to put as much load as possible
# on the VM rather than MOO code; listinsert() and listdelete() are orders of
# magnitude faster than anything I can possibly do in MOO code. It's a sort of
# insertion sort.

.program $god $numutils:sort tnt
	{inlist} = args;
	if (length(inlist) <= 2)
		return inlist;
	endif
	outlist = {inlist[1]};
	inlist = inlist[2..$];
	item = 1;
	itemv = outlist[1][1];
	for i in (inlist)
		notify($god, toliteral(inlist));
		notify($god, toliteral(outlist));
		notify($god, "item="+toliteral(item)+" itemv="+tostr(itemv));
		if (i[1] < itemv)
			while (item > 1)
				item = item - 1;
				itemv = outlist[item][1];
				if (i[1] > itemv)
					break;
				endif
			endwhile
			if (item == 1)
				outlist = {i, @outlist};
			else
				outlist = listappend(outlist, i, item);
			endif
		elseif (i[1] > itemv)
			while (item < length(outlist))
				item = item + 1;
				itemv = outlist[item][1];
				if (i[1] < itemv)
					break;
				endif
			endwhile
			if (item == length(outlist))
				outlist = {@outlist, i};
			else
				outlist = listappend(outlist, i, item);
			endif
		else
			outlist = listinsert(outlist, i, item);
		endif
		itemv = outlist[item][1];
	endfor
	return outlist;
.

.quit

rem Revision History
rem $Log: numutils.moo,v $
rem Revision 1.4  2000/08/27 23:50:55  dtrg
rem Added sort routine. No idea if it's an optimal algorithm or not, or
rem even if it works properly. Seems to so far.
rem
rem Revision 1.3  2000/08/05 22:34:05  dtrg
rem Now zero-pads time components.
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

