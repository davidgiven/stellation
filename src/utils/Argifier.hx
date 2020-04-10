package utils;

import haxe.iterators.StringIteratorUnicode;
import StringTools.contains;
import StringTools.replace;

class PeekableStringIteratorUnicode {
	var iterator: StringIteratorUnicode;
	var peeked = 0;

	public function new(input: String) {
		iterator = new StringIteratorUnicode(input);
	}

	public function hasNext(): Bool {
		return (peeked != 0) || iterator.hasNext();
	}

	public function next(): Int {
		if (peeked != 0) {
			var s = peeked;
			peeked = 0;
			return s;
		} else if (!iterator.hasNext()) {
			return 0;
		} else {
			return iterator.next();
		}
	}

	public function peek(): Int {
		if ((peeked == 0) && iterator.hasNext()) {
			peeked = iterator.next();
		}
		return peeked;
	}
}

class Argifier {
	static final BACKSLASH = 92;
	static final DQUOTE = 34;
	static final SQUOTE = 39;
	static final SPACE = 32;

	static function throwBadStringEscapeException(): Void {
		throw new Fault(SYNTAX).withDetail("bad string escape");
	}

	static function throwUnterminatedStringException(): Void {
		throw new Fault(SYNTAX).withDetail("unterminated string");
	}

	public static function argify(input: String): Array<String> {
		var iterator = new PeekableStringIteratorUnicode(input);
		var builders: Array<StringBuf> = [];
		var current: StringBuf = null;

		function isWhitespace(c: Int): Bool {
			return switch (c) {
				case 9 | 10 | 11 | 12 | 13 | 32: true;
				default: false;
			};
		}

		function consumeWhitespace() {
			while (iterator.hasNext()) {
				var c = iterator.peek();
				if (!isWhitespace(c)) {
					return;
				}
				iterator.next();
			}
		}

		function unescape(): Int {
			var c = iterator.next();
			return switch (c) {
				case BACKSLASH|DQUOTE|SQUOTE|SPACE:
					c;
				default:
					throwBadStringEscapeException();
					0;
			}
		}

		function appendText() {
			while (iterator.hasNext()) {
				var c = iterator.peek();
				if (isWhitespace(c) || (c == DQUOTE) || (c == SQUOTE)) {
					return;
				}
				iterator.next();
				if (c == BACKSLASH) {
					c = unescape();
				}

				current.addChar(c);
			}
		}

		function appendString() {
			var terminator = iterator.next();
			while (iterator.hasNext()) {
				var c = iterator.next();
				if (c == terminator) {
					return;
				}

				if ((c == BACKSLASH) && (terminator == DQUOTE)) {
					c = unescape();
				}

				current.addChar(c);
			}
			throwUnterminatedStringException();
		}

		while (iterator.hasNext()) {
			consumeWhitespace();
			if (!iterator.hasNext()) {
				break;
			}

			current = new StringBuf();
			builders.push(current);

			while (iterator.hasNext()) {
				var c = iterator.peek();
				if (isWhitespace(c)) {
					break;
				} else if ((c == DQUOTE) || (c == SQUOTE)) {
					appendString();
				} else {
					appendText();
				}
			}
		}

		return builders.map(sb -> sb.toString());
	}

	public static function unargify(argv: Array<String>): String {
		var words: Array<String> = [];
		for (arg in argv) {
			var word = arg;
			word = replace(word, "\\", "\\\\");
			word = replace(word, "\"", "\\\"");
			word = replace(word, "'", "\\'");
			if (contains(word, " ")) {
				word = '"' + word + '"';
			}
			words.push(word);
		}
		return words.join(" ");
	}
}

