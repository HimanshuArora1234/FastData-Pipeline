package actions

/**
  * Created by himanshu on 02/04/17.
  */

import play.api.Logger
import play.api.mvc._

import scala.concurrent.Future

object LogAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    block(request)
  }
}