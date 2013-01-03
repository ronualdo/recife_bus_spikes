package controllers

import play.api._
import play.api.db._
import play.api.mvc._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import recifeBuses._
import models._
import play.Logger
import play.api.libs.json.Json._

object Application extends Controller {
  def index = Action {
    Ok(views.html.index(Route.find(externalRouteId = "744")))
  }
  
  def stopsFor(routeId: String) = Action {
    val routes = Route.find(externalRouteId= routeId).map { route =>
      Map("name" -> route.name,
        "externalRouteId" -> route.externalRouteId,
        "nomeItinerario" -> route.nomeItinerario,
        "stops" -> toJson(route.stops.map {s =>
            Map("codigo" -> s.codigo,
              "bairro" -> s.bairro,
              "logradouro" -> s.logradouro,
              "referencia" -> s.referencia,
              "latitude" -> s.latitude,
              "longitude" -> s.longitude)
          }).toString
       )
    }
    
    Ok(toJson(routes).toString)
  }

  def reload = Action {
    //cleaning database
    //check if theres new information
    Route.deleteAll()
    
    val routes = PageScraper.parse
    routes.foreach(Route.insert(_))
    Ok(views.html.index(Route.find(externalRouteId = "744")))
  }
}
