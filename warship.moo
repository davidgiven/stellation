rem warship.moo
rem Generic warship.
rem $Source: /cvsroot/stellation/stellation/warship.moo,v $
rem $State: Exp $

.patch warship.moo 6 1
notify(player, "warship.moo");

$god:prop(#0, "warship", create($ship, $god));
$warship.name = "Generic warship";

.quit

rem Revision History
rem $Log: warship.moo,v $
rem Revision 1.1  2000/07/30 21:20:19  dtrg
rem Updated all the .patch lines to contain the correct line numbers.
rem Cosmetic makeover; we should now hopefully look marginally better.
rem Bit more work on the nova cannon.
rem A few minor bug fixes.
rem
