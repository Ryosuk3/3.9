package ru.myitschool.lab23

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale


class MainActivity : AppCompatActivity(), CustomAdapter.OnItemLongClickListener {

    private lateinit var balanceTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var viewModel: MainViewModel
    private var selectedPosition: Int = -1
    private val itemList: MutableList<ExpenseItem> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        balanceTextView = findViewById(R.id.ef_current_balance_text)
        recyclerView = findViewById(R.id.ef_expenses_rv)

        adapter = CustomAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        updateBalance(0.0)

        val addFab: FloatingActionButton = findViewById(R.id.add_fab)
        addFab.setOnClickListener {
            val dialog = Dialog()
            dialog.show(supportFragmentManager, "AddOperationDialog")
        }

        viewModel.operationList.observe(this, Observer {
            adapter.updateItems(adapter.getItems())
        })
        if (savedInstanceState!=null){
            val balance=savedInstanceState.getDouble("balance")
            updateBalance(balance)
            val operationList: ArrayList<ExpenseItem>? = savedInstanceState.getSerializable("operationList") as ArrayList<ExpenseItem>?
            operationList?.let {
                adapter.updateItems(it)
            }
        }
        else{
            updateBalance(0.0)
            adapter.updateItems(emptyList())
        }
        adapter.setOnLongItemClickListener(this)
    }
    private fun calculateBalance(): Double {
        var balance = 0.0
        for (item in adapter.getItems()) {
            if (item.type == "Income") {
                balance += item.amount
            } else if (item.type == "Expenses") {
                balance -= item.amount
            }
        }
        return balance
    }
    private fun updateBalance(balance: Double) {
        balanceTextView.text = String.format(Locale.US,"%.1f", balance)
    }
    fun addItem(item: ExpenseItem) {
        itemList.add(item)
        adapter.updateItems(itemList)
        updateBalance(calculateBalance())
    }


    override fun onPause() {
        super.onPause()
        viewModel.operationList.removeObservers(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.operationList.observe(this, Observer {
            adapter.updateItems(adapter.getItems())
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("balance",balanceTextView.text.toString().toDouble())
        outState.putSerializable("operationList", ArrayList(adapter.getItems()))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val balance=savedInstanceState.getDouble("balance")
        updateBalance(balance)
    }
    override fun onItemLongClick(position: Int) {
        selectedPosition = position
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        if (viewHolder != null) {
            val popupMenu = PopupMenu(this, viewHolder.itemView)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        itemList.removeAt(position)
                        adapter.updateItems(itemList)
                        updateBalance(calculateBalance())
                        true
                    }
                    R.id.menu_duplicate -> {
                        val item = itemList[position]
                        itemList.add(item.copy())
                        adapter.updateItems(itemList)
                        updateBalance(calculateBalance())
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        } else {
            // Handle the case where the ViewHolder is null if necessary
        }
    }
}
