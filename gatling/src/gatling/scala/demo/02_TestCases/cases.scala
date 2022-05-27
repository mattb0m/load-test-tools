package demo
import com.mattb0m.perf._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object TestCase01 extends TestCase (
	InitStep.requests,
	Global.pacing(1),
	SessionHelper.flushAll(),
	TestStep01.requests,
	TestStep02.requests)

object TestCase02 extends TestCase (
	exec(),
	Global.pacing(2),
	TestStep01.requests)
