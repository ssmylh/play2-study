package controllers

import play.api.mvc._
import play.api.libs.json._
import models._

object Students extends Controller {
  implicit val studentWrites: Writes[Student] = new Writes[Student] {
    def writes(student: Student) = Json.obj(
      "id" -> student.id,
      "lastName" -> student.lastName,
      "firstName" -> student.firstName,
      "kana" -> student.kana,
      "grade" -> student.grade.fold("-")(_.toString),
      "class" -> student.clazz.fold("-")(identity))
  }

  def show(id: Long) = Action {
    Student.find(id).map(student => Ok(Json.toJson(student))) getOrElse NotFound
  }

  def searchByKana(kana: Option[String], offset: Int, limit: Int) = Action {
    val students = Student.searchByKana(kana, offset, limit)
    Ok(Json.toJson(students))
  }

}