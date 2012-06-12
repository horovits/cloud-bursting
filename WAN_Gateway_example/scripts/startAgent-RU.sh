. setExampleEnv.sh

export LOOKUPGROUPS="RU"

# PLEASE replace localhost with relevant HOSTNAME in production
export LOOKUPLOCATORS=localhost:4166

export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=4166"
export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.gigaspaces.system.registryPort=10098"
export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.gigaspaces.start.httpPort=9813"
export EXT_JAVA_OPTIONS="$EXT_JAVA_OPTIONS -Dcom.gs.zones=RU"

# Modify this as needed
export GSC_JAVA_OPTIONS=-Xmx128m

$GS_HOME/bin/gs-agent.sh gsa.gsm 1 gsa.gsc 2
