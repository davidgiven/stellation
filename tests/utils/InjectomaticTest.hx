package utils;

import haxe.unit.TestCase;
import utils.Injectomatic.InjectomaticException;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Injectomatic.resetBindingsForTest;
import Tests.exceptionOf;

class Superclass {
	public function new() {}
}

class Subclass extends Superclass {
	public function new() {
		super();
	}
}

class InjectomaticTest extends TestCase {
	override function setup() {
		resetBindingsForTest();
	}

	public function testSimple() {
		var t = new Superclass();

		bind(Superclass, t);
		assertEquals(t, inject(Superclass));
	}

	public function testComplex() {
		var t = new Subclass();

		bind(Superclass, t);
		var got = inject(Superclass);
		assertEquals(got, inject(Superclass));
	}

	public function testMultipleBindingsFail() {
		var e = exceptionOf(() -> {
			bind(Superclass, new Superclass());
			bind(Superclass, new Subclass());
		});

		assertTrue(Std.is(e, InjectomaticException));
	}
}

