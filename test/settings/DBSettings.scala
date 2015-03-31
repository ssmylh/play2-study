package settings

import scalikejdbc.config._

trait DBSettings {
  val dbName = 'test
  DBSettings.initialize(dbName)
}

object DBSettings {
  private val lock = new AnyRef
  private var initialized: Boolean = _
  def initialize(dbName: Symbol): Unit = lock.synchronized {
    if (initialized) return
    DBs.setupAll
    DBInitializer.run(dbName)
    initialized = true
  }
}