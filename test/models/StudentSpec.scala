package models

import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.fixture.FlatSpec
import org.scalatest.Matchers._
import controllers.Students

class StudentSpec extends FlatSpec with AutoRollback with settings.DBSettings {

  override def fixture(implicit session: DBSession): Unit = {
    val clazzId = sql"""insert into class (grade, name) values (1, '1');""".updateAndReturnGeneratedKey.apply()
    val student1Id = sql"""insert into student (last_name, first_name, kana) values ('山田', '一郎', 'やまだいちろう');""".updateAndReturnGeneratedKey.apply()
    val student2Id = sql"""insert into student (last_name, first_name, kana) values ('山田', '二郎', 'やまだじろう');""".updateAndReturnGeneratedKey.apply()
    val student3Id = sql"""insert into student (last_name, first_name, kana) values ('鈴木', '一郎', 'すずきいちろう');""".updateAndReturnGeneratedKey.apply()
    val student4Id = sql"""insert into student (last_name, first_name, kana) values ('佐藤', '一郎', 'さとういちろう');""".updateAndReturnGeneratedKey.apply()
    val student5Id = sql"""insert into student (last_name, first_name, kana) values ('佐藤', '二郎', 'さとうじろう');""".updateAndReturnGeneratedKey.apply()
    val student6Id = sql"""insert into student (last_name, first_name, kana) values ('田中', '一郎', 'たなかいちろう');""".updateAndReturnGeneratedKey.apply()
    val student7Id = sql"""insert into student (last_name, first_name, kana) values ('田中', '邦衛', 'たなかくにえ');""".updateAndReturnGeneratedKey.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student1Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student2Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student3Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student4Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student5Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student6Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student7Id});""".execute.apply()
  }

  behavior of "Student"

  it should "find by id" in { implicit session =>
    val id = sql"""select id from student limit 1""".map(_.long(1)).single.apply().get
    val some = Student.find(id)
    some.isDefined should be (true)
  }

  it should "find by specific kana, offset 0, limit 10" in { implicit session =>
    val students = Student.searchByKana(Some("いちろう"), 0, 10)
    students.length should be (4)
  }

  it should "find by unspecified kana, offset 5, limit 10" in { implicit session =>
    val students = Student.searchByKana(None, 5, 10)
    students.length should be > 0
    students.length should be <= 10
  }

  it should "create if the class is defined" in { implicit session =>
    val student = Student.create("野口", "英雄", "のぐちひでお", 1, "1")
    val some = Student.find(student.id)
    some.get.grade.isDefined should be (true)
    some.get.clazz.isDefined should be (true)
  }

  it should "create throw IllegalArgumentException if the class is undefined " in { implicit session =>
    val undefinedClass = "2"
    an [IllegalArgumentException] should be thrownBy Student.create("野口", "英雄", "のぐちひでお", 1, undefinedClass)
  }

  it should "update" in { implicit session =>
    val student = Student.create("佐々木", "望", "ささきのぞみ", 1, "1")
    val updated = student.copy(kana = "ささきのぞむ").update()
    updated.kana should be ("ささきのぞむ")
  }

  it should "delete by id" in {implicit session =>
    val student = Student.create("小島", "一正", "こじまかずまさ", 1, "1")
    Student.delete(student.id)
    Student.find(student.id).isEmpty should be (true)

    val c2sNone = sql"""select class_id from class2student where student_id = ${student.id}"""
      .map(rs => rs.long("class_id")).single().apply()
    c2sNone.isEmpty should be (true)
  }
}