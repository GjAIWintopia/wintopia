package com.example.wintopia.view.regist

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.data.UserList
import com.example.wintopia.dialog.Custumdialog
import com.example.wintopia.dialog.MyCustomDialog
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistViewModel: ViewModel() {

    val event = MutableLiveData<String>()
    val eventCowId = MutableLiveData<String>()
    val registEvent = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val birth = MutableLiveData<String>()
    val variety = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val vaccine = MutableLiveData<String>()
    val pregnancy = MutableLiveData<String>()
    val milk = MutableLiveData<String>()
    val castration = MutableLiveData<String>()
    val fax = MutableLiveData<String>()
    val imgFileList = arrayListOf<MultipartBody.Part>()
    val imgList = arrayListOf<Uri?>()
    val milkCowInfoModel = MutableLiveData<MilkCowInfoModel>()
    var cowInfoEvent: MilkCowInfoModel? = null

    fun sendImage(cow_id:String, imgFileList: ArrayList<MultipartBody.Part>) {
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowImageList(imgFileList) //통신 API 패스 설정


        call?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    event.value = "success"
                    eventCowId.value = response?.body().toString()

                   // Toast.makeText(applicationContext,"통신성공", Toast.LENGTH_SHORT).show()
                } else {
                    event.value = "failed"
                  //  Toast.makeText(applicationContext,"통신실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }

    // 버튼 이벤트 전에 사진 올렸을 때 자동으로 개체 정보 확인해서
    // 소가 아니면 다시 찍어달라 다이얼로그, 등록 개체는 등록 불가, 미등록 개채는 나머지 정보 입력
    // 소 정보 신규 등록
    fun registCowInfo(user_id: String, milkCowInfoModel: MilkCowInfoModel) {
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<String>? = service.cowInfoRegist(user_id, milkCowInfoModel)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                if (response.isSuccessful()) {
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = response.body()
                    registEvent.value = "success"

                } else {
                    // 서버통신 실패
                    registEvent.value = "fail1"
                }
            }
            override fun onFailure(call: Call<String?>?, t: Throwable) {
                // 통신 실패
                registEvent.value = "fail2"
            }
        })
    }


    fun cowInfoOne(cow_id: String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<List<MilkCowInfoModel>> = service.getData(cow_id)
        call!!.enqueue(object : Callback<List<MilkCowInfoModel>> {
            override fun onResponse(call: Call<List<MilkCowInfoModel>>, response: Response<List<MilkCowInfoModel>>) {
                if (response.isSuccessful()) {

                    var result = response.body()!!.get(0)

                    cowInfoEvent = MilkCowInfoModel(result.id, result.name, result.birth, result.variety,
                                                    result.gender, result.vaccine, result.pregnancy, result.milk,
                                                    result.castration, result.list, result.num)

                } else {
                    // 서버통신 실패
                }
            }
            override fun onFailure(call: Call<List<MilkCowInfoModel>>, t: Throwable) {
                // 통신 실패
            }
        })
    }

}