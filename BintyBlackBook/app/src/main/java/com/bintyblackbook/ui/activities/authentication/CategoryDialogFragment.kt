package com.bintyblackbook.ui.activities.authentication

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CategoryAdapter
import com.bintyblackbook.model.CategoryData
import com.bintyblackbook.model.SubCategories
import com.bintyblackbook.util.CustomInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.item_view_categories.*

class CategoryDialogFragment(var categoryList: ArrayList<CategoryData>, var callback: CustomInterface, var mcontext: Context) : BottomSheetDialogFragment() {
    private lateinit var behavior: BottomSheetBehavior<View>

    private var listOf: ArrayList<CategoryData> = arrayListOf()
    var subList:ArrayList<SubCategories> = arrayListOf()

    val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(categoryList, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return inflater.inflate(R.layout.item_view_categories, container, false)
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
            bottomSheet.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT

        }
    }
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setAdapter()

        btnDone.setOnClickListener {
            dialog?.dismiss()
            callback.callbackMethod(listOf)
            callback.callbackSubCategories(subList)
        }
    }


    private fun setAdapter() {

        rvCategories.layoutManager= LinearLayoutManager(requireContext())
        rvCategories.adapter=categoryAdapter
        //sortAdapter.recyclerItemClick=this
    }

    fun updateList(listOf: ArrayList<CategoryData>) {
        this.listOf = listOf
    }

    fun subCategoryList(listOf: ArrayList<SubCategories>) {
        this.subList = listOf
    }

}