package models

import scalikejdbc._

case class Student(id: Long, lastName: String, firstName: String, kana: String,
  grade: Option[Int] = None, clazz: Option[String] = None) {
  def update()(implicit session: DBSession = Student.autoSession): Student = Student.update(this)(session)
}
object Student extends SQLSyntaxSupport[Student] {
  val s = Student.syntax("s")
  val c2s = Class2Student.c2s
  val c = Class.c
  private val c2sColumun = Class2Student.column

  def apply(s: ResultName[Student])(rs: WrappedResultSet): Student = new Student(
    id = rs.get(s.id),
    lastName = rs.get(s.lastName),
    firstName = rs.get(s.firstName),
    kana = rs.get(s.kana))
  def apply(s: SyntaxProvider[Student])(rs: WrappedResultSet): Student = apply(s.resultName)(rs)
  def opt(s: SyntaxProvider[Student])(rs: WrappedResultSet): Option[Student] = rs.longOpt(s.resultName.id).map(_ => apply(s)(rs))

  def apply(s: SyntaxProvider[Student], c: SyntaxProvider[Class])(rs: WrappedResultSet): Student = {
    val student = apply(s)(rs)
    rs.longOpt(c.resultName.id) match {
      case Some(_) =>
        val clazz = Class(c)(rs)
        student.copy(grade = Some(clazz.grade), clazz = Some(clazz.name))
      case None => student
    }
  }

  def apply(student: Student, clazz: Class): Student = student.copy(grade = Some(clazz.grade), clazz = Some(clazz.name))

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Student] = withSQL {
    select
      .from(Student as s)
      .leftJoin(Class2Student as c2s).on(s.id, c2s.studentId)
      .leftJoin(Class as c).on(c2s.classId, c.id)
      .where.eq(s.id, id)
  }.map(Student(s, c)).single.apply()

  // escaped like SQLSyntax for kana
  private def kanaLike(kana: Option[String]): Option[SQLSyntax] = kana.map(n => sqls.like(s.kana, s"%${LikeConditionEscapeUtil.escape(n)}%"))
  private def searchByName(nameSQLSyntax: Option[String] => Option[SQLSyntax],
    name: Option[String], offset: Int, limit: Int)(implicit sesstion: DBSession = autoSession): List[Student] = withSQL {
    select
      .from(Student as s)
      .leftJoin(Class2Student as c2s).on(s.id, c2s.studentId)
      .leftJoin(Class as c).on(c2s.classId, c.id)
      .where(nameSQLSyntax(name))
      .limit(limit)
      .offset(offset)
  }.map(Student(s, c)).list.apply()

  def searchByKana(kana: Option[String], offset: Int, limit: Int)(implicit sesstion: DBSession = autoSession): List[Student] =
    searchByName(kanaLike, kana, offset, limit)

  def create(lastName: String, firstName: String, kana: String,
    grade: Int, clazz: String)(implicit session: DBSession = autoSession): Student = {
    Class.findByGradeAndName(grade, clazz) match {
      case None => throw new IllegalArgumentException("Class is not found.")
      case Some(k) => {
        val id = withSQL {
          insert.into(Student).namedValues(
            column.lastName -> lastName,
            column.firstName -> firstName,
            column.kana -> kana)
        }.updateAndReturnGeneratedKey.apply()

        withSQL {
          insert.into(Class2Student).namedValues(
            c2sColumun.classId -> k.id,
            c2sColumun.studentId -> id)
        }.update.apply()

        Student(id = id, lastName = lastName, firstName = firstName, kana = kana, grade = Some(grade), clazz = Some(clazz))
      }
    }
  }

  def update(student: Student)(implicit session: DBSession = autoSession): Student = {
    withSQL {
      QueryDSL.update(Student).set(
        column.lastName -> student.lastName,
        column.firstName -> student.firstName,
        column.kana -> student.kana
      ).where.eq(column.id, student.id)
    }.update().apply()
    student
  }

  def delete(id: Long)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      QueryDSL.delete.from(Student).where.eq(column.id, id)
    }.update().apply()
    Class2Student.deleteByStudentId(id)
  }
}