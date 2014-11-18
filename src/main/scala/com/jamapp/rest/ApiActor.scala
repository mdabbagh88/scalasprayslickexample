package com.jamapp.rest

import akka.actor.{ActorSystem, Actor, ActorRefFactory}
import com.jamapp.rest.user.UserApi
import spray.http.MediaTypes._
import spray.routing._
import spray.http._

/**
 * Created by wiktort on 2014-11-13.
 */
class ApiActor extends Actor with Api {

  override implicit def actorRefFactory = context

  override def receive = runRoute(routing)

}

object Api {
  val actorSystem = ActorSystem("restActorSystem")
}

trait Api extends HttpService with UserApi {
  val routing = userRoute
}
