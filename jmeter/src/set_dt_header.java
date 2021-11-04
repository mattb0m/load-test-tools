/*=============================================================================================================================
 * AUTH: Matthew Baum
 * DATE: 2018/03/12
 * DESC: This script automatically sets custom x-dynaTrace headers on Jmeter samplers, as documented here:
 * 		https://community.dynatrace.com/community/display/DOCDT65/Integration+with+Web+Load+Testing+and+Monitoring+Tools
 *
 * NOTE: This script should be executed by a JSR223 Preprocessor. It can be included under specific samplers, or can be
 *		included directly under the Test Plan to apply to all sampler (they must all be HTTP samplers).
 * NOTE: Header will look like:
 *    	x-dynaTrace: ID=1efd3eba-8bff-4b74-aa82-f2944e3135b3;VU=PP17804_Thread Group 1_0;NA=request_1;
 *		SI=JMETER;AN=PP17804;SN=Thread Group 1;TE=dt_integr_ex.jmx
 *=============================================================================================================================*/
import java.util.UUID;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;

/* Define all header values */
final HEADER_NAME = "x-dynaTrace";
String ID = UUID.randomUUID().toString(); /* Unique ID for the request (UUID) */
String PC; /* Page Context (NOT USED) */

/* Virtual User (host_thread-group_thread-num) */
String VU = JMeterUtils.getLocalHostName() + "_" + ctx.getThreadGroup().getName() + "_" + String.valueOf(ctx.getThreadNum());

String NA = sampler.getName(); /* Timer Name (Sampler name) */
String SI = "JMETER"; /* Source ID (JMETER) */
String GR; /* Geographic Region (NOT USED) */
String AN = JMeterUtils.getLocalHostName(); /* Agent Name (local host name) */
String SN = ctx.getThreadGroup().getName(); /* Script Name (thread group name) */
String TE = FileServer.getFileServer().getScriptName(); /* Test Name (JMX File name) */

/* Set or replace header */
HeaderManager hm = sampler.getHeaderManager();
if(hm == null) {
	hm = new HeaderManager();
	sampler.setHeaderManager(hm);
}

hm.removeHeaderNamed(HEADER_NAME);
hm.add(new Header(HEADER_NAME, "ID="+ID+";VU="+VU+";NA="+NA+";SI="+SI+";AN="+AN+";SN="+SN+";TE="+TE));