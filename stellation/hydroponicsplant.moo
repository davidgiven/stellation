rem hydroponicsplant.moo
rem Represents a hydroponics plant.
.patch hydroponicsplant.moo 3 1
notify(player, "hydroponicsplant.moo");

$god:prop(#0, "hydroponicsplant", create($solarrefinery, $god));
$hydroponicsplant.name = "hydroponics plant";

$hydroponicsplant.asteroids = {0, 1};
$hydroponicsplant.rate = (1.0/24.0);
$hydroponicsplant.production = {0.0, 0.0, 20.0};

$hydroponicsplant.description = "This unit searches out carbonaceous asteroids and builds greenhouse domes around them. In conjunction with sunlight, the plants in the dome draw off the organic compounds in the asteroid to provide the organic consumables required to maintain the biochemical brains in all other units.";

.quit

