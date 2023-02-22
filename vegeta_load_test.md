# Vegeta load testing

echo "GET http://localhost:8888/assets" | vegeta attack -workers=4 -max-workers=10 -duration=30s

echo "GET http://localhost:8888/assets" | vegeta attack -workers=4 -max-workers=10 -duration=30s | tee results.bin | vegeta report

vegeta report -type=json results.bin > metrics.json

report -type=hdrplot

echo "GET http://localhost/" | vegeta attack -duration=5s | tee results.bin | vegeta report
vegeta report -type=json results.bin > metrics.json

cat results.bin | vegeta plot > plot.html
cat results.bin | vegeta report -type="hist[0,100ms,200ms,300ms]"



###

nano target.list
**GET http://<application_url>/list/user/1
GET http://<application_url>/list/user/2
GET http://<application_url>/list/user/3**

POST http://<application_url>/create/newuser/
Content-Type: application/json
@/path/to/newuser.json
The content of the file /path/to/newuser.json contains the body of the request:
{
"name": "Peter";
"lastname": "Smith";
"email": "psmith@example.com"
}


vegeta attack -duration=5s -rate=5 -targets=target.list

vegeta attack -duration=120s -rate=100 -targets=target.list -output=attack-5.bin
vegeta plot -title=Attack%20Results attack-5.bin > results.html


vegeta report -type=json results.bin

vegeta report results.bin
