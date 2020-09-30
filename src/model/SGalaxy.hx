package model;

import utils.Injectomatic.inject;
import utils.Random;
import tink.CoreApi;
import haxe.ds.HashMap;
using utils.NumberTools;
using Math;
using Lambda;
using utils.ArrayTools;
using model.ThingTools;

typedef Location = {
	x: Float,
	y: Float,
	hashCode: Void -> Int
};

@:tink
class SGalaxy extends SThing {
	public static final RADIUS = 30.0;
	public static final NUMBER_OF_STARS = 200;

	public function initialiseGalaxy(): Void {
		var nameGenerator = new NameGenerator();
		var starNames = new Map<String, Noise>();
		var starCount = 0;
		while (starCount < NUMBER_OF_STARS) {
			var name = nameGenerator.generate();
			if (!starNames.exists(name)) {
				starNames[name] = Noise;
				starCount++;
			}
		}

		var starLocations = new HashMap<Location, Noise>();
		starCount = 0;
		while (starCount < NUMBER_OF_STARS) {
			var theta = random.randomFloat(0.0, Math.PI * 2.0);
			var r = random.randomFloat(0.0, RADIUS);
			var x = (theta.sin() * r).roundBy(10.0);
			var y = (theta.cos() * r).roundBy(10.0);

			var p = {
				x: x,
				y: y,
				hashCode: () -> Std.int(x*100) ^ Std.int(y*100)
			};
			if (!starLocations.exists(p)) {
				starLocations[p] = Noise;
				starCount++;
			}
		}

		for (pair in starNames.keys().zip(starLocations.keys())) {
			var star = objectLoader.createObject(SStar);
			star.name = pair.a;
			star.x = pair.b.x;
			star.y = pair.b.y;
			star.brightness = random.randomFloat(0.0, 10.0).roundBy(10.0);
			star.asteroidsm = random.randomInt(10, 20);
			star.asteroidsc = random.randomInt(10, 20);
			star.moveTo(this);
		}
	}
}
