# FilterSMS

This is simple SMS read application. Only different is it's filter the SMS using given keyword and reply to the sender preconfigured SMS.

## Setting up
*	Need to create Realtime DB (Firebase) connection and add generated google-services.json to the app/src/ path.

## Screen captures
* Main Screen

![Main Screen](https://github.com/shamera82/FilterSMS/blob/master/screenshots/main_screen.png?raw=true)

* Setting Screen

![Setting Screen](https://github.com/shamera82/FilterSMS/blob/master/screenshots/setting_screen.png?raw=true)

## Pending Tasks
*	currently DB has one record support. Need to modify as primary key based on receiver mobile number to filter
*	currently on setting you can select siren tune, but it's not updating db. Need to enable that facility
*	Setting/Siren Tune view list need to update from res/raw based on title names. (currently it just hardcoded list coming from string-array under values/setting.xml
*	Need to add / remove siren tunes from the ListView


#### Enjoy the Free App and appreciate if you can suggest/work on the pending tasks
