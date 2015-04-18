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
    val idOpt = sql"""select id from student limit 1""".map(_.long(1)).single.apply()
    val studentOpt = Student.find(idOpt.get)
    studentOpt.isDefined should be (true)
  }

  it should "find by specific kana, offset 0, limit 10" in { implicit session =>
    val students = Student.searchByKana(Some("いちろう"), 0, 10)
    students.length should be (4)
  }

  it should "find by unspecified kana, offset 5, limit 10" in { implicit session =>
    val students = Student.searchByKana(None, 5, 10)
    students.length should be (2)
  }

  it should "create if the class is defined" in { implicit session =>
    val student = Student.create("野口", "英雄", "のぐちひでお", 1, "1")
    val shouldNotNone = Student.find(student.id)
    shouldNotNone.get.grade.isDefined should be (true)
    shouldNotNone.get.clazz.isDefined should be (true)
  }

  it should "create throw IllegalArgumentException if the class is undefined " in { implicit session =>
    val undefinedClass = "2"
    an [IllegalArgumentException] should be thrownBy Student.create("野口", "英雄", "のぐちひでお", 1, undefinedClass)
  }
}