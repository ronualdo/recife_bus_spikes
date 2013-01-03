package models

import play.api.db.DB
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.Logger

class Stop(val codigo: String, val bairro: String, val logradouro: String, val referencia: String, val latitude: String, val longitude: String)

object Stop {
  
  def insert(routeId: Long, newStop: Stop) = {
    Logger.debug("route id " + routeId)
    
    DB.withConnection { implicit c => 
      SQL("""
        INSERT INTO Stops (codigo, routeId, bairro, logradouro, referencia, latitude, longitude)
        VALUES ({codigo}, {routeId}, {bairro}, {logradouro}, {referencia}, {latitude}, {longitude})
      """).on(
          'routeId -> routeId,
          'codigo -> newStop.codigo,
          'bairro -> newStop.bairro,
          'logradouro -> newStop.logradouro,
          'referencia -> newStop.referencia,
          'latitude -> newStop.latitude,
          'longitude -> newStop.longitude
        ).executeInsert()
    }
  }

  def deleteAll() = {
    DB.withConnection { implicit c =>
      SQL("DELETE from Stops").executeUpdate()
    }
  }
}
