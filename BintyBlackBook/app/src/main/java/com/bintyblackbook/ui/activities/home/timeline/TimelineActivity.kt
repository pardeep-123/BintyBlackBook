package com.bintyblackbook.ui.activities.home.timeline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.TimelineAdapter
import com.bintyblackbook.models.TimelineModel
import com.bintyblackbook.ui.dialogues.PostDeleteDialogFragment
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : AppCompatActivity() {

    var timelineAdapter:TimelineAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        rlBack.setOnClickListener {
            finish()
        }

        ivAdd.setOnClickListener {
            startActivity(Intent(this,AddPostActivity::class.java))
        }

        val arrayList = ArrayList<TimelineModel>()
        arrayList.add(TimelineModel(R.drawable.john,"Jhon","2m ago",getString(R.string.dummy_text),"100","50",R.drawable.background,false,true))
        arrayList.add(TimelineModel(R.drawable.malli,"Malli","2m ago",getString(R.string.dummy_text),"100","50",null,false,false))
        arrayList.add(TimelineModel(R.drawable.matinn,"Matinn","2m ago",getString(R.string.dummy_text),"90","50",null,false,false))
        arrayList.add(TimelineModel(R.drawable.alina,"Alina","2m ago",getString(R.string.dummy_text),"100","50",null,true,false))
        arrayList.add(TimelineModel(R.drawable.loop1,"Loop","2m ago",getString(R.string.dummy_text),"80","50",null,false,false))
        arrayList.add(TimelineModel(R.drawable.sophia12,"Sophia","2m ago",getString(R.string.dummy_text),"30","50",null,false,false))
        arrayList.add(TimelineModel(R.drawable.shren,"Shren","2m ago",getString(R.string.dummy_text),"100","50",null,false,false))


        timelineAdapter = TimelineAdapter(this,arrayList)
        rvTimeline.adapter = timelineAdapter
        rvTimeline.layoutManager = LinearLayoutManager(this)
        adapterItemClickEditOrDelete()
    }

    private fun adapterItemClickEditOrDelete(){
        timelineAdapter?.onItemClick ={timelineModel: TimelineModel, clickOn: String ->
            if (clickOn=="editClick"){
                val intent = Intent(this,AddPostActivity::class.java)
                intent.putExtra(AppConstant.HEADING,"Edit Past")
                startActivity(intent)

            }else if(clickOn=="deleteClick"){
                val dialog = PostDeleteDialogFragment()
                dialog.show(supportFragmentManager,"postDelete")
            }
        }
    }
}