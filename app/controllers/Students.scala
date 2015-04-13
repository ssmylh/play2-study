package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.utils._
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

  case class PostParams(lastName: String, firstName: String, kana: String,
    grade: Int, clazz: String)
  implicit val requestParamsReads: Reads[PostParams] = (
    (JsPath \ "lastName").read[String] and
    (JsPath \ "firstName").read[String] and
    (JsPath \ "kana").read[String] and
    (JsPath \ "grade").read[Int] and
    (JsPath \ "class").read[String])(PostParams.apply _)

  def show(id: Long) = Action {
    Student.find(id).map(student => Ok(Json.toJson(student))) getOrElse NotFound
  }

  def searchByKana(kana: Option[String], offset: Int, limit: Int) = Action {
    val students = Student.searchByKana(kana, offset, limit)
    Ok(Json.toJson(students))
  }

  def create = Action(BodyParsers.parse.json) { request =>
    request.body.validate[PostParams].fold(
      errors => BadRequest(JsError.toFlatJson(errors)),
      params => {
        val either = Student.create(params.lastName, params.firstName, params.kana, params.grade, params.clazz)
        either match {
          case Right(student) => Created(Json.toJson(student)).withHeaders(LOCATION -> UriEncoding.encodePathSegment(s"/students/${student.id}", "UTF-8"))
          case Left(e) => BadRequest // TODO should return proper status
        }
      })
  }
}