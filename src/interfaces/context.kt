package interfaces

fun context(): IContext = IContext.context

open class IContext {
    companion object {
        lateinit var context: IContext
    }

    open val logger: ILogger? = null
    open val time: ITime? = null
}

