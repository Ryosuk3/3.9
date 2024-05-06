package ru.myitschool.lab23
import java.io.Serializable

data class ExpenseItem(val type: String, val date: String, val amount: Double) : Serializable