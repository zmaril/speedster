package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._
import Requests._

class LongSimulation extends Simulation {
	val scn = scenario("Long test")
		.group("Long"){repeat(num_repeats){exec(long_request)}}

	setUp(scn.inject(ramp(num_users users) over (num_seconds seconds)))
		.protocols(httpProtocol)
}
