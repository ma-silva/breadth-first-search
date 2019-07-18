package net.masilva

import cats.effect.IO
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._
import io.circe.generic.auto._
import net.masilva.service.DataServices

/**
 *  A finch http server class. 
 */
object Server extends App {
  def service: Service[Request, Response] = Bootstrap
    .serve[Application.Json](DataServices.services)
    .serve[Text.Plain](DataServices.index)
    .toService

  println("Browse http://localhost:8081 into your browser.")
  Await.ready(Http.server.serve(":8081", service))
}