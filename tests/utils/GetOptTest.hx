package utils;

import utils.FaultDomain.SYNTAX;
import utils.Flags;
import utils.GetOpt.getopt;
import utest.Assert;

class GetOptTest extends TestCase {
	public var vFlag: Bool;
	public var fFlag: String;
	public var verboseFlag: Bool;
	public var fileFlag: String;
	public var intFlag: Int;
	public var boolFlag: Bool;

	var options: Flags;

	function setup() {
		options = new Flags()
				.addFlag("-v", (v) -> vFlag = true)
				.addString("-f", (v) -> fFlag = v)
				.addFlag("--verbose", (v) -> verboseFlag = true)
				.addString("--file", (v) -> fileFlag = v)
				.addInt("--int", (v) -> intFlag = v)
				.addBoolean("-b", (v) -> boolFlag = v);

		vFlag = false;
		fFlag = "";
		verboseFlag = false;
		fileFlag = "";
		intFlag = 0;
		boolFlag = false;
	}

	
    function testEmpty() {
        var remaining = getopt([], options);
        Assert.isFalse(vFlag);
        Assert.same(remaining, []);
    }

	function testShortFlag() {
		var remaining = getopt(["-v"], options);
        Assert.isTrue(vFlag);
        Assert.same(remaining, []);
    }

	function testLongFlag() {
		var remaining = getopt(["--verbose"], options);
        Assert.isTrue(verboseFlag);
        Assert.same(remaining, []);
    }

	function testShortWithInlineFlag() {
		var remaining = getopt(["-fFNORD", "-v"], options);
		Assert.same("FNORD", fFlag);
        Assert.isTrue(vFlag);
        Assert.same(remaining, []);
    }

	function testShortWithOutoflineFlag() {
		var remaining = getopt(["-f", "FNORD", "-v"], options);
		Assert.same("FNORD", fFlag);
        Assert.isTrue(vFlag);
        Assert.same(remaining, []);
    }

	function testLongWithInlineFlag() {
		var remaining = getopt(["--file=FNORD", "-v"], options);
		Assert.same("FNORD", fileFlag);
        Assert.isTrue(vFlag);
        Assert.same(remaining, []);
    }

	function testLongWithOutoflineFlag() {
		var remaining = getopt(["--file", "FNORD", "-v"], options);
		Assert.same("FNORD", fileFlag);
        Assert.isTrue(vFlag);
        Assert.same(remaining, []);
    }

	function testWithNonParameterFirst() {
        var remaining = getopt(["FNORD", "--verbose"], options);
        Assert.isFalse(verboseFlag);
        Assert.same(remaining, ["FNORD", "--verbose"]);
    }

	function testWithNonParameterLast() {
        var remaining = getopt(["--verbose", "FNORD", "--verbose"], options);
        Assert.isTrue(verboseFlag);
        Assert.same(["FNORD", "--verbose"], remaining);
    }

	function testMissingShortParameter() {
		try {
			getopt(["-f"], options);
			Assert.fail("exception not thrown");
		} catch (f: Fault) {
			Assert.same(FaultDomain.SYNTAX, f.domain);
			Assert.stringContains("missing", f.detail);
		}
	}

	function testMissingLongParameter() {
		try {
			getopt(["--file"], options);
			Assert.fail("exception not thrown");
		} catch (f: Fault) {
			Assert.same(FaultDomain.SYNTAX, f.domain);
			Assert.stringContains("missing", f.detail);
		}
	}

	function testInvalidShortParameter() {
		try {
			getopt(["-x"], options);
			Assert.fail("exception not thrown");
		} catch (f: Fault) {
			Assert.same(FaultDomain.SYNTAX, f.domain);
			Assert.stringContains("unrecognised", f.detail);
		}
	}

	function testInvalidLongParameter() {
		try {
			getopt(["--does-not-exist"], options);
			Assert.fail("exception not thrown");
		} catch (f: Fault) {
			Assert.same(FaultDomain.SYNTAX, f.domain);
			Assert.stringContains("unrecognised", f.detail);
		}
	}
}

