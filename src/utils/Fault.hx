package utils;
import utils.FaultDomain.INTERNAL;
import haxe.Exception;

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

	public function new(domain: FaultDomain) {
		withDomain(domain);
		super(detail);
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

	public override function toString(): String {
		return '${detail}: ${domain}.${status}';
	}
}


//private const val STATUS = "status"
//private const val DOMAIN = "domain"
//private const val DETAIL = "detail"
//
//class Fault : Exception {
//    override val message: String?
//        get() = "$detail: $domain.$status"
//
//    constructor() : super()
//
//    constructor(e: Throwable?) : super(e)
//
//    constructor(domain: FaultDomain) : super() {
//        this.domain = domain
//    }
//
//    constructor(encoded: String) : super() {
//        val p = Message(encoded)
//        status = p.getInt(STATUS)
//        val domainOrdinal = p.getInt(DOMAIN)
//        domain = FaultDomain.values()[domainOrdinal]
//        detail = p[DETAIL]
//    }
//
//    fun serialise(): String {
//        val p = Message()
//        p.setInt(STATUS, status)
//        p.setInt(DOMAIN, domain.ordinal)
//        p[DETAIL] = detail
//        return p.serialise()
//    }
//}
//
