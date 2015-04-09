package controllers

import play.api.mvc._
import play.api.libs.json._
import play.utils._
import models._

object Grades extends Controller {
  implicit val classWrites: Writes[Class] = new Writes[Class] {
    def writes(clazz: Class) = Json.obj(
      "name" -> clazz.name,
      "link" -> UriEncoding.encodePathSegment("/%d/%s".format(clazz.grade, clazz.name + ".json"), "UTF-8"))
  }
  implicit val gradeWrites: Writes[Grade] = new Writes[Grade] {
    def writes(grade: Grade) = Json.obj(
      "grade" -> grade.grade,
      "classes" -> grade.classes)
  }

  def show(grade: Int) = Action {
    Class.findByGrade(grade) match {
      case classes @ (_::_) => Ok(Json.toJson(Grade(grade, classes)))
      case _ => NotFound
    }
  }

}