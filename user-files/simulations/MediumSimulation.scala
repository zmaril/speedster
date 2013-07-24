package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._
import Requests._

class MediumSimulation extends Simulation {
	val scn = scenario("Medium test")
		.group("Medium"){repeat(num_repeats){exec(medium_request)}}

	setUp(scn.inject(ramp(num_users users) over (num_seconds seconds)))
		.protocols(httpProtocol)
}
