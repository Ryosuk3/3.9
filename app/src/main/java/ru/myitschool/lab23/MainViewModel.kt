package ru.myitschool.lab23

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val operationList: MutableLiveData<List<ExpenseItem>> by lazy{
        MutableLiveData<List<ExpenseItem>>()
    }
}