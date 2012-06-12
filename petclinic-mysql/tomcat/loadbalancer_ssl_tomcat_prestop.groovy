
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import org.cloudifysource.dsl.context.ServiceContextFactory


println "tomcat_stop.groovy: About to stop tomcat..."

def nullTrustManager = [
    checkClientTrusted: { chain, authType ->  },
    checkServerTrusted: { chain, authType ->  },
    getAcceptedIssuers: { null }
]

def nullHostnameVerifier = [
    verify: { hostname, session -> true }
]

SSLContext sc = SSLContext.getInstance("SSL")
sc.init(null, [nullTrustManager as X509TrustManager] as TrustManager[], null)
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
HttpsURLConnection.setDefaultHostnameVerifier(nullHostnameVerifier as HostnameVerifier)

def LBconfig=new ConfigSlurper().parse(new File("RSloadbalancer.properties").toURL());

def serviceContext = ServiceContextFactory.getServiceContext();
def instanceID = serviceContext.getInstanceId();
def env = System.getenv();
def nodePublicIP = env["CLOUDIFY_AGENT_ENV_PUBLIC_IP"];
println "tomcat_preStop.groovy: public IP is " + nodePublicIP;

final ClientConfig config = new DefaultClientConfig();
client = Client.create(config);
		
final WebResource service = client.resource(LBconfig.authURI);
final String resp = service.path(
		"/v2.0/tokens").header(
		"Content-Type", "application/json").post(
		String.class, LBconfig.authJson);
println "tomcat_preStop.groovy: successfully authnticated with LB provider. Public IP is " + nodePublicIP;

String[] parts = resp.split("\"endpoints\"");
String tokenIdValue = null;
String publicURLValue = null;
for(String part : parts){
String[] words = part.split("\"");
boolean token = false;
boolean compute = false;
String idValue=null;
String tenantValue = null;
String URLValue = null;
for(int i=0;i<words.length;i++)
{
	String word = words[i];
	if(word.compareToIgnoreCase("token")==0)
	{
		token = true;
	}
	if(word.compareToIgnoreCase("compute")==0)
	{
		compute = true;
	}
	if(word.compareToIgnoreCase("id")==0)
	{
		idValue = i + 2 < words.length?words[i+2]:null;
	}
	if(word.compareToIgnoreCase("tenantId")==0)
	{
		 tenantValue = i + 2 < words.length?words[i+2]:null;    
	}
	if(word.compareToIgnoreCase("publicURL")==0)
	{
		 URLValue = i + 2 < words.length?words[i+2]:null;    
	}
	if(compute)
	{
		PublicURLValue = URLValue==null?PublicURLValue:URLValue;
	}
	if(token)
	{
		tokenIdValue = idValue==null?tokenIdValue:idValue;
		if(idValue !=null)
        	token=false
	}
}
}

String pathPrefix;
String endpoint;
parts = PublicURLValue.split("\\\\");
PublicURLValue="";
for(String part : parts)
{
	PublicURLValue = PublicURLValue.concat(part);
 }
int pos = 0;
for(int j=0;j<3;j++)
{
	pos = PublicURLValue.indexOf('/',pos);
	println(pos);
	if(pos <0)
		break;
	pos++;
}
if(pos >0)
{
	pos--;
	pathPrefix = PublicURLValue.substring(pos) + "/";
	endpoint = PublicURLValue.substring(0,pos);
}
String serverBootResponse = null;
String endpointLB = endpoint.replace("servers.", "dfw.loadbalancers.");
service = client.resource(endpointLB);
try {
	serverBootResponse = service.path(
			pathPrefix + "loadbalancers/" + LBconfig.loadbalancerID + "/nodes/" + serviceContext.attributes.thisInstance["LBNodeID"]).header(                   
			"X-Auth-Token", tokenIdValue).accept(
			MediaType.APPLICATION_XML).delete(String.class);
	println "tomcat_preStop.groovy: successfully deleted";
} catch (final UniformInterfaceException e) {
	throw new Exception(e);
}

def home= serviceContext.attributes.thisInstance["home"]
println "tomcat_stop.groovy: tomcat(${instanceID}) home ${home}"

def script= serviceContext.attributes.thisInstance["script"]
println "tomcat_stop.groovy: tomcat(${instanceID}) script ${script}"


println "tomcat_stop.groovy: executing command ${script}"

/*new AntBuilder().sequential {
	exec(executable:"${script}.sh", osfamily:"unix") {
        env(key:"CATALINA_HOME", value: "${home}")
	    env(key:"CATALINA_BASE", value: "${home}")
    	env(key:"CATALINA_TMPDIR", value: "${home}/temp")
		arg(value:"stop")
	}
	exec(executable:"${script}.bat", osfamily:"windows"){
        env(key:"CATALINA_HOME", value: "${home}")
    	env(key:"CATALINA_BASE", value: "${home}")
    	env(key:"CATALINA_TMPDIR", value: "${home}/temp")
		arg(value:"stop")
	}
}
*/
println "tomcat_stop.groovy: tomcat is stopped"




