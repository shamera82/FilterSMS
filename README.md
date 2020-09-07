# FilterSMS

This is simple sms read application. Only different is it's filter the sms using given keyword and reply to the sender preconfigred sms.

## Setting up
1. Need to create Realtime DB (Firebase) connection and add generated `google-services.json` to the app/src/ path.


## Pending Tasks
1. curretly DB has one record support. Need to modify as primary key based on receiver mobile number to filter
2. currently on setting you can select siren tune, but it's not updating db. Need to enable that facility
3. Setting/Siren Tune view list need to update from res/raw based on title names. (currently it just hardcored list coming from `string-array` under values/setting.xml
4. Need to add / remove siren tunes from the ListView


### Enjoy the Free App and appriciate if you can suggest/work on the pending tasks
