package utils;

import org.hamcrest.Matchers.assertThat;
import org.hamcrest.Matchers.containsString;
import org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matchers.isEmpty;
import utils.FaultDomain.SYNTAX;
import utils.Flags;
import utils.GetOpt.getopt;

class GetOptTest extends TestCase {
	public var vFlag: Bool;
	public var fFlag: String;
	public var verboseFlag: Bool;
	public var fileFlag: String;
	public var intFlag: Int;
	public var boolFlag: Bool;

	var options: Flags;

	override function setup() {
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
        assertFalse(vFlag);
        assertThat(remaining, isEmpty());
    }

	function testShortFlag() {
		var remaining = getopt(["-v"], options);
        assertTrue(vFlag);
        assertThat(remaining, isEmpty());
    }

	function testLongFlag() {
		var remaining = getopt(["--verbose"], options);
        assertTrue(verboseFlag);
        assertThat(remaining, isEmpty());
    }

	function testShortWithInlineFlag() {
		var remaining = getopt(["-fFNORD", "-v"], options);
		assertEquals("FNORD", fFlag);
        assertTrue(vFlag);
        assertThat(remaining, isEmpty());
    }

	function testShortWithOutoflineFlag() {
		var remaining = getopt(["-f", "FNORD", "-v"], options);
		assertEquals("FNORD", fFlag);
        assertTrue(vFlag);
        assertThat(remaining, isEmpty());
    }

	function testLongWithInlineFlag() {
		var remaining = getopt(["--file=FNORD", "-v"], options);
		assertEquals("FNORD", fileFlag);
        assertTrue(vFlag);
        assertThat(remaining, isEmpty());
    }

	function testLongWithOutoflineFlag() {
		var remaining = getopt(["--file", "FNORD", "-v"], options);
		assertEquals("FNORD", fileFlag);
        assertTrue(vFlag);
        assertThat(remaining, isEmpty());
    }

	function testWithNonParameterFirst() {
        var remaining = getopt(["FNORD", "--verbose"], options);
        assertFalse(verboseFlag);
        assertThat(remaining, equalTo(["FNORD", "--verbose"]));
    }

	function testWithNonParameterLast() {
        var remaining = getopt(["--verbose", "FNORD", "--verbose"], options);
        assertTrue(verboseFlag);
        assertThat(remaining, equalTo(["FNORD", "--verbose"]));
    }

	function testMissingShortParameter() {
		try {
			getopt(["-f"], options);
			fail("exception not thrown");
		} catch (f: Fault) {
			assertEquals(FaultDomain.SYNTAX, f.domain);
			assertThat(f.detail, containsString("missing"));
		}
	}

	function testMissingLongParameter() {
		try {
			getopt(["--file"], options);
			fail("exception not thrown");
		} catch (f: Fault) {
			assertEquals(FaultDomain.SYNTAX, f.domain);
			assertThat(f.detail, containsString("missing"));
		}
	}

	function testInvalidShortParameter() {
		try {
			getopt(["-x"], options);
			fail("exception not thrown");
		} catch (f: Fault) {
			assertEquals(FaultDomain.SYNTAX, f.domain);
			assertThat(f.detail, containsString("unrecognised"));
		}
	}

	function testInvalidLongParameter() {
		try {
			getopt(["--does-not-exist"], options);
			fail("exception not thrown");
		} catch (f: Fault) {
			assertEquals(FaultDomain.SYNTAX, f.domain);
			assertThat(f.detail, containsString("unrecognised"));
		}
	}
}

