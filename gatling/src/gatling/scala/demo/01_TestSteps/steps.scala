package demo
import com.mattb0m.perf._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object InitStep extends TestStep (
	exec(
		http("GET Login")
		.get("/login")
		.check(status.is(404)))
	.pause(1.second)
)

object TestStep01 extends TestStep (
	exec(
		http("GET Google")
		.get("/"))
	.pause(1.second)
)

object TestStep02 extends TestStep (
	feed(UuidFeeder.feed)
	.exec(
		http("GET /test")
		.post("/test")
		.body(StringBody("{ testVal : #{uuid} }"))
		.check(status.is(404)))
	.pause(1.second)
)
