. setExampleEnv.sh

cd ../deploy
# PLEASE replace localhost with relevant HOSTNAME in production

export LOOKUPLOCATORS=localhost:4266
$GS_HOME/bin/gs.sh deploy -zones US wan-space-US
$GS_HOME/bin/gs.sh deploy -zones US wan-gateway-US

