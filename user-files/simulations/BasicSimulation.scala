package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._
import Requests._

class BasicSimulation extends Simulation {
	val scn = scenario("Baseline test")
		.group("Baseline"){
			repeat(100){
				exec(simple_get).exec(simple_post)}}

	setUp(scn.inject(ramp(1000 users) over (1 seconds)))
		.protocols(httpProtocol)
}
