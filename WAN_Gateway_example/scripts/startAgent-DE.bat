call setExampleEnv.bat

set LOOKUPGROUPS="DE"

rem PLEASE replace localhost with relevant HOSTNAME in production
set LOOKUPLOCATORS=localhost:4366

set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=4366
set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.gigaspaces.system.registryPort=10298
set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.gigaspaces.start.httpPort=9713
set EXT_JAVA_OPTIONS=%EXT_JAVA_OPTIONS% -Dcom.gs.zones=DE

rem Modify this as needed
set GSC_JAVA_OPTIONS=-Xmx128m

%GS_HOME%/bin/gs-agent.bat gsa.gsm 1 gsa.gsc 2
