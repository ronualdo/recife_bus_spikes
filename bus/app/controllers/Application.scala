package controllers

import play.api._
import play.api.mvc._
import recifeBuses._
import models._
import com.codahale.jerkson.Json

object Application extends Controller {
  def index = Action {
    Ok(views.html.index(Route.find(externalRouteId = "744")))
  }
  
  def stopsFor(routeId: String) = Action {
    val foundRoutes = Route.find(externalRouteId = routeId)
    val result = Json.generate(foundRoutes)
    Ok(result)
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
