package;

import haxe.PosInfos;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import utils.Exception;

class TestCase extends haxe.unit.TestCase {
	public function assertThat<T>(actual: Dynamic, ?matcher: Matcher<T>, ?reason: String, ?info: PosInfos)
	{
		currentTest.done = true;

		if (reason == null) {
			reason = "";
		}
		
		if (matcher != null) {
			if (!matcher.matches(actual)) {
				var description = new StringDescription();
					description.appendText(reason)
								.appendText("\nExpected: ")
								.appendDescriptionOf(matcher)
								.appendText("\n     but: ");
				matcher.describeMismatch(actual, description);

				fail(description.toString(), info);
			}
		} else if (Std.is(actual, Bool)) {
			if (!actual) {
				fail(reason, info);
			}
		} else {
			throw new Exception("If no Matcher defined then 'actual' must be of type Bool.");
		}
	}

	private function fail(?reason: String, ?c: PosInfos) {
		currentTest.success = false;
		currentTest.error   = reason;
		currentTest.posInfos = c;
		throw currentTest;
	}
}

