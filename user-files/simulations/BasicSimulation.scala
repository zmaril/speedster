package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

class SimpleSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://ec2-50-19-15-5.compute-1.amazonaws.com:8080")

	val headers_1 = Map("Keep-Alive" -> "200")

	val scn = scenario("Up and running")
		.group("Index") {
			exec(
				http("request_1")
					.get("/")
					.headers(headers_1)
					.check(status.is(200)))
				.pause(0 milliseconds, 100 milliseconds)
		}

	setUp(scn.inject(ramp(1 users) over (1 seconds)))
		.protocols(httpProtocol)
}
