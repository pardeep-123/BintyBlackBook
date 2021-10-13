package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AccountsAdapter
import com.bintyblackbook.model.Data
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.ui.activities.authentication.SignupActivity
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.getUserList
import com.bintyblackbook.util.saveUser
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_add_account.*

class AddAccountDialogFragment(var homeActivity: HomeActivity) : BottomSheetDialogFragment(), View.OnClickListener,AccountsAdapter.AccountInterface {
    private lateinit var behavior: BottomSheetBehavior<View>

    val accountsAdapter:AccountsAdapter by lazy {
        AccountsAdapter(requireContext())
    }
    var userList= ArrayList<Data>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return inflater.inflate(R.layout.layout_add_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()

        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.peekHeight = displayMetrics.heightPixels
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT
//            bottomSheet.layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT
        }
        try{

            userList=getUserList(requireContext())!!
                //removeDuplicates(getUserList(requireContext())!!)
        }catch (e: Exception){
            e.printStackTrace()
        }


        setAdapter()
        setOnClicks()
        //callback.callbackInterest(listOf)

    }

    // Function to remove duplicates from an ArrayList
    open fun removeDuplicates(list: ArrayList<Data>): ArrayList<Data> {

        // Create a new ArrayList
        val newList = ArrayList<Data>()


        // Traverse through the first list
        for (element in list) {

            // If this element is not present in newList
            // then add it

            if (!newList.contains(element)) {
                newList.add(element)
            }
        }

        // return the new list
        return newList
    }

    private fun setOnClicks() {
        btnLogin.setOnClickListener(this)
        tvNewAcc.setOnClickListener(this)
        tvAddAcc.setOnClickListener(this)
    }


    private fun setAdapter() {
        rvAccounts.layoutManager = LinearLayoutManager(requireContext())
        rvAccounts.adapter = accountsAdapter
        accountsAdapter.accountInterface=this
        accountsAdapter.list=userList
        accountsAdapter.notifyDataSetChanged()
        // sortAdapter.recyclerItemClick=this
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnLogin -> {
                dialog?.dismiss()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finishAffinity()
            }

            R.id.tvNewAcc -> {
                dialog?.dismiss()
                startActivity(Intent(requireActivity(), SignupActivity::class.java))
                requireActivity().finishAffinity()
            }

            R.id.tvAddAcc -> {
                group.visibility = View.VISIBLE
                rvAccounts.visibility = View.GONE
                tvAddAcc.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(data: Data) {
//        if(!userList.contains(data)){
        saveUser(requireContext(), data)
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        dialog?.dismiss()
//        }

    }


}