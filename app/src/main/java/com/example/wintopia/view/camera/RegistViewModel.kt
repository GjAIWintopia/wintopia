package com.example.wintopia.view.camera

import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistViewModel: ViewModel() {

    val registName = MutableLiveData<String>()
    val registId = MutableLiveData<String>()
    val registBirth = MutableLiveData<String>()
    val registStatus = MutableLiveData<String>()
    val registTel = MutableLiveData<String>()
    val registFax = MutableLiveData<String>()

}