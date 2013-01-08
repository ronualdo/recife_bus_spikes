package models

import play.api.db.DB
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Route(name: String, externalRouteId: String, nomeItinerario: String, stops: Seq[Stop]) {
  def this(name: String, externalRouteId: String, nomeItinerario: String) = this(name,externalRouteId,nomeItinerario,Nil)
}

object Route {
  type RouteTuple = List[(String, String, String, String, String, String,
    String, String, String)]
    
  def insert(route: Route) = {
    val routeId = DB.withConnection { implicit c =>
      SQL("""
        INSERT INTO Routes (externalRouteId, nomeItinerario, name)
        VALUES ({externalRouteId}, {nomeItinerario}, {name})
      """).on(
          'externalRouteId -> route.externalRouteId,
          'nomeItinerario  -> route.nomeItinerario,
          'name            -> route.name
        ).executeInsert()
    }
    route.stops.foreach(Stop.insert(routeId.get, _))
  }

  def deleteAll() = {
    Stop.deleteAll()
    DB.withConnection { implicit c =>
      SQL("DELETE from Routes").executeUpdate()
    }
  }

  def find(externalRouteId: String = "%", nomeItinerario: String = "%"): Set[Route] = {
    DB.withConnection { implicit c =>
      val routes = SQL(
        """
          SELECT * FROM Routes r INNER JOIN Stops s
          on r.id = s.routeId
          where externalRouteId like {externalRouteId} and nomeItinerario like {nomeItinerario}
        """).
        on(
          'externalRouteId -> externalRouteId,
          'nomeItinerario -> nomeItinerario
        ).as(Route.complicated.*)
      
      routes.headOption.map { f => 
          def createStops(routes: RouteTuple) = {
            routes filter(_._2 == f._2) map { s =>
              Stop(s._4, s._5, s._6, s._7, s._8, s._9)
            }
          }
          Route(f._1, f._2, f._3, createStops(routes))
      }.toSet
    }
  }

  def simple = {
    get[String]("externalRouteId") ~
    get[String]("nomeItinerario") ~
    get[String]("name") map {
      case externalRouteId ~ nomeItinerario ~ name => new Route(name,externalRouteId,nomeItinerario)
    }
  }
  
  def complicated = {
    str("name") ~ 
    str("externalrouteid") ~ 
    str("nomeitinerario") ~
    str("codigo") ~ 
    str("bairro") ~ 
    str("logradouro") ~ 
    str("referencia") ~
    str("latitude") ~ 
    str("longitude") map(flatten)
  }
}
