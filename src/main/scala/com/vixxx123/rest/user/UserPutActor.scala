package com.vixxx123.rest.user

import akka.actor.Actor
import com.vixxx123.rest.EntityNotFound
import com.vixxx123.rest.internal.configuration.DatabaseAccess
import com.vixxx123.util.{SqlUtil, JsonUtil}
import spray.json.JsonReader
import spray.routing.{Rejection, RequestContext}
import spray.httpx.SprayJsonSupport._
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation

case class PutMessage(ctx: RequestContext, user: User)
case class PatchMessage(ctx: RequestContext, userId: Int)

class UserPutActor extends Actor with DatabaseAccess {



  override def receive: Receive = {
    case PutMessage(ctx, user) =>
      val localCtx = ctx
      connectionPool withSession {
        implicit session =>
          val updated = Users.filter(_.id === user.id).update(user)
          if (updated == 1) {
            localCtx.complete(user)
          } else {
            localCtx.complete(EntityNotFound("Not found user id " + user.id))
          }
      }

    case PatchMessage(ctx, userId) =>
      val localCtx = ctx
      val updateStatement = SqlUtil.patch2updateStatement("user", ctx.request.message.entity.asString)

      connectionPool withSession {
        implicit session =>
          val updated = Q.updateNA(updateStatement + " " + SqlUtil.whereById(userId))
          if (updated.first == 1) {
            localCtx.complete(Users.filter(_.id === userId).firstOption)
          } else {
            localCtx.complete(EntityNotFound("Not found user id " + userId))
          }
      }
  }


}
