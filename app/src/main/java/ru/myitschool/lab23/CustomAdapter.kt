package ru.myitschool.lab23


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.AdapterView.OnItemLongClickListener

class CustomAdapter(private val itemList: MutableList<ExpenseItem>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.expense_type_text)
        val dateTextView: TextView = itemView.findViewById(R.id.expense_date_text)
        val amountTextView: TextView = itemView.findViewById(R.id.expense_amount_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun addItem(item: ExpenseItem) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    fun updateItems(newItems: List<ExpenseItem>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
        notifyItemInserted(itemList.size - 1)
    }
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    private var longClickListener: OnItemLongClickListener? = null

    fun setOnLongItemClickListener(listener: OnItemLongClickListener) {
        this.longClickListener = listener
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.typeTextView.text = item.type
        holder.dateTextView.text = item.date
        holder.amountTextView.text = item.amount.toString()


        holder.itemView.setOnLongClickListener {
            longClickListener?.onItemLongClick(position)
            true // Важно вернуть true, чтобы указать, что событие обработано
        }
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun duplicateItem(position: Int) {
        val item = itemList[position]
        itemList.add(position + 1, item.copy())
        notifyItemInserted(position + 1)
    }
    fun getItems(): List<ExpenseItem> {
        return itemList
    }


}