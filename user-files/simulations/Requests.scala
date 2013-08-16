package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

object Requests{
	
	val APP = "rexster"//sys.env("APP")
	val URL = "http://ec2-54-225-75-101.compute-1.amazonaws.com"
  val PORT = "8182"
	val URI  = "/graphs/graph/tp/gremlin"
	val httpProtocol = http.baseURL(URL+":"+PORT)


	val num_users   = 100
  val num_seconds = 1
  val num_repeats = 1000

	var names = csv("/home/ubuntu/names.csv").random
	var name_request = feed(names).exec(http("Named Request") 													
																			.post(URI)
																			.header("Accept", "application/json")
																			.header("Content-Type","application/json")
																			.body(StringBody("""{
																				"script": "g.V(\"name\",name).both().both().has(\"name\",name).dedup.sideEffect{it.prop = (new Random()).nextInt(100)}count();123455+1",
																				"params": {"name": "${name}"}
																						}"""))
																			.check(status.is(200),regex("123456")))

	var titles = csv("/home/ubuntu/titles.csv").random
	var title_request = feed(titles).exec(http("Title Request") 													
																			.post(URI)
																			.header("Accept", "application/json")
																			.header("Content-Type","application/json")
																			.body(StringBody("""{
																				"script": "g.V(\"title\",title).both().dedup.sideEffect{it.prop = (new Random()).nextInt(100)}.count();123455+1",
																				"params": {"title": "${title}"}
																						}"""))
																			.check(status.is(200),regex("123456")))


	var remove_request = exec(http("Remove Request") 
														.post(URI)
														.header("Accept", "application/json")
														.param("script","g.E().remove();g.V().remove();123456788+1")
														.check(status.is(200),regex("123456789")))

}
