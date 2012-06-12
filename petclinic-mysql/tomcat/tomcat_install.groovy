import org.cloudifysource.dsl.context.ServiceContextFactory

def config = new ConfigSlurper().parse(new File("tomcat.properties").toURL())
def serviceContext = ServiceContextFactory.getServiceContext()
def instanceID = serviceContext.getInstanceId()


println "tomcat_install.groovy: Installing tomcat..."

def home = "${serviceContext.serviceDirectory}/${config.name}"
def script = "${home}/bin/catalina"

serviceContext.attributes.thisInstance["home"] = "${home}"
serviceContext.attributes.thisInstance["script"] = "${script}"
println "tomcat_install.groovy: tomcat(${instanceID}) home is ${home}"



def configXmlFile = new File("${serviceContext.serviceDirectory}/petclinic.xml") 
def configXmlText = configXmlFile.text	
configXmlText = configXmlText.replace('WAR_LOC', "${config.applicationWar}") 
configXmlFile.text = configXmlText


//download apache tomcat
new AntBuilder().sequential {	
	mkdir(dir:"${config.installDir}")
	get(src:"${config.downloadPath}", dest:"${config.installDir}/${config.zipName}", skipexisting:true)
	unzip(src:"${config.installDir}/${config.zipName}", dest:"${config.installDir}", overwrite:true)
	move(file:"${config.installDir}/${config.name}", tofile:"${home}")
	get(src:"${config.applicationWarUrl}", dest:"${config.applicationWar}", skipexisting:true)
	copy(tofile: "${home}/webapps/${config.warName.toLowerCase()}", file:"${config.applicationWar}", overwrite:true)
	chmod(dir:"${home}/bin", perm:'+x', includes:"*.sh")
	copy(todir:"${home}/conf/Catalina/localhost", file:"${serviceContext.serviceDirectory}/petclinic.xml", overwrite:true)
}

println "tomcat_install.groovy: Replacing default tomcat port with port ${config.port}"
def serverXmlFile = new File("${home}/conf/server.xml") 
def serverXmlText = serverXmlFile.text	
def portStr = "port=\"${config.port}\""
serverXmlText = serverXmlText.replace('port="8080"', portStr) 
serverXmlFile.text = serverXmlText.replace('unpackWARs="true"', 'unpackWARs="false"') 

println "tomcat_install.groovy: Tomcat installation ended"
sleep(30)
