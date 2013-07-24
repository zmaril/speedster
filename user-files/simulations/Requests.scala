package speedster

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

object Requests{
	
	val APP = sys.env("APP")
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


	val num_users   = 1000
  val num_seconds = 1
  val num_repeats = 100

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

	var trivial_request = script_request("123456789","123456789")

	var short_request    = script_request("sleep(1);123456789","123456789")
	var medium_request   = script_request("sleep(10);123456789","123456789")
	var long_request     = script_request("sleep(100);123456789","123456789")
	var forever_request  = script_request("sleep(1000);123456789","123456789")
}
