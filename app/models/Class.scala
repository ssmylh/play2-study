package models

import scalikejdbc._

case class Class(id: Long, grade: Int, name: String, students: Seq[Student] = Nil)
object Class extends SQLSyntaxSupport[Class] {
  val c = Class.syntax("c")
  val s = Student.s
  val c2s = Class2Student.c2s

  def apply(c: ResultName[Class])(rs: WrappedResultSet): Class = new Class(
    id = rs.get(c.id),
    grade = rs.get(c.grade),
    name = rs.get(c.name))
  def apply(c: SyntaxProvider[Class])(rs: WrappedResultSet): Class = apply(c.resultName)(rs)

  // Find Classes by grade. This does not include students.
  def findByGrade(grade: Int)(implicit session: DBSession = autoSession): List[Class] = withSQL {
    select.from(Class as c)
      .where.eq(c.grade, grade)
      .orderBy(c.name)
  }.map(Class(c)).list.apply()

  def findByGradeAndName(grade: Int, name: String)(implicit session: DBSession = autoSession): Option[Class] = withSQL {
    select
      .from(Class as c)
      .leftJoin(Class2Student as c2s).on(c.id, c2s.classId)
      .leftJoin(Student as s).on(c2s.studentId, s.id)
      .where.eq(c.grade, grade).and.eq(c.name, name)
  }.one(Class(c))
    .toMany(Student.opt(s))
    .map { (clazz, students) => clazz.copy(students = students.map(Student(_, clazz))) }
    .single.apply()

  def put(grade: Int, name: String)(implicit session: DBSession = autoSession): Class = {
    // TODO grade validation
    findByGradeAndName(grade, name) match {
      case Some(clazz) => clazz
      case None => create(grade, name)
    }
  }

  def create(grade: Int, name: String)(implicit session: DBSession = autoSession): Class = {
    val id = withSQL {
      insert.into(Class).namedValues(
        column.grade -> grade,
        column.name -> name)
    }.updateAndReturnGeneratedKey.apply()
    Class(id = id, grade = grade, name = name)
  }
}
