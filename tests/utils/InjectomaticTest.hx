package utils;

import utils.Injectomatic.InjectomaticException;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Injectomatic.resetBindingsForTest;
import Tests.exceptionOf;
import utest.Assert;

class Superclass {
	public function new() {}
}

class Subclass extends Superclass {
	public function new() {
		super();
	}
}

class InjectomaticTest extends TestCase {
	function setup() {
		resetBindingsForTest();
	}

	public function testSimple() {
		var t = new Superclass();

		bind(Superclass, t);
		Assert.equals(t, inject(Superclass));
	}

	public function testComplex() {
		var t = new Subclass();

		bind(Superclass, t);
		var got = inject(Superclass);
		Assert.equals(got, inject(Superclass));
	}

	public function testMultipleBindingsFail() {
		var e = exceptionOf(() -> {
			bind(Superclass, new Superclass());
			bind(Superclass, new Subclass());
		});

		Assert.isOfType(e, InjectomaticException);
	}
}

