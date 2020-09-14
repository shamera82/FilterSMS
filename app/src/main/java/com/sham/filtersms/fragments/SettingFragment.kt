package com.sham.filtersms.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sham.filtersms.R as ResData
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


//class SettingFragment : Fragment(), AdapterView.OnItemClickListener {
class SettingFragment : Fragment() {

    lateinit var listView : ListView
    private var tuneName = ""
    private var tuneArrayIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(ResData.layout.fragment_settings, container, false)

        // read from SharedPreferences
        val pref = activity?.getPreferences(Context.MODE_PRIVATE)
        var filterKeyword = pref?.getString("FILTER_KEY", "")
        var replySMS = pref?.getString("REPLY_SMS", "")
        var sirentune = pref?.getString("TUNE_NAME", "")
        tuneName = sirentune.toString()
        Log.d(
            "Shamera-SettingFragment",
            "filterKeyword: $filterKeyword and replySMS: $replySMS and sirentune: $sirentune")

        v.editTextFilterKeyword.setText(filterKeyword)
        v.editTextFilterReply.setText(replySMS)

        var tuneArray = resources.getStringArray(ResData.array.siren_tunes_list)
        listView = v.ListViewSiran
        listView.adapter = ArrayAdapter(
            v.context,
            android.R.layout.simple_list_item_multiple_choice,
            tuneArray
        )
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE


        tuneArrayIndex = tuneArray.indexOf(sirentune)
        listView.setItemChecked(tuneArrayIndex, true)
        Log.d(
            "Shamera-SettingFragment",
            "tuneArrayIndex: $tuneArrayIndex and sirentune: $sirentune"
        )
        listView.setOnItemClickListener{ parentFragment: AdapterView<*>?, view: View?, position: Int, id: Long ->
            tuneName = parentFragment?.getItemAtPosition(position) as String
            tuneArrayIndex = position
            Toast.makeText(v.context, "Select Tune $tuneName", Toast.LENGTH_SHORT).show()
            Log.d(
                "Shamera-SettingFragment",
                "listView.setOnItemClickListener tuneArrayIndex: $tuneArrayIndex and name: $tuneName"
            )
        }

        v.btnApply.setOnClickListener {
            filterKeyword = v.editTextFilterKeyword.text.toString().toUpperCase().trim()
            replySMS = v.editTextFilterReply.text.toString().toUpperCase().trim()

            if (filterKeyword!!.isEmpty()) {
                editTextFilterKeyword.error = "Cannot be Empty!!! Add the Filter Key Word"
                return@setOnClickListener
            }
            if (replySMS!!.isEmpty()) {
                editTextFilterReply.error = "Cannot be Empty!!! Add the Reply Message"
                return@setOnClickListener
            }

            // add to SharedPreferences
            val pref = activity?.getPreferences(Context.MODE_PRIVATE)
            val editor = pref?.edit()

            editor?.putString("FILTER_KEY", filterKeyword)
            editor?.putString("REPLY_SMS", replySMS)
            editor?.putString("TUNE_NAME", tuneName)

            var status = editor?.commit()
            if(status == true){
                Log.d(
                    "Shamera-SettingFragment",
                    "btnApply.setOnClickListener FILTER_KEY: $filterKeyword, REPLY_SMS: $replySMS and TUNE_NAME: $tuneName"
                )
                Toast.makeText(v.context, "Setting Changed", Toast.LENGTH_LONG).show()
            }else{
                Log.d(
                    "Shamera-SettingFragment",
                    "btnApply.setOnClickListener ERROR FILTER_KEY: $filterKeyword, REPLY_SMS: $replySMS and TUNE_NAME: $tuneName"
                )
                Toast.makeText(v.context, "ERROR!!! Filter Key set as : $filterKeyword, Reply Msg set as $replySMS and Tune set as $tuneName", Toast.LENGTH_LONG).show()

            }
        }
        return v
    }
}