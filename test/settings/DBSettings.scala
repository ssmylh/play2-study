package settings

import scalikejdbc._
import scalikejdbc.config._

trait DBSettings {
  DBSettings.initialize("test")
}

object DBSettings {
  private val lock = new AnyRef
  private var initialized: Boolean = _
  def initialize(env: String): Unit = lock.synchronized {
    if (initialized) return
    DBsWithEnv(env).setupAll()
    DBInitializer.run()
    initialized = true
  }
}