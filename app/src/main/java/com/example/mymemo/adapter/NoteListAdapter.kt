package com.example.mymemo.adapter

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemo.R
import com.example.mymemo.entity.Noteinfo

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.MyHolder>() {

    private var noteInfoList: List<Noteinfo> = emptyList()

    fun setNoteInfoList(list: List<Noteinfo>) {
        Log.d("NoteListAdapter", "新数据数量: ${list.size}")
        noteInfoList = list
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_notelist, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val note = noteInfoList[position]
        holder.bind(note)

        //置顶加粗
        if (note.note_is_top == 1) {
            holder.ivTop.visibility = View.VISIBLE
            val typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            holder.tvTitle.typeface = typeface
            holder.tvContent.typeface = typeface
        } else {
            holder.ivTop.visibility = View.GONE
            val typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            holder.tvTitle.typeface = typeface
            holder.tvContent.typeface = typeface
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, note)
        }

        holder.ivMore.setOnClickListener {
            onItemClickListener?.onMore(position, note)
        }
    }

    override fun getItemCount(): Int = noteInfoList.size

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_note_title)
        val tvContent: TextView = itemView.findViewById(R.id.tv_note_content)
        val tvTime: TextView = itemView.findViewById(R.id.tv_note_create_time)
        val tvDay: TextView = itemView.findViewById(R.id.tv_day)
        val tvMonth: TextView = itemView.findViewById(R.id.tv_month)
        val ivMore: ImageView = itemView.findViewById(R.id.iv_more)
        val ivTop: ImageView = itemView.findViewById(R.id.iv_note_top)

        fun bind(note: Noteinfo) {
            tvTitle.text = note.getNoteTitle()
            tvContent.text = note.getNoteContent()
            tvTime.text = note.getNoteCreateTime()

            // 拆分时间
            val dateParts = note.getNoteCreateTime().split("-")
            if (dateParts.size >= 3) {
                tvMonth.text = dateParts[1]
                tvDay.text = dateParts[2]
            }

            // 置顶图标显示
            ivTop.visibility = if (note.getNoteIsTop() == 1) View.VISIBLE else View.GONE

//            // 更多按钮点击
//            ivMore.setOnClickListener {
//                Toast.makeText(itemView.context, "点击了更多：${note.getNoteTitle()}", Toast.LENGTH_SHORT).show()
//            }





        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, noteInfo: Noteinfo)
        fun onMore(position: Int, noteInfo: Noteinfo)
    }
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }




}
