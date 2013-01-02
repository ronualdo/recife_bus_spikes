package controllers

import play.api._
import play.api.db._
import play.api.mvc._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import recifeBuses._
import models._

object Application extends Controller {

  def index = Action {
  Route.deleteAll()
  val routes = PageScraper.parse
  routes.foreach(Route.insert(_))
    val rroutes = DB.withConnection { implicit connection =>
        val routesSelect = SQL("SELECT * FROM Routes")
        routesSelect().map( row =>
          new Route(row[String]("name"), row[String]("externalRouteId").toString, row[String]("nomeItinerario"))
        ).toList
    }
    Ok(views.html.index(routes))
  }
}
