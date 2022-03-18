package demo
import com.mattb0m.perf._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

// Load basic load testing system props
object Global extends BasicTestConfig

// The complete load test
class test01 extends Simulation {
	val httpConf = http.baseUrl("https://www.google.ca")
	
	// Inject Dynatrace headers
	val injector = new HttpHeaderInjector()
	injector.injectHeaders(httpConf, true)
	
	setUp (
		TestCase01.scn.inject(rampUsers(ConfigLoader.loadInt("users", 3)).during(Global.rampUp))
	)
	.assertions (
		global.failedRequests.percent.lt(1)
	)
	.maxDuration(Global.maxDuration)
	.pauses(Global.pauses)
	.protocols(httpConf)
}
