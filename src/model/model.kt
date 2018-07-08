package model

import interfaces.IDatastore
import utils.get

class Model(val datastore: IDatastore = get())
