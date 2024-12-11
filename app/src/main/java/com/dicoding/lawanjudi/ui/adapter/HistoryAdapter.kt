package com.dicoding.lawanjudi.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.database.local.entity.ReportEntity
import com.dicoding.lawanjudi.databinding.ItemReportBinding
import com.dicoding.lawanjudi.ui.detail.ReportDetailActivity

class HistoryAdapter: ListAdapter<ReportEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    class MyViewHolder(val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(report: ReportEntity){
            binding.imgAiStatus.setImageResource(if(report.aiConfirmed == true) R.drawable.ic_check else R.drawable.ic_cross)
            binding.tvLaporanId.text = "Laporan ${report.id}"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ReportDetailActivity::class.java)
                intent.putExtra(ID, report.id)
                startActivity(itemView.context, intent, null)
            }
        }
    }

    companion object{
        private const val ID = "reportId"

        val DIFF_CALLBACK: DiffUtil.ItemCallback<ReportEntity> =
            object: DiffUtil.ItemCallback<ReportEntity>() {
                override fun areItemsTheSame(oldItem: ReportEntity, newItem: ReportEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: ReportEntity, newItem: ReportEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}