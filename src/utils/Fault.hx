package utils;
import utils.FaultDomain.INTERNAL;
import haxe.Exception;
import haxe.CallStack;
import haxe.io.Output;
import haxe.io.BytesOutput;
using StringTools;
using haxe.EnumTools;

typedef SerialisedFault = {
	domain: Int,
	detail: String,
	status: Int,
}

@:tink
class Fault extends Exception {
	@:calc public static final UNIMPLEMENTED = new Fault(INTERNAL).withDetail("unimplemented operation");
	@:calc public static final AUTH_FAILED = new Fault(PERMISSION).withStatus(401).withDetail("authentication failed");
	@:calc public static final NOBODY_LOGGED_IN = new Fault(PERMISSION).withStatus(401).withDetail("nobody is logged in");
	@:calc public static final PERMISSION_DENIED = new Fault(PERMISSION).withStatus(403).withDetail("you're not allowed to do that");
	@:calc public static final PROTOCOL = new Fault(INTERNAL).withStatus(403).withDetail("protocol error");

	public var status = 500;
	public var domain = INTERNAL;
	public var detail = "Fault";
	public var stackTrace: Array<StackItem>;
	public var cause: Fault = null;

	public static function wrap(e: Exception): Fault {
		return new Fault(INTERNAL).withException(e);
	}

	public function new(domain: FaultDomain = INTERNAL) {
		super(detail);
		withDomain(domain);
		stackTrace = CallStack.exceptionStack();
	}

	public function withDomain(domain: FaultDomain): Fault {
		this.domain = domain;
		return this;
	}

	public function withDetail(detail: String): Fault {
		this.detail = detail;
		return this;
	}

	public function withStatus(status: Int): Fault {
		this.status = status;
		return this;
	}

	public function withCause(cause: Fault): Fault {
		this.cause = cause;
		return this;
	}

	public function withException(exception: Exception): Fault {
		this.detail = exception.message;
		this.stackTrace = [];
		var i = 0;
		while (i < exception.stack.length) {
			this.stackTrace[i] = exception.stack[i];
			i++;
		}
		return this;
	}

	public override function toString(): String {
		return '${detail}: ${domain}.${status}';
	}

	public function rethrow(): Fault {
		return new Fault(domain).withStatus(status).withDetail(detail).withCause(this);
	}
	
	public function dumpStackTrace(output: Output): Void {
		var f = this;
		while (f != null) {
			for (item in CallStack.toString(f.stackTrace).split("\n")) {
				if (item == "") continue;
				output.writeString("  ");
				output.writeString(item.replace("Called from", "| at"));
				output.writeString("\n");
			}
			f = f.cause;
			if (f != null) {
				output.writeString("Caused by:\n");
			}
		}
	}

	public function getStackTrace(): String {
		var bo = new BytesOutput();
		dumpStackTrace(bo);
		return bo.getBytes().toString();
	}

	public function serialise(): SerialisedFault {
		return {
			status: status,
			domain: domain.getIndex(),
			detail: detail
		};
	}

	public function withSerialisedFault(f: SerialisedFault): Fault {
		this.status = f.status;
		this.domain = FaultDomain.createByIndex(f.domain);
		this.detail = f.detail;
		return this;
	}
}

