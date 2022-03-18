package demo
import com.mattb0m.perf._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object TestCase01 extends TestCase (
	Global.pacing,
	TestStep01.requests,
	TestStep02.requests)
