package demo
import com.mattb0m.perf._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object TestCase01 extends TestCase (
	InitStep,
	Global.pacing(1),
	SessionHelper.flushAll(),
	TestStep01,
	TestStep02)

object TestCase02 extends TestCase (
	Global.pacing(2),
	TestStep01)
