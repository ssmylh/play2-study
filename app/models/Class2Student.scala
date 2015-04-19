package models

import scalikejdbc._

case class Class2Student(classId: Long, studentId: Long)
object Class2Student extends SQLSyntaxSupport[Class2Student] {
  override val tableName = "class2student"
  val c2s = Class2Student.syntax("c2s")

  def deleteByStudentId(studentId: Long)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(Class2Student).where.eq(column.studentId, studentId)
    }.update().apply()
  }

  def deleteByClassId(classId: Long)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      delete.from(Class2Student).where.eq(column.classId, classId)
    }.update().apply()
  }
}