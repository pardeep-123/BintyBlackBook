package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.SubCategories
import com.bintyblackbook.ui.activities.authentication.CategoryDialogFragment
import kotlinx.android.synthetic.main.item_view_sub_categories.view.*

class SubCategoryAdapter(var context: Context, val categoryDialogFragment: CategoryDialogFragment,var list: ArrayList<SubCategories>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolderGrid(inflater.inflate(R.layout.item_view_sub_categories, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolderGrid).bindItems(list[position], position, list,categoryDialogFragment)
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolderGrid(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bindItems(data: SubCategories, position: Int, list: ArrayList<SubCategories>, categoryDialogFragment: CategoryDialogFragment
        ) {

            itemView.apply {
                tvSubCategory.text = data.name

                itemView.cbSubCategory.setOnCheckedChangeListener { p0, isChecked -> categoryDialogFragment.subCategoryList(list)
                    if (isChecked){
                          list[position].isSelect = true
                         categoryDialogFragment.subCategoryList(list)
                        cbSubCategory.isChecked=true
                    }else{
                        cbSubCategory.isChecked=false

                    }

                }

                if (list.get(position).isSelect){
                    cbSubCategory.isChecked=true
                }else{
                    cbSubCategory.isChecked=false

                }
            }

        }
    }
}