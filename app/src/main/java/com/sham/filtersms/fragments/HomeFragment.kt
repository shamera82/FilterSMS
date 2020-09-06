package com.sham.filtersms.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sham.filtersms.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_home, container, false)
        receiveMsg()        // calling the sms read function
        return v
    }

    private fun receiveMsg() {
        println("Shamera receiveMsg()")
        // db connection
        val ref = FirebaseDatabase.getInstance().reference
        var getfilterkey = ""
        var getrepsms = ""
        var sirantune = ""

        // retrieve data from db
        var getdatafilersms = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("Shamera Main DB Error " + p0.getMessage())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                    if("${i.key}" == "sms_setting") {
                        println("Shamera Main DB get i " + i)
                        getfilterkey = i.child("filterKeyword").getValue().toString()
                        getrepsms = i.child("replySMS").getValue().toString()
                    }
                    if("${i.key}" == "siran_setting") {
                        println("Shamera Main DB get i " + i)
                        sirantune = i.child("siranName").getValue().toString()
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
                        if (sms.displayMessageBody.contains(getfilterkey,true)) {

                            println("Shamera: got message content keyword")
                            editTextMobile.setText(sms.displayOriginatingAddress)
                            editTextText.setText(sms.displayMessageBody)

                            // read selected siran tune from variable and play when required.
                            println("Shamera: playing tune $sirantune")
                            mediaPlayer = MediaPlayer.create(context, resources.getIdentifier(sirantune,"raw", context?.packageName))
                            mediaPlayer?.setLooping(true)
                            mediaPlayer?.start()
                            println("Shamera: playing")

                            // to stop the ringtune
                            btnStop.setOnClickListener {
                                mediaPlayer?.pause() // pause media player instead of stop.
                                println("Shamera: click pause")

                                // auto reply to message
                                val replyText = getrepsms
                                var sms = SmsManager.getDefault()
                                sms.sendTextMessage(editTextMobile.text.toString(),"Shamera",replyText,null,null)

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
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

}