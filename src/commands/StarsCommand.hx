package commands;

import model.SStar;
import tink.CoreApi;
import utils.GetOpt.getopt;
import utils.Flags;

class StarsCommand extends AbstractLocalCommand<Noise, Noise> {
    @:keep public static final NAME = "stars";
    @:keep public static final DESCRIPTION = "lists interesting (or otherwise) stars";

	private var showAll = false;

    @:keep override function parse(argv: Array<String>): Noise {
		var remaining = getopt(argv.slice(1), new Flags()
				.addFlag("-a", s -> showAll = true)
				.addFlag("--all", s -> showAll = true)
		);
		return Noise;
    }

    override function run(argv: Array<String>, req: Noise): Noise {
        return Noise;
    }

	override function render(res: Noise): Void {
		if (showAll) {
			for (o in galaxy.contents.getAll()) {
				var star = o.as(SStar);
				if (star != null) {
					showStar(star);
				}
			}
		} else {
			for (ship in player.ships.getAll()) {
				if (player.canSee(ship)) {
					showStar(ship.getContainingStar());
				}
			}
		}
	}

	private function showStar(star: SStar) {
		console.println('  ${star.name} at ${star.x}, ${star.y} (#${star.oid})');
	}
}

