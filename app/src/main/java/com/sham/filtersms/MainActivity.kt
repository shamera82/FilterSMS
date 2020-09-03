package com.sham.filtersms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.sham.filtersms.fragments.HomeFragment
import com.sham.filtersms.fragments.SettingFragment
import com.sham.filtersms.fragments.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setUpTabs()
        println("Shamera: start here")

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_warning_siren)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            println("Shamera: checkSelfPermission")
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS), 111)
            setUpTabs()
        }
        else {

            mediaPlayer?.setOnPreparedListener {
                println("Shamera: ready to go")
                setUpTabs()
            }
        }
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(SettingFragment(), "Settings")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_settings_24)
        receiveMsg()        // calling the sms read function

    }

    private fun receiveMsg() {
        var br = object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms : SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
//                        Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()
                        if (sms.displayMessageBody.contains("INC",true)) {

                            println("Shamera: got message content keyword")
                            editTextMobile.setText(sms.displayOriginatingAddress)
                            editTextText.setText(sms.displayMessageBody)

                            mediaPlayer?.setLooping(true)
                            mediaPlayer?.start()
                            println("Shamera: playing")
//                            mediaPlayer?.setLooping(true)
//                            mediaPlayer?.start()

                            // auto reply to message
                            val replyText = "ACC"
                            var sms = SmsManager.getDefault()
                            sms.sendTextMessage(editTextMobile.text.toString(),"Shamera",replyText,null,null)


                            // to stop the ringtune
                            btnStop.setOnClickListener {
                                mediaPlayer?.pause() // pause media player instead of stop.
                                println("Shamera: click pause")
                                toastMsg("Press Stop")
                            }
                        }
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    // disply message handling
    private fun toastMsg(msg: String) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}