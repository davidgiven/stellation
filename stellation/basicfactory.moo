rem basicfactory.moo
rem Represents a basic (entry-level) factory.
.patch basicfactory.moo 3 1
notify(player, "basicfactory.moo");

$god:prop(#0, "basicfactory", create($factory, $god));
$basicfactory.name = "basic factory";

$basicfactory.description = "The basic factory is a semi-automated device that, given the appropriate quantities of basic resources, can produce (one at a time) any of a considerable number of units.";

$basicfactory.buildable = {$jumpship, $cargoship, $tug, $basicfactory, $metalmine, $antimatterdistillery, $hydroponicsplant, $messagebuoy, $novacannon};

.quit

