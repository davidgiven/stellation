rem ship.moo
rem Represents a single ship.
rem $Source: /cvsroot/stellation/stellation/ship.moo,v $
rem $State: Exp $

.patch ship.moo 3 1
notify(player, "ship.moo");

$god:prop(#0, "ship", create($unit, $god));
$ship.name = "Generic ship";

.quit

rem Revision History
rem $Log: ship.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem


