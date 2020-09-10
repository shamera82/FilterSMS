package com.sham.filtersms.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
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


//class SettingFragment : Fragment(), AdapterView.OnItemClickListener {
class SettingFragment : Fragment() {

    lateinit var listView : ListView
    var tuneName = ""
    var tuneId = 0
    var tuneArrayIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_settings, container, false)


        var tuneArray = resources.getStringArray(R.array.siren_tunes_list)
        listView = v.ListViewSiran
        listView.adapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_multiple_choice, tuneArray)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        listView.setOnItemClickListener{
            parentFragment: AdapterView<*>?, view: View?, position: Int, id: Long ->
            tuneName = parentFragment?.getItemAtPosition(position) as String
            tuneId = position
            Toast.makeText(v.context,"Select Tune $tuneName", Toast.LENGTH_SHORT).show()
            Log.d("Shamera-SettingFragment", "listView.setOnItemClickListener id: $tuneId and name: $tuneName")
        }

        //init db connection
        val ref = FirebaseDatabase.getInstance().reference

        var getdatafilersms = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.i("Shamera-SettingFragment", "DB Error " + p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var filterkey = ""
                var repsms = ""
                var sirentune = ""
                for(i in p0.children){
                    Log.d("Shamera-SettingFragment", "${i.key} receive as $i")
                    if("${i.key}" == "sms_setting") {
                        Log.d("Shamera-SettingFragment", "${i.key} filter as $i")
                        filterkey = i.child("filterKeyword").getValue().toString()
                        repsms = i.child("replySMS").getValue().toString()
                    }
                    if("${i.key}" == "sms_setting") {
                        Log.d("Shamera-SettingFragment", "${i.key} filter as $i")
                        sirentune = i.child("sirenName").getValue().toString()
                    }
                }
                v.editTextFilterKeyword.setText(filterkey)
                v.editTextFilterReply.setText(repsms)

                tuneArrayIndex = tuneArray.indexOf(sirentune)
                listView.setItemChecked(tuneArrayIndex, true)
                Log.d("Shamera-SettingFragment", "onDataChange id: $tuneId and name: $tuneName and db value: $sirentune")

            }
        }
        ref.addValueEventListener(getdatafilersms)
        ref.addListenerForSingleValueEvent(getdatafilersms)

        v.btnApply.setOnClickListener({
            // Write a message to the database
            var filterKeyword = v.editTextFilterKeyword.text.toString().trim()
            var replySMS = v.editTextFilterReply.text.toString().trim()

            if (filterKeyword.isEmpty()) {
                editTextFilterKeyword.error = "Cannot be Empty!!! Add the Filter Key Word"
                return@setOnClickListener
            }
            if (replySMS.isEmpty()) {
                editTextFilterReply.error = "Cannot be Empty!!! Add the Reply Message"
                return@setOnClickListener
            }


            val myData = FilterSMS(filterKeyword, replySMS, tuneName)
            ref.child("sms_setting").setValue(myData).addOnCompleteListener {
                Toast.makeText(context, "Add / Update center db", Toast.LENGTH_SHORT).show()
            }


        })
        return v
    }
}