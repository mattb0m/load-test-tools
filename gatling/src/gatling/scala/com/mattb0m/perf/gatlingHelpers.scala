package com.mattb0m.perf

import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import scala.collection.mutable.Map
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.client.Request
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.core.pause.{Constant, Disabled}
import io.gatling.core.structure.ChainBuilder
import scala.util.control.Breaks._

object UuidFeeder {
	val feed = Iterator.continually(scala.collection.immutable.Map("uuid" -> UUID.randomUUID().toString))
}

// Support dynamic HTTP header injection on all requests
trait InjectorCallback {
	def call(): String
}

class HttpHeaderInjector {
	val callbacks = Map[String, InjectorCallback]()
	
	// Add dynamic header generated by a callback function
	def addHeader(name:String, cb:InjectorCallback): Unit = {
		this.callbacks.addOne(name, cb)
	}
	
	def addDynatraceHeader(request:Request, session:Session): Unit = {
		val hostname = InetAddress.getLocalHost().getHostName()
		val TE = getClass.getSimpleName + "_" +LocalDateTime.now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) /* Test run ID */
		val ID = UUID.randomUUID.toString /* Unique request ID */
		val VU = hostname + "_" + session.scenario + "_" + session.userId /* unique virtual user ID */
		val NA = request.getName /* timer/sampler/request name */
		val SI = "GATLING" /* source/testing tool */
		val AN = hostname /* agent name (source) */
		val SN = session.scenario /* scenario/thread group */
		request.getHeaders.set("x-dynaTrace", "ID="+ID+";VU="+VU+";NA="+NA+";SI="+SI+";AN="+AN+";SN="+SN+";TE="+TE)
	}
	
	/* add all headers to HTTP request signature */
	def injectHeaders(http:HttpProtocolBuilder, enableDynatrace:Boolean): HttpProtocolBuilder = {
		return http.sign((request, session) => {
			for ((name,cb) <- this.callbacks) request.getHeaders.set(name, cb.call())
			if(enableDynatrace) addDynatraceHeader(request, session)
		})
	}
}

// Load config values from System properties
object ConfigLoader {
	def load(name:String, default:String): String = {
		return System.getProperty(name, default)
	}
	
	def loadInt(name:String, default:Int): Int = {
		return System.getProperty(name, default.toString).toInt
	}
	
	def loadDouble(name:String, default:Double): Double = {
		return System.getProperty(name, default.toString).toDouble
	}
	
	def loadBool(name:String, default:Boolean): Boolean = {
		return System.getProperty(name, default.toString).toBoolean
	}
}

// Basic config for most load tests
class BasicTestConfig {
	// Load profile (in seconds)
	val rampUp = ConfigLoader.loadInt("rampUp", 1).seconds
	val holdLoad = ConfigLoader.loadInt("holdLoad", 600).seconds
	val rampDown = ConfigLoader.loadInt("rampDown", 1).seconds
	val maxDuration = rampUp + holdLoad + rampDown
	
	// Wait time (in seconds)
	val pacing = ConfigLoader.loadInt("pacing", 1).seconds
	val pauses = if(ConfigLoader.loadBool("pauses", true)) Constant else Disabled // Execute pauses ?
	
	// User counts, matching pattern "users\d{2}"
	val usersMap = Map[Int, Int]()
	
	breakable {
	for(i <- 1 to 99) {
		val num = ConfigLoader.loadInt("users%02d".format(i), 0)
		if(num == 0) {
			break
		} else {
			this.usersMap.addOne(i,num)
		}
	}}
	
	def users(index:Int): Int = {
		if(this.usersMap.contains(index)) {
			return this.usersMap(index)
		} else {
			return 0
		}
	}
}

// Single load test step
class TestStep (actions:ChainBuilder) {
	val name = this.getClass.getSimpleName.stripSuffix("$")
	val requests = group(name) {actions}
}

// A grouped load test case, calling individual Steps/Actions
class TestCase(init:ChainBuilder, pacing:FiniteDuration, actions:ChainBuilder*) {
	val name = this.getClass.getSimpleName.stripSuffix("$")
	val scn = scenario(name).exec(group(s"_TC_${name}") {init.exitHereIfFailed}).forever {
		group(s"_TC_${name}") {
			exitBlockOnFail {
				pace(pacing)
				.exec(actions)
			}
		}
	}
}
