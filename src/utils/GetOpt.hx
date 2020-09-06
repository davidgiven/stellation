package utils;

using StringTools;
using utils.ArrayTools;
using utils.Flags;

class GetOpt {
	public static function getopt(argv: Array<String>, flags: Flags): Array<String> {
		var remaining: Array<String> = [];
		var index = 0;
		while (index < argv.length) {
			var arg = argv[index];

			var key: String;
			var value: String;
			var consume = false;
			if (arg.startsWith("--")) {
				/* --foo bar */
				var equals = arg.indexOf("=");
				if (equals == -1) {
					key = arg;
					value = argv.getOrElse(index + 1, "");
					consume = true;
				} else {
					key = arg.substring(0, equals);
					value = arg.substring(equals+1);
				}
			} else if (arg.startsWith("-")) {
				/* -fbar or -f bar */
				if (arg.length == 2) {
					key = arg;
					value = argv.getOrElse(index + 1, "");
					consume = true;
				} else {
					key = arg.substring(0, 2);
					value = arg.substring(2);
				}
			} else {
				remaining = argv.slice(index);
				break;
			}

			var flag = flags.map[key];
			if (flag == null) {
				throw Flags.unrecognisedFlagException(key);
			}
			var consumed = flag.set(value);
			if (consume) {
				if (consumed) {
					index++;
				}
				if (index >= argv.length) {
					throw Flags.missingFlagException(key);
				}
			}
			index++;
		}

		if (flags.remainingSetter != null) {
			if (remaining.length != flags.expectRemaining) {
				throw Flags.expectParameters(flags.expectRemaining, remaining.length);
			}
			flags.remainingSetter(remaining);
			return null;
		} else {
			return remaining;
		}
	}
}

