rem ship.moo
rem Represents a single ship.
rem $Source: /cvsroot/stellation/stellation/ship.moo,v $
rem $State: Exp $

.patch ship.moo 6 1
notify(player, "ship.moo");

$god:prop(#0, "ship", create($unit, $god));
$ship.name = "Generic ship";

.quit

rem Revision History
rem $Log: ship.moo,v $
rem Revision 1.2  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
rem Revision 1.1.1.1  2000/07/29 17:53:01  dtrg
rem Initial checkin.
rem


