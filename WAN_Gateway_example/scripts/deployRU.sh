. setExampleEnv.sh

cd $GS_HOME/bin
# PLEASE replace localhost with relevant HOSTNAME in production

export LOOKUPLOCATORS=localhost:4166
gs deploy -zones RU wan-space-RU
gs deploy -zones RU wan-gateway-RU
