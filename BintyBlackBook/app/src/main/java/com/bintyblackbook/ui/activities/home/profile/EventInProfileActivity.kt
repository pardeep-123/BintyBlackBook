package com.bintyblackbook.ui.activities.home.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventInProfileAdapter
import com.bintyblackbook.models.EventsModel
import com.bintyblackbook.ui.activities.home.AddEventActivity
import com.bintyblackbook.ui.dialogues.EventDeleteDialogFragment
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_event_in_profile.*

class EventInProfileActivity : AppCompatActivity() {

    var eventInProfileAdapter: EventInProfileAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_in_profile)
        rvEvents.layoutManager = GridLayoutManager(this, 2)

        rlBack.setOnClickListener {
            finish()
        }

        rlAdd.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }

        init()
    }

    private fun init() {
        val arrayList = ArrayList<EventsModel>()
        arrayList.add(EventsModel(R.drawable.john, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.girl1, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.matinn, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.girl, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.girl1, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.mattrin, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.john, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.matinn, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.girl, "John", "Arizona, USA", null))
        arrayList.add(EventsModel(R.drawable.mattrin, "John", "Arizona, USA", null))

        eventInProfileAdapter = EventInProfileAdapter(this, arrayList)
        rvEvents.adapter = eventInProfileAdapter
        adapterItemClick()
    }

    private fun adapterItemClick() {
        eventInProfileAdapter?.onItemClick = {eventsModel: EventsModel, clickOn: String ->
            if (clickOn.equals("editClick")){
                val intent = Intent(this,AddEventActivity::class.java)
                intent.putExtra(AppConstant.HEADING, "Edit Event")
                startActivity(intent)
            }else if(clickOn.equals("deleteClick")){
                val eventDeleteDialog = EventDeleteDialogFragment()
                eventDeleteDialog.show(supportFragmentManager,"eventDelete")
            }
        }
    }
}