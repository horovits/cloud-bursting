. setExampleEnv.sh

export LOOKUPGROUPS="US"

# PLEASE replace localhost with relevant HOSTNAME in production
export LOOKUPLOCATORS=localhost:4266

export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=4266"
export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.gigaspaces.system.registryPort=10198"
export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.gigaspaces.start.httpPort=9913"
export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.gs.zones=US"

# Modify this as needed
export GSC_JAVA_OPTIONS=-Xmx128m

$GS_HOME/bin/gs-agent.sh gsa.gsm 1 gsa.gsc 2
