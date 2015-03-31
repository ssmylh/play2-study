package models

import scalikejdbc._

case class Class2Student(classId: Long, studentId: Long)
object Class2Student extends SQLSyntaxSupport[Class2Student] {
  override val tableName = "class2student"
  val c2s = Class2Student.syntax("c2s")
}