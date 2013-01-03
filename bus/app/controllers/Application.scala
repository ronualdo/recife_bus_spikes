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

object Application extends Controller {

  def index = Action {
    //cleaning database
    //check if theres new information
    Route.deleteAll()
    
    val routes = PageScraper.parse
    routes.foreach(Route.insert(_))
 
    Ok(views.html.index(Route.findAll))
  }
}
