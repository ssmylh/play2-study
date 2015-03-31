package controllers

import play.api.mvc._
import play.api.libs.json._
import models._

object Classes extends Controller {
  implicit val studentWrites: Writes[Student] = new Writes[Student] {
    def writes(student: Student) = Json.obj(
      "id" -> student.id,
      "lastName" -> student.lastName,
      "firstName" -> student.firstName,
      "kana" -> student.kana,
      "grade" -> student.grade.fold("-")("%d年".format(_)),
      "class" -> student.clazz.fold("-")(identity)
      )
  }
  implicit val classWrites: Writes[Class] = new Writes[Class] {
    def writes(clazz: Class) = Json.obj(
      "grade" -> "%d年".format(clazz.grade),
      "name" -> clazz.name,
      "students" -> clazz.students)
  }

  def show(grade: Int, name: String) = Action {
    Class.findByGradeAndName(grade, name).map(clazz => Ok(Json.toJson(clazz))) getOrElse NotFound
  }

}