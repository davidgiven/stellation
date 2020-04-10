package utils;

import Tests.faultOf;
import org.hamcrest.Matchers.assertThat;
import org.hamcrest.Matchers.equalTo;
import utils.Argifier.argify;
import utils.Argifier.unargify;
import utils.Fault.FaultDomain.SYNTAX;

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
			assertThat(got, equalTo(want));
        }
    }

	function testUnterminatedStrings() {
		for (input in unterminatedStringCases) {
			var e = faultOf(() -> argify(input));
			assertThat(e.domain, equalTo(SYNTAX));
		}
	}

	function testIllegalEscapeCases() {
		for (input in illegalEscapeCases) {
			var e = faultOf(() -> argify(input));
			assertThat(e.domain, equalTo(SYNTAX));
		}
	}

    function testUnargify() {
        assertThat(unargify(["foo", "bar"]), equalTo("foo bar"));
        assertThat(unargify(["foo bar"]), equalTo("\"foo bar\""));
        assertThat(unargify(["\"foo"]), equalTo("\\\"foo"));
        assertThat(unargify(["'foo"]), equalTo("\\'foo"));
    }
}

