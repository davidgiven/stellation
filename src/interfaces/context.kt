package interfaces

import datastore.IDatabase

private var currentContext: IContext? = null

var context: IContext
    get() = currentContext!!
    set(c: IContext) { currentContext = c }

open class IContext {
    open val logger: ILogger? = null
    open val time: ITime? = null
    open val database: IDatabase? = null
}

