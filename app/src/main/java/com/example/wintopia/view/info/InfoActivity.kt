package com.example.wintopia.view.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityInfoBinding
import com.example.wintopia.view.edit.CowInfo
import com.example.wintopia.view.edit.EditActivity
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.Constants.TAG
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.utilssd.API_


class InfoActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityInfoBinding
    val viewModel: InfoViewModel by viewModels()
    lateinit var cowInfo: MilkCowInfoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_info)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // webView에 띄울 이미지 관련 설정들
        binding.wvInfoPhto.settings.useWideViewPort = true
        binding.wvInfoPhto.settings.loadWithOverviewMode = true
        binding.wvInfoPhto.settings.builtInZoomControls = true
        binding.wvInfoPhto.settings.setSupportZoom(true)

        setIntent(intent)
        observeData()

//        cowInfo = MilkCowInfoModel(viewModel.id.value.toString(), viewModel.name.value.toString(), viewModel.birth.value.toString(), viewModel.variety.value.toString(), viewModel.gender.toString(),
//                                   viewModel.vaccine.value.toString(), viewModel.pregnancy.value.toString(), viewModel.milk.value.toString(), viewModel.castration.value.toString(),
//                                   viewModel.wishEvent.value!!.toInt(), 0)


        // 즐겨찾기 별
        binding.imgInfoStar.setOnClickListener {
            viewModel.cowWish(binding.tvInfoId.text.toString())
            if (viewModel.wishEvent.equals("0")) {
                binding.imgInfoStar.setImageResource(R.drawable.filledstar)
                viewModel.wishEvent.value = "1"
            } else {
                binding.imgInfoStar.setImageResource(R.drawable.star)
                viewModel.wishEvent.value = "0"

            }
            cowWishEvent()
        }


        // 뒤로가기 버튼
        binding.imgInfoBack.setOnClickListener {
            finish()
        }

        // 수정하기 버튼
        binding.btnInfoEdit.setOnClickListener {
            Log.d(TAG, " 수정하기 버튼 클릭")
            //수정하기 페이지로 이동
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("cowInfo", cowInfo)
            startActivity(intent)
            finish()
        }

        // 삭제하기 버튼
        binding.btnInfoDelete.setOnClickListener {
            var cow_id = binding.tvInfoId.text.toString()
            viewModel.cowInfoDelete(cow_id)
            cowDeleteEvent()
            // 전체 리스트 페이지 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(fragment.id, fragment)
        fragmentTransaction.commit()
    }




//

    override fun setIntent(intent: Intent) {
        val intent = intent
        if(intent.getStringExtra("where").equals("list")) {
            cowInfo = intent.getSerializableExtra("infos") as MilkCowInfoModel
            binding.wvInfoPhto.loadUrl("${API_.BASE_URL}image/cowImgOut?cow_id=${cowInfo.id}")
            viewModel.name.value = (cowInfo.name)
            viewModel.id.value = (cowInfo.id)
            viewModel.birth.value = (cowInfo.birth)
            viewModel.variety.value = (cowInfo.variety)
            viewModel.gender.value = (cowInfo.gender)
            viewModel.vaccine.value = (cowInfo.vaccine)
            viewModel.pregnancy.value = (cowInfo.pregnancy)
            viewModel.milk.value = (cowInfo.milk)
            viewModel.castration.value = (cowInfo.castration)
            viewModel.wishEvent.value = (cowInfo.list.toString())



        } else if(intent.getStringExtra("where").equals("edit")) {
            val cow_id = intent.getStringExtra("TEXT")
            Log.d("확인ㅁㅈㅇ용", "$cow_id")
            viewModel.cowInfoOne(cow_id.toString())
//            binding.wvInfoPhto.loadUrl("${API_.BASE_URL}image/cowImgOut?cow_id${cowInfo.id}")
//            viewModel.name.value = (cowInfo.name)
//            viewModel.id.value = (cowInfo.id)
//            viewModel.birth.value = (cowInfo.birth)
//            viewModel.variety.value = (cowInfo.variety)
//            viewModel.gender.value = (cowInfo.gender)
//            viewModel.vaccine.value = (cowInfo.vaccine)
//            viewModel.pregnancy.value = (cowInfo.pregnancy)
//            viewModel.milk.value = (cowInfo.milk)
//            viewModel.castration.value = (cowInfo.castration)
//            viewModel.wishEvent.value = (cowInfo.list.toString())

        }

    }


    fun observeData() {
        viewModel.id.observe(this){
            binding.tvInfoId.text = it
        }
        viewModel.name.observe(this){
        binding.tvInfoName.text = it
        }
        viewModel.birth.observe(this){
            binding.tvInfoBirth.text = it
        }
        viewModel.variety.observe(this){
            binding.tvInfoVariety.text = it
        }
        viewModel.gender.observe(this){
            binding.rbInfoGender.checkedRadioButtonId
        }
        viewModel.vaccine.observe(this){
            binding.rbInfoVaccine.checkedRadioButtonId
        }
        viewModel.pregnancy.observe(this){
            binding.rbInfoPregnant.checkedRadioButtonId
        }
        viewModel.milk.observe(this){
            binding.rbInfoMilk.checkedRadioButtonId
        }
        viewModel.castration.observe(this){
            binding.rbInfoCas.checkedRadioButtonId
        }

    }


    fun cowWishEvent() {
        viewModel.wishEvent.observe(this){
            when(it){
                "0" ->{
                    Toast.makeText(this, "즐겨찾기 해제 완료", Toast.LENGTH_SHORT).show()
                }
                "1" ->{
                    Toast.makeText(this, "즐겨찾기 추가 완료", Toast.LENGTH_SHORT).show()
                }
                "fail" -> {
                    Toast.makeText(this, "즐겨찾기 실패", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(this, "통신상태 불량", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(this, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun cowDeleteEvent() {
        viewModel.deleteEvent.observe(this){
            when(it){
                "success" ->{
                    Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
                }
                "fail" -> {
                    Toast.makeText(this, "삭제 실패, 소의 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(this, "통신상태 불량", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(this, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}