package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._
import Requests._

class LoadSimulation extends Simulation {
	val scn = scenario("Load test")
	.group("Load test"){repeat(num_repeats){
		exec(name_request)
		.exec(title_request)}}

	setUp(scn.inject(ramp(num_users users) over (num_seconds seconds)))
		.protocols(httpProtocol)
}
