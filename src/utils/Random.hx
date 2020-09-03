package utils;

class Random {
	public function new() {}

    public function randomFloat(min: Float, max: Float): Float {
        return Math.random() * (max - min) + min;
    }

    public function randomInt(min: Int, max: Int): Int {
		return Std.int(randomFloat(min, max));
    }

	public function getRandomMember<T>(list: Array<T>): T {
		var i = randomInt(0, list.length);
		return list[i];
	}
}

