package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._
import Requests._

class MixedSimulation extends Simulation {
	var n = 50
	val scn = scenario("Mixed")
		.group("Mixed"){
			repeat(n){exec(long_request)}
			.repeat(100-n){exec(trivial_request)}
			}

	//TODO: Extract out users and seconds into another object
	setUp(scn.inject(ramp(1000 users) over (1 seconds)))
		.protocols(httpProtocol)
}
