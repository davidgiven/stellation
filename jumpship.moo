rem jumpship.moo
rem Represents a jumpship.
rem $Source: /cvsroot/stellation/stellation/jumpship.moo,v $
rem $State: Exp $

.patch jumpship.moo 3 1
notify(player, "jumpship.moo");

$god:prop(#0, "jumpship", create($ship, $god));
$jumpship.name = "jumpship";

$jumpship.description = "A jumpship is used to carry the spatial flaw used to bootstrap wormholes. Its primary function is to carry fleets of other ships through interstellar distances. The spatial flaw can also be used to send instantaneous communications from one jumpship to another, but only ones for which the exact resonant frequency of the flaw is known.";

.quit

rem Revision History
rem $Log: jumpship.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

