package models

import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.fixture.FlatSpec
import org.scalatest.Matchers._

class ClassSpec extends FlatSpec with AutoRollback with settings.DBSettings {

  override def fixture(implicit session: DBSession): Unit = {
    val clazzId = sql"""insert into class (grade, name) values (1, '1');""".updateAndReturnGeneratedKey.apply()
    val student1Id = sql"""insert into student (last_name, first_name, kana) values ('山田', '一郎', 'やまだいちろう');""".updateAndReturnGeneratedKey.apply()
    val student2Id = sql"""insert into student (last_name, first_name, kana) values ('山田', '二郎', 'やまだじろう');""".updateAndReturnGeneratedKey.apply()
    val student3Id = sql"""insert into student (last_name, first_name, kana) values ('山田', '三郎', 'やまださぶろう');""".updateAndReturnGeneratedKey.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student1Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student2Id});""".execute.apply()
    sql"""insert into class2student (class_id, student_id) values (${clazzId}, ${student3Id});""".execute.apply()
  }

  behavior of "Class"

  it should "find by grade" in { implicit session =>
    val classes = Class.findByGrade(1)
    classes.length should be(1)
  }

  it should "find by grade and name" in { implicit session =>
    val some = Class.findByGradeAndName(1, "1")
    some.get.students.length should be(3)
  }

  it should "create" in { implicit session =>
    val clazz = Class.create(1, "2")
    clazz.name should be ("2")
  }
}