package datastore

import utils.UNIMPLEMENTED

actual fun setTimer(oid: Oid, expiry: Long): Unit = UNIMPLEMENTED()
actual fun processTimer(max_time: Long, callback: (Oid, Long) -> Unit): Unit = UNIMPLEMENTED()
