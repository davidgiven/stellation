rem basicfactory.moo
rem Represents a basic (entry-level) factory.
rem $Source: /cvsroot/stellation/stellation/basicfactory.moo,v $
rem $State: Exp $

.patch basicfactory.moo 3 1
notify(player, "basicfactory.moo");

$god:prop(#0, "basicfactory", create($factory, $god));
$basicfactory.name = "basic factory";

$basicfactory.description = "The basic factory is a semi-automated device that, given the appropriate quantities of basic resources, can produce (one at a time) any of a considerable number of units.";

$basicfactory.buildable = {$jumpship, $cargoship, $tug, $basicfactory, $metalmine, $antimatterdistillery, $hydroponicsplant, $messagebuoy, $novacannon};

.quit

rem Revision History
rem $Log: basicfactory.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

