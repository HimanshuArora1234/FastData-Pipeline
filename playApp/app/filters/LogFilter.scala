package filters

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import factory.akkaFactory.{AkkaFactory, LogMessage}
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by himanshu on 02/04/17.
  */
class LogFilter @Inject()(implicit override val mat: Materializer, exec: ExecutionContext, akkaFactory: AkkaFactory) extends Filter {

  override def apply(nextFilter: RequestHeader => Future[Result])
                    (requestHeader: RequestHeader): Future[Result] = {
    // Run the next filter in the chain. This will call other filters
    // and eventually call the action. Take the result and modify it
    // by adding a new header.
    nextFilter(requestHeader).map { result =>
      System.out.println("#############################")
      akkaFactory.kafkaProducerActorRef ! new LogMessage("hello")
      result
    }
  }

}
