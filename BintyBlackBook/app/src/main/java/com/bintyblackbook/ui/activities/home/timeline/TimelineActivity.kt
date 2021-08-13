package com.bintyblackbook.ui.activities.home.timeline

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.TimelineAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.PostData
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.PostDeleteDialogFragment
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.PostsViewModel
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : BaseActivity() {

    var timelineAdapter:TimelineAdapter? = null
    val postList = ArrayList<PostData>()
    lateinit var postsViewModel: PostsViewModel

    var post_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        postsViewModel= PostsViewModel(this)
        setOnClicks()

        setAdapter()

        getAllPostList()

//        arrayList.add(TimelineModel(R.drawable.john,"Jhon","2m ago",getString(R.string.dummy_text),"100","50",R.drawable.background,false,true))
//        arrayList.add(TimelineModel(R.drawable.malli,"Malli","2m ago",getString(R.string.dummy_text),"100","50",null,false,false))
//        arrayList.add(TimelineModel(R.drawable.matinn,"Matinn","2m ago",getString(R.string.dummy_text),"90","50",null,false,false))
//        arrayList.add(TimelineModel(R.drawable.alina,"Alina","2m ago",getString(R.string.dummy_text),"100","50",null,true,false))
//        arrayList.add(TimelineModel(R.drawable.loop1,"Loop","2m ago",getString(R.string.dummy_text),"80","50",null,false,false))
//        arrayList.add(TimelineModel(R.drawable.sophia12,"Sophia","2m ago",getString(R.string.dummy_text),"30","50",null,false,false))
//        arrayList.add(TimelineModel(R.drawable.shren,"Shren","2m ago",getString(R.string.dummy_text),"100","50",null,false,false))

    }

    private fun getAllPostList() {
        postsViewModel.allPostList(getSecurityKey(this)!!, getUser(this)?.authKey!!)

        postsViewModel.postListLiveData.observe(this, Observer {

            if(it.code==200){
                if(it?.data?.size==0){
                    tvNoPost.visibility=View.VISIBLE
                    rvTimeline.visibility=View.GONE
                }

                else{
                    tvNoPost.visibility=View.GONE
                    rvTimeline.visibility=View.VISIBLE
                    postList.addAll(it?.data!!)
                    timelineAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setAdapter() {
        rvTimeline.layoutManager = LinearLayoutManager(this)
        timelineAdapter = TimelineAdapter(this)

        rvTimeline.adapter = timelineAdapter

        timelineAdapter?.arrayList=postList

        adapterItemClickEditOrDelete()
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        ivAdd.setOnClickListener {
            startActivity(Intent(this,AddPostActivity::class.java))
        }
    }

    private fun adapterItemClickEditOrDelete(){
        timelineAdapter?.onItemClick ={ timelineModel: PostData, clickOn: String ->
            if (clickOn=="imageClick" || clickOn=="nameClick"){
                val intent = Intent(this,UserDetailActivity::class.java)
                intent.putExtra("user_id",timelineModel.id)
                intent.putExtra(AppConstant.SHOW_CHAT_BTN,true)
                startActivity(intent)

            }else if (clickOn=="editClick"){
                val intent = Intent(this,AddPostActivity::class.java)
                intent.putExtra("post_id",timelineModel.id)
                intent.putExtra("description",timelineModel.description)
                intent.putExtra("image",timelineModel.userImage)
                intent.putExtra(AppConstant.HEADING,"Edit Past")
                startActivity(intent)

            }else if(clickOn=="deleteClick"){
                post_id=timelineModel.id.toString()
                val dialog = PostDeleteDialogFragment(this)
                dialog.show(supportFragmentManager,"postDelete")
            }
        }
    }

    fun deletePost(){
        postsViewModel.deletePost(getSecurityKey(context)!!, getUser(context)?.authKey!!,post_id)
        postsViewModel.addPostLiveData.observe(this, Observer {
            if(it.code==200){
                getAllPostList()
            }
        })
    }
}