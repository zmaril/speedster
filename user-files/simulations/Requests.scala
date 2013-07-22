package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

object Requests{

	val httpProtocol = http
		.baseURL("http://ec2-54-225-75-101.compute-1.amazonaws.com:8080")

	val simple_get = exec(http("Get Index")
											 .get("/")
  										 .check(status.is(200)))

	var simple_post = exec(http("Empty Post Request")
												 .post("/execute")
												 .check(status.is(200)))

	var trivial_request = exec(http("Trivial Request")
														 .post("/execute")
														 .header("Accept", "application/json")
														 .header("Content-Type", "application/json")
   													 .body(StringBody("""{"script":"1+1"}"""))
														 .check(status.is(200)))

	var long_request = exec(http("Long Request")
													.post("/execute")
													.header("Accept", "application/json")
													.header("Content-Type", "application/json")
   												.body(StringBody("""{"script":"sleep(10)"}"""))
													.check(status.is(200)))
}
