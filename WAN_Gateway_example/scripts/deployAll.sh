. setExampleEnv.sh
echo $GS_HOME
cd $GS_HOME/bin
# PLEASE replace localhost with relevant HOSTNAME in production
export LOOKUPLOCATORS=localhost:4366
. $GS_HOME/bin/gs.sh deploy -zones DE wan-space-DE 
. $GS_HOME/bin/gs.sh deploy -zones DE wan-gateway-DE

export LOOKUPLOCATORS=localhost:4166
. $GS_HOME/bin/gs.sh deploy -zones RU wan-space-RU
. $GS_HOME/bin/gs.sh deploy -zones RU wan-gateway-RU

export LOOKUPLOCATORS=localhost:4266
. $GS_HOME/bin/gs.sh deploy -zones US wan-space-US
. $GS_HOME/bin/gs.sh deploy -zones US wan-gateway-US

