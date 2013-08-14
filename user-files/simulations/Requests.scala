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
	var PORT, URI ="";

	if (APP == "rexster"){
    PORT = "8182"
		URI  = "/graphs/graph/tp/gremlin"
	}
	else if (APP == "speedster"){
    PORT = "8080"
		URI  = "/execute"
	}
	else{
		throw new Exception("'APP' wasn't set in the environment.")
	}
	val httpProtocol = http.baseURL(URL+":"+PORT)


	val num_users   = 1
  val num_seconds = 1
  val num_repeats = 1

	def script_request(script:String,result:String):
			io.gatling.core.structure.ChainBuilder = {
		if(APP=="rexster"){
			exec(http("Script Request")
					 .post(URI)
					 .header("Accept", "application/json")
					 .param("script",script)
					 .check(status.is(200),regex(result)))
		}
		else{
				exec(http("Script Request")
           .post("/execute")
           .header("Accept", "application/json")
           .header("Content-Type", "application/json")
           .body(StringBody("{\"script\":\""+script+"\"}\""))
					 .check(status.is(200),regex(result)))
		}
	}

	var ack = "def ack (m,n){m == 0 ? n + 1 : n == 0 ? ack(m-1, 1) : ack(m-1, ack(m, n-1));}\n"

	def acked_for(n:Integer): String = {ack+"for(int i =1; i<"+n+"; i++){ack(3,7)};\n 123456788+1"}
	

	var trivial_request  = script_request(ack+"123456788+1","123456789")
	var short_request    = script_request(acked_for(1),"123456789")
	var medium_request   = script_request(acked_for(10),"123456789")
	var long_request     = script_request(acked_for(100),"123456789")
	var forever_request  = script_request(acked_for(1000),"123456789")

	//Load up the graph
	//Query the graph a bunch
	//Remove said graph
	var clean_request = exec(http("Clean Graph Request") 
													.post(URI)
													.header("Accept", "application/json")
													.param("script","""
																 if(g.V().count() == 0){
																 GraphSONReader.inputGraph(g,new FileInputStream(new File("/home/ubuntu/marvel.graphson")));
																 }
																 123456788+1""")
															 .check(status.is(200),regex("123456789")))

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
