package com.example.wintopia.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivitySignUpBinding
import com.example.wintopia.view.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        observeData()

        // 로그인페이지로 돌아가기
        binding.btnJoinback.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 누를 경우
        binding.btnJoinJoin.setOnClickListener(){

        }

    }

    fun observeData() {
        viewModel.id.observe(this){
            binding.edt.text = it
        }
    }


}