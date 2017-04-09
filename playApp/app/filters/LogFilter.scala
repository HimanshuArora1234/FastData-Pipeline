package filters

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import factory.akkaFactory.{AkkaFactory, LogMessage}
import factory.kafkaFactory.KafkaLogUtils
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by himanshu on 02/04/17.
  */
class LogFilter @Inject()(implicit override val mat: Materializer, exec: ExecutionContext) extends Filter {

  override def apply(nextFilter: RequestHeader => Future[Result])
                    (requestHeader: RequestHeader): Future[Result] = {
    // Run the next filter in the chain. This will call other filters
    // and eventually call the action. Take the result and modify it
    // by adding a new header.
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      AkkaFactory.kafkaProducerActorRef ! new LogMessage(KafkaLogUtils.toJson(result,
        requestTime, result.header.headers.get("uid").getOrElse("")))
      result
    }
  }

}
