package server

import interfaces.IAuthenticator
import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.IEnvironment
import interfaces.ISyncer
import interfaces.ITime
import interfaces.IUtf8
import model.Model
import model.SThing
import model.SUniverse
import model.Timers
import model.createNewUniverse
import runtime.shared.CommandShell
import utils.Fault
import utils.injection

abstract class AbstractHandler {
    protected val authenticator by injection<IAuthenticator>()
    protected val clock by injection<IClock>()
    protected val database by injection<IDatabase>()
    protected val datastore by injection<IDatastore>()
    protected val environment by injection<IEnvironment>()
    protected val model by injection<Model>()
    protected val remoteServer by injection<RemoteServer>()
    protected val syncer by injection<ISyncer>()
    protected val time by injection<ITime>()
    protected val utf8 by injection<IUtf8>()
    protected val commandshell by injection<CommandShell>()
    protected val timers by injection<Timers>()

    protected fun findUniverse(model: Model) = model.loadObject(1, SUniverse::class)

    protected fun findOrCreateUniverse(model: Model): SUniverse {
        try {
            return findUniverse(model)
        } catch (f: Fault) {
            return model.createNewUniverse()
        }
    }

    protected fun catchup() {
        val realtime = time.realtime()
        timers.processTimers(realtime) { oid, servertime ->
            clock.setTime(servertime)

            model.loadObject(oid, SThing::class).onTimerExpiry()
        }
        clock.setTime(realtime)
    }
}
