package commands;

import model.SStar;
import model.SShip;
import tink.CoreApi;

class ShipsCommand extends AbstractLocalCommand<Noise> {
    @:keep public static final NAME = "ships";
    @:keep public static final DESCRIPTION = "lists your visible ships";

    override function run(): Noise {
        return Noise;
    }

	override function render(res: Noise): Void {
		for (ship in player.ships.getAll()) {
			if (player.canSee(ship)) {
				showShip(ship);
			}
		}
	}

	private function showShip(ship: SShip) {
		var star = ship.getContainingStar();
		console.println('  ${ship.name} (#${ship.oid}) currently at ${star.name} (#${star.oid})');
	}
}

