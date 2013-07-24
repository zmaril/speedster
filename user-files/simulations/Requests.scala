package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

object Requests{

	val httpProtocol = http.baseURL(sys.env("URL"))	
	val URI = sys.env("URI")
	val num_users   = 1
  val num_seconds = 1
  val num_repeats = 1

	var trivial_request = exec(http("Trivial Request")
												 .post(URI)
												 .header("Accept", "application/json")
												 .param("script","123456789")
												 .check(status.is(200),
																regex("""123456789""")))

	var short_request   = exec(http("Trivial Request")
												 .post(URI)
												 .header("Accept", "application/json")
												 .param("script","sleep(1);123456789")
												 .check(status.is(200),
																regex("""123456789""")))

	var medium_request   = exec(http("Medium Request")
													.post(URI)
													.header("Accept", "application/json")
 	  										  .param("script","sleep(10);123456789")
   												.check(status.is(200),
													 			 regex("""123456789""")))


	var long_request    = exec(http("Long Request")
													.post(URI)
													.header("Accept", "application/json")
 	  										  .param("script","sleep(10);123456789")
   												.check(status.is(200),
													 			 regex("""123456789""")))
}
