package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.databinding.ItemViewCategoryCheckboxBinding
import com.bintyblackbook.model.CategoryData
import com.bintyblackbook.model.SubCategories
import com.bintyblackbook.ui.activities.authentication.CategoryDialogFragment

class CategoryAdapter(var list: ArrayList<CategoryData>, val categoryDialogFragment: CategoryDialogFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var categoryList=ArrayList<CategoryData>()
    var subCategoryList=ArrayList<SubCategories>()

    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding= ItemViewCategoryCheckboxBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val holder= MyViewHolderGrid(binding)
        mContext= parent.context
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder=holder as MyViewHolderGrid

        viewHolder.viewBinding.tvCategory.text=categoryList[position].name
        viewHolder.viewBinding.arrow.visibility=View.GONE

        var adapter=SubCategoryAdapter(mContext, categoryDialogFragment,categoryList.get(position).subCategories)
        viewHolder.viewBinding.rvSubCategory.layoutManager=LinearLayoutManager(mContext,RecyclerView.VERTICAL,false)
        viewHolder.viewBinding.rvSubCategory.adapter=adapter
       // subCategoryAdapter.list=subCategoryList

        for (i in 0 until list.size){
            categoryList[i].isSelect=false
        }

        viewHolder.viewBinding.cbCategory.setOnCheckedChangeListener { p0, isChecked ->

            categoryList[position].isSelect = true
            categoryDialogFragment.updateList(categoryList)
            if (isChecked){
                viewHolder.viewBinding.rvSubCategory.visibility=View.VISIBLE

                for (i in 0 until   categoryList.get(position).subCategories.size){
                    categoryList.get(position).subCategories.get(i).isSelect=true
                }
                adapter.notifyDataSetChanged()
            }else{
                viewHolder.viewBinding.rvSubCategory.visibility=View.GONE

            }

        }

        viewHolder.viewBinding.tvCategory.setOnClickListener {
            viewHolder.viewBinding.rvSubCategory.visibility=View.VISIBLE
        }
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolderGrid(var viewBinding:ItemViewCategoryCheckboxBinding):RecyclerView.ViewHolder(viewBinding.root)
    init {
        categoryList = list
    }
}

