package datastore

expect fun setTimer(oid: Oid, expiry: Long)
expect fun processTimer(max_time: Long, callback: (Oid, Long) -> Unit)
