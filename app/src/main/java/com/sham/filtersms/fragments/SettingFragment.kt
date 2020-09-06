package com.sham.filtersms.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sham.filtersms.FilterSMS
import com.sham.filtersms.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_settings, container, false)

//        val ref = FirebaseDatabase.getInstance().getReference("sms_setting")
        val ref = FirebaseDatabase.getInstance().reference

        var getdatafilersms = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println("Shamera DB Error " + p0.getMessage())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var filterkey = ""
                var repsms = ""
                for(i in p0.children){
                    if("${i.key}" == "sms_setting") {
                        println("Shamera DB get i " + i)
                        filterkey = i.child("filterKeyword").getValue().toString()
                        repsms = i.child("replySMS").getValue().toString()
                    }
                }
                v.editTextFilterKeyword.setText(filterkey)
                v.editTextFilterReply.setText(repsms)
            }
        }
        ref.addValueEventListener(getdatafilersms)
        ref.addListenerForSingleValueEvent(getdatafilersms)


        v.btnApply.setOnClickListener ({
            // Write a message to the database
            var filterKeyword = v.editTextFilterKeyword.text.toString().trim()
            var replySMS = v.editTextFilterReply.text.toString().trim()
//            val database = FirebaseDatabase.getInstance()
//            val myReffilterKeyword = database.getReference("filterKeyword")
//            val myRefreplySMS = database.getReference("replySMS")
//
////            myRef.setValue("Hello, Shamera!")
//            myReffilterKeyword.setValue(filterKeyword)
//            myRefreplySMS.setValue(replySMS)
            if(filterKeyword.isEmpty()){
                editTextFilterKeyword.error = "Cannot be Empty!!! Add the Filter Key Word"
                return@setOnClickListener
            }
            if(replySMS.isEmpty()){
                editTextFilterReply.error = "Cannot be Empty!!! Add the Reply Message"
                return@setOnClickListener
            }


            val myData = FilterSMS(filterKeyword,replySMS)
            ref.child("sms_setting").setValue(myData).addOnCompleteListener {
                Toast.makeText(context, "added", Toast.LENGTH_SHORT).show()
            }


        })
        return v
    }
}