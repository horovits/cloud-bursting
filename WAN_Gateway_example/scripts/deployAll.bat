call setExampleEnv.bat

cd %GS_HOME%\bin
rem PLEASE replace localhost with relevant HOSTNAME in production
set LOOKUPLOCATORS=localhost:4366
call gs deploy -zones DE wan-space-DE 
call gs deploy -zones DE wan-gateway-DE

set LOOKUPLOCATORS=localhost:4166
call gs deploy -zones RU wan-space-RU
call gs deploy -zones RU wan-gateway-RU

set LOOKUPLOCATORS=localhost:4266
call gs deploy -zones US wan-space-US
call gs deploy -zones US wan-gateway-US

