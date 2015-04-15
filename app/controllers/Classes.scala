package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.utils._
import models._

object Classes extends Controller {
  implicit val studentWrites: Writes[Student] = new Writes[Student] {
    def writes(student: Student) = Json.obj(
      "id" -> student.id,
      "lastName" -> student.lastName,
      "firstName" -> student.firstName,
      "kana" -> student.kana,
      "grade" -> student.grade.fold(0)(identity),
      "class" -> student.clazz.fold("-")(identity)
      )
  }
  implicit val classWrites: Writes[Class] = new Writes[Class] {
    def writes(clazz: Class) = Json.obj(
      "grade" -> clazz.grade,
      "class" -> clazz.name,
      "students" -> clazz.students)
  }

  def show(grade: Int, name: String) = Action {
    Class.findByGradeAndName(grade, name).map(clazz => Ok(Json.toJson(clazz))) getOrElse NotFound
  }

  def create(grade: Int) = Action(BodyParsers.parse.json) { request =>
    (request.body \ "class").asOpt[String] match {
      case Some(name) =>
        val clazz = Class.create(grade, name)
        Created(Json.toJson(clazz)).withHeaders(LOCATION -> UriEncoding.encodePathSegment(s"/${grade}/${name}", "UTF-8"))
      case None => BadRequest
    }
  }
}