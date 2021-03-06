package com.sham.filtersms.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sham.filtersms.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_home, container, false)
        Log.d("Shamera-HomeFragment", "On Home Fragment")
        receiveMsg()        // calling the sms read function
        return v
    }

    private fun receiveMsg() {
        Log.d("Shamera-HomeFragment", "Calling receiveMsg()")

        // db connection
        val ref = FirebaseDatabase.getInstance().reference
        var getfilterkey = ""
        var getrepsms = ""
        var sirentune = ""

        // retrieve data from db
        var getdatafilersms = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                println("Shamera Main DB Error " + p0.getMessage())
                Log.i("Shamera-HomeFragment", "DB Error "+ p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                    Log.d("Shamera-HomeFragment", "${i.key} receive as $i")
                    if("${i.key}" == "sms_setting") {
//                        println("Shamera Main DB get i " + i)
                        Log.d("Shamera-HomeFragment", "${i.key} filter as $i")
                        getfilterkey = i.child("filterKeyword").getValue().toString()
                        getrepsms = i.child("replySMS").getValue().toString()
                    }
                    if("${i.key}" == "sms_setting") {
                        Log.d("Shamera-HomeFragment", "${i.key} filter as $i")
                        sirentune = i.child("sirenName").getValue().toString()
                    }
                }
            }
        }
        ref.addValueEventListener(getdatafilersms)
        ref.addListenerForSingleValueEvent(getdatafilersms)

        // sms read and reply
        var br = object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms : SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                        Log.d("Shamera-HomeFragment", "Received SMS as $sms")
                        if (sms.displayMessageBody.contains(getfilterkey,true)) {

//                            println("Shamera: got message content keyword")
                            Log.i("Shamera-HomeFragment", "Received SMS $sms and Filter by $getfilterkey")
                            editTextMobile.setText(sms.displayOriginatingAddress)
                            editTextText.setText(sms.displayMessageBody)

                            // stop meadiaplayer if running
                            mediaPlayer?.stop()
                            //create new meadiaPlayer using the tune and read selected siren tune from variable and play when required
                            mediaPlayer = MediaPlayer.create(context, resources.getIdentifier(sirentune,"raw", context?.packageName))
                            mediaPlayer?.setLooping(true)
                            mediaPlayer?.start()
//                            println("Shamera: playing")
                            Log.i("Shamera-HomeFragment", "Playing Tune $sirentune")


                            if(!btnStop.isClickable){
                                btnStop.isClickable = true
                            }
                            // to stop the ringtone
                            btnStop.setOnClickListener {
                                mediaPlayer?.stop() // pause media player instead of stop.
//                                println("Shamera: click Stop")
                                Log.i("Shamera-HomeFragment", "Press Stop Button")

                                // auto reply to message
                                val replyText = getrepsms
                                var sms = SmsManager.getDefault()
                                sms.sendTextMessage(editTextMobile.text.toString(),"Shamera",replyText,null,null)

                                btnStop.isClickable = false

                                toastMsg("Acknowledge and Send Reply $getrepsms")
                            }
                        }
                    }
                }
            }
        }
        requireActivity().registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    // disply message handling
    private fun toastMsg(msg: String) {
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
    }

}