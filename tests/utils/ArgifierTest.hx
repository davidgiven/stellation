package utils;

import Tests.faultOf;
import utest.Assert;
import utils.Argifier.argify;
import utils.Argifier.unargify;
import utils.FaultDomain.SYNTAX;

class ArgifierTest extends TestCase {
    static final successfulCases = [
		"" => [],
		"foo" => ["foo"],
		"foo bar" => ["foo", "bar"],
		"foo bar baz" => ["foo", "bar", "baz"],
		"  foo   bar  baz  " => ["foo", "bar", "baz"],

		"'foo'" => ["foo"],
		"'f'o'o'" => ["foo"],
		"\"foo\"" => ["foo"],
		"\"f\"o\"o\"" => ["foo"],

		"'foo bar' baz" => ["foo bar", "baz"],
		"\"foo bar\" baz" => ["foo bar", "baz"],

		"foo\\ bar" => ["foo bar"],
		"foo\\\"bar" => ["foo\"bar"],
		"\"foo\\\"bar\"" => ["foo\"bar"]
    ];

    static final unterminatedStringCases = [
            "foo 'foo",
            "foo \"foo",
            "'foo\\'bar'",
            "'",
            "\""
    ];

    static final illegalEscapeCases = [
            "foo\\zbar",
            "foobar\\"
    ];

    function testSuccessfulParsing() {
		for (input => want in successfulCases) {
			var got = argify(input);
			Assert.same(got, want);
        }
    }

	function testUnterminatedStrings() {
		for (input in unterminatedStringCases) {
			var e = faultOf(() -> argify(input));
			Assert.same(e.domain, SYNTAX);
		}
	}

	function testIllegalEscapeCases() {
		for (input in illegalEscapeCases) {
			var e = faultOf(() -> argify(input));
			Assert.same(e.domain, SYNTAX);
		}
	}

    function testUnargify() {
        Assert.same(unargify(["foo", "bar"]), "foo bar");
        Assert.same(unargify(["foo bar"]), "\"foo bar\"");
        Assert.same(unargify(["\"foo"]), "\\\"foo");
        Assert.same(unargify(["'foo"]), "\\'foo");
    }
}

