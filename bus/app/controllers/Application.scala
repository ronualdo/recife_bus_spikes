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
      Map("name" -> toJson(route.name),
        "externalRouteId" -> toJson(route.externalRouteId),
        "nomeItinerario" -> toJson(route.nomeItinerario),
        "stops" -> toJson(route.stops.map {s =>
            Map("codigo" -> toJson(s.codigo),
              "bairro" -> toJson(s.bairro),
              "logradouro" -> toJson(s.logradouro),
              "referencia" -> toJson(s.referencia),
              "latitude" -> toJson(s.latitude),
              "longitude" -> toJson(s.longitude)
              )
          })
       )
    }
    
    Ok(toJson(routes))
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
