package com.example.wintopia.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityLoginBinding
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        observeData()

        binding.btnLoginJoin.setOnClickListener(){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인을 위한 예비 아이디 비번
        var userId = "test"
        val userPw = "1234"

        binding.btnLoginLgin.setOnClickListener(){
            val id = binding.etLoginId.text.toString()
            val pw = binding.etLoginPw.text.toString()

            // 로그인 성공
            if (id.equals(userId) && pw.equals(userPw)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // 로그인 실패
                Toast.makeText(this, "ID 혹은 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun observeData() {
        viewModel.id.observe(this){
            binding.edt.text = it
        }
    }
}

//class LoginViewModel : ViewModel() {
//    val name = MutableLiveData<String>()
//}