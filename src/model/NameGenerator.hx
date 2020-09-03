package model;

import utils.Random;
import utils.Injectomatic.inject;

class NameGenerator {
	private static final syllables1 = [
			"An", "Ca", "Jo", "Ka", "Kri", "Da", "Re", "De", "Ed", "Ma", "Ni",
			"Qua", "Qa", "Li", "La", "In", "On", "An", "Un", "Ci", "Cu", "Ce",
			"Co", "Xa", "Xef", "Xii", "Xo'o", "Xu", "Ram", "Noq", "Mome", "Pawa",
			"Limi", "Ney"
	];

	private static final syllables2 = [
			"the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"
	];

	private static final syllables3 = [
			"drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven",
			"cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan",
			"sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"
	];

	private var random = inject(Random);

	public function new() {}

	public function generate(): String {
        var sb = new StringBuf();
        sb.add(random.getRandomMember(syllables1));
        if (random.randomInt(0, 1) == 1) {
            sb.add(random.getRandomMember(syllables2));
        }
        sb.add(random.getRandomMember(syllables3));
        return sb.toString();
    }
}

