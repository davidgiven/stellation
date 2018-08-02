package utils

enum class FaultDomain {
    INTERNAL,
    NETWORK,
    PERMISSION,
    VISIBILITY,
    SYNTAX,
    INVALID_ARGUMENT,
}

private const val STATUS = "status"
private const val DOMAIN = "domain"
private const val DETAIL = "detail"

class Fault : Exception {
    var status = 500
    var domain = FaultDomain.INTERNAL
    var detail = "Fault"

    fun withDomain(domain: FaultDomain): Fault {
        this.domain = domain
        return this
    }

    fun withDetail(detail: String): Fault {
        this.detail = detail
        return this
    }

    fun withStatus(status: Int): Fault {
        this.status = status
        return this
    }

    override val message: String?
        get() = "$detail: $domain.$status"

    constructor() : super()

    constructor(e: Throwable?) : super(e)

    constructor(domain: FaultDomain) : super() {
        this.domain = domain
    }

    constructor(encoded: String) : super() {
        val codec = inject<Codec>()
        val p = Message()
        p.setFromMap(codec.decode(encoded))
        status = p[STATUS]
        val domainOrdinal: Int = p[DOMAIN]
        domain = FaultDomain.values()[domainOrdinal]
        detail = p[DETAIL]
    }

    fun serialise(): String {
        val p = Message()
        p[STATUS] = status
        p[DOMAIN] = domain.ordinal
        p[DETAIL] = detail

        val codec = inject<Codec>()
        return codec.encode(p.toMap())
    }
}
