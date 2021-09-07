package com.bintyblackbook.ui.activities.home.timeline

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CommentsAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.CommentData
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.CommentsViewModel
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

class CommentsActivity : BaseActivity() {

    var commentsAdapter:CommentsAdapter? = null

    var arrayList= ArrayList<CommentData>()
    var post_id =""

    lateinit var commentsViewModel: CommentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        post_id = intent.getStringExtra("post_id").toString()

        commentsViewModel= CommentsViewModel(this)

        setOnClicks()

        setAdapter()
        getCommentList(post_id)

    }

    private fun setOnClicks() {
        rlSend.setOnClickListener {
            if(InternetCheck.isConnectedToInternet(context)
                && Validations.isEmpty(context,edtMsg,getString(R.string.err_comment))){

                commentsViewModel.addComment(getSecurityKey(this)!!, getUser(this)?.authKey!!,post_id,edtMsg.text.toString())
                commentsViewModel.addCommentLiveData.observe(this, Observer {

                    if(it.code==200){
                        edtMsg.text.clear()
                        showAlertWithOk(it.msg)
                        getCommentList(post_id)
                    }
                })
            }
        }

        rlBack.setOnClickListener {
            finish()
        }
    }

    private fun getCommentList(postId: String) {
        commentsViewModel.getComments(getSecurityKey(context)!!, getUser(context)?.authKey!!,postId)

        commentsViewModel.commentLiveData.observe(this, Observer {
            if(it.code==200){
                if(it.data.size==0){
                    rvComments.visibility=View.GONE
                    tvNoComments.visibility=View.VISIBLE
                } else{
                    tvNoComments.visibility= View.GONE
                    rvComments.visibility=View.VISIBLE
                    arrayList.clear()
                    arrayList.addAll(it.data)
                    commentsAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setAdapter() {
        rvComments.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentsAdapter(this)
        rvComments.adapter = commentsAdapter
        commentsAdapter?.arrayList=arrayList
    }

    fun deleteComment(){
        commentsViewModel.deleteComment(getSecurityKey(this)!!, getUser(this)?.authKey!!,"")
        commentsViewModel.addCommentLiveData.observe(this, Observer {

            if(it.code==200){
                arrayList.removeAt(0)
                commentsAdapter?.notifyDataSetChanged()
            }

        })
    }
}