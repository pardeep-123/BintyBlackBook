package com.bintyblackbook.ui.activities.home.timeline

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.TimelineAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.PostData
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.activities.home.profileUser.MyProfileActivity
import com.bintyblackbook.ui.dialogues.PostDeleteDialogFragment
import com.bintyblackbook.ui.dialogues.ReportUserDialogFragment
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.PostsViewModel
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : BaseActivity(),TimelineAdapter.TimeLineInterface {

    var timelineAdapter:TimelineAdapter? = null
    val postList = ArrayList<PostData>()
    lateinit var postsViewModel: PostsViewModel

    var post_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        postsViewModel= PostsViewModel()
        setOnClicks()

        setAdapter()

        getAllPostList()
    }

    private fun getAllPostList() {
        postsViewModel.allPostList(this,getSecurityKey(this)!!, getUser(this)?.authKey!!)

        postsViewModel.postListLiveData.observe(this, Observer {

            if(it.code==200){
                if(it?.data?.size==0){
                    tvNoPost.visibility=View.VISIBLE
                    rvTimeline.visibility=View.GONE
                } else{
                    tvNoPost.visibility=View.GONE
                    rvTimeline.visibility=View.VISIBLE
                    postList.clear()
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
        timelineAdapter?.timeLineInterface=this
        timelineAdapter?.arrayList=postList

        //adapterItemClickEditOrDelete()
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        ivAdd.setOnClickListener {
            val intent=Intent(this,AddPostActivity::class.java)
            intent.putExtra("type","Add Post")
            startActivity(intent)
        }
    }

    private fun adapterItemClickEditOrDelete(){
       /* timelineAdapter?.onItemClick = { timelineModel: PostData, clickOn: String, position:Int ->
            if (clickOn=="imageClick" || clickOn=="nameClick"){

                if(timelineModel.userId== getUser(context)?.id){
                    val intent= Intent(this, MyProfileActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this,UserDetailActivity::class.java)
                    intent.putExtra("user_id",timelineModel.userId.toString())
                    intent.putExtra(AppConstant.SHOW_CHAT_BTN,true)
                    startActivity(intent)
                }

            }else if (clickOn=="editClick"){
                val intent = Intent(this,AddPostActivity::class.java)
                intent.putExtra("post_id",timelineModel.id.toString())
                intent.putExtra("description",timelineModel.description)
                intent.putExtra("image",timelineModel.image)
                intent.putExtra(AppConstant.HEADING,"Edit Post")
                intent.putExtra("screen_type","Edit Post")
                startActivity(intent)

            }else if(clickOn=="deleteClick"){
                post_id=timelineModel.id.toString()
                val dialog = PostDeleteDialogFragment(this,position)
                dialog.show(supportFragmentManager,"postDelete")
            }
        }*/

        /*timelineAdapter?.onCommentClick={ timelineModel: PostData->
            val intent = Intent(context,CommentsActivity::class.java)
            intent.putExtra("post_id",timelineModel.id.toString())
            startActivity(intent)
        }*/
    }

    fun deletePost(position:Int){
       postsViewModel.deletePost(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,post_id)
        postsViewModel.baseResponseLiveData.observe(this, Observer {
            postList.removeAt(position)
            timelineAdapter?.notifyDataSetChanged()

        })
    }

    override fun onRestart() {
        getAllPostList()
        super.onRestart()
    }

    fun likeDislikePost(){
        postsViewModel.likeDislikePost(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,"","")
        postsViewModel.baseResponseLiveData.observe(this, Observer {

            if(it.code==200){
                Log.i("====",it.msg)
            }

        })
    }

    override fun onProfileClick(data: PostData, position: Int) {
        if(data.userId== getUser(context)?.id){
            val intent= Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this,UserDetailActivity::class.java)
            intent.putExtra("user_id",data.userId.toString())
            intent.putExtra(AppConstant.SHOW_CHAT_BTN,true)
            startActivity(intent)
        }
    }

    override fun onEditItem(data: PostData, position: Int) {
        val intent = Intent(this,AddPostActivity::class.java)
        intent.putExtra("post_id",data.id.toString())
        intent.putExtra("description",data.description)
        intent.putExtra("image",data.image)
        intent.putExtra(AppConstant.HEADING,"Edit Post")
        intent.putExtra("screen_type","Edit Post")
        startActivity(intent)
    }

    override fun onDeleteItem(data: PostData, position: Int) {
        post_id=data.id.toString()
        val dialog = PostDeleteDialogFragment(this,position)
        dialog.show(supportFragmentManager,"postDelete")
    }

    override fun onCommentSelect(data: PostData, position: Int) {
        val intent = Intent(context,CommentsActivity::class.java)
        intent.putExtra("post_id",data.id.toString())
        startActivity(intent)
    }

    override fun onReportPost(data: PostData, position: Int) {
        post_id= data.id.toString()
        val dialogFragment = ReportUserDialogFragment(this)
        dialogFragment.show(supportFragmentManager, "reportUser")
    }

    fun reportPost(report_text: String) {
        postsViewModel.report_post(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,post_id,report_text)
        postsViewModel.baseResponseLiveData.observe(this, Observer {

            if(it.code==200){
                Log.i("====",it.msg)
            }
        })
    }
}