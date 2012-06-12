call setExampleEnv.bat

set LOOKUPGROUPS="US"

rem PLEASE replace localhost with relevant HOSTNAME in production
set LOOKUPLOCATORS=localhost:4266

set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=4266
set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.gigaspaces.system.registryPort=10198
set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.gigaspaces.start.httpPort=9913
set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.gs.zones=US

rem Modify this as needed
set GSC_JAVA_OPTIONS=-Xmx128m

%GS_HOME%/bin/gs-agent.bat gsa.gsm 1 gsa.gsc 2
