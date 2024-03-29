package com.example.wintopia.view.list

import android.annotation.SuppressLint
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityMainBinding
import com.example.wintopia.databinding.FragmentListBinding
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.utils.SwipeHelperCallback
import com.example.wintopia.view.regist.RegistActivity
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.example.wintopia.view.utilssd.Constants.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment() {

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreschLayout
    }
    lateinit var binding: FragmentListBinding
    lateinit var data: List<MilkCowInfoModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        cowInfo("test")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)


        // sound effect
        val soundPool = SoundPool.Builder().build()
        var soundId :Int = 0
        soundId = soundPool.load(requireContext(), R.raw.cow2, 1)

        binding.fbListRegist.setOnClickListener {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            val intent = Intent(activity, RegistActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    // 서버에서 전체 정보 가져오기
    fun cowInfo(userId: String) {
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowListAll(userId) //통신 API 패스 설정

        call?.enqueue(object : Callback<MutableList<MilkCowInfoModel>> {
            @SuppressLint("ClickableViewAccessibility")
            override fun onResponse(call: Call<MutableList<MilkCowInfoModel>>, response: Response<MutableList<MilkCowInfoModel>>) {
                if (response.isSuccessful) {
                    Log.d(TAG,""+response?.body().toString())
                    data = response?.body()!!

                    val listAdapter = ListVOAdapter(data as MutableList<MilkCowInfoModel>)

                    listAdapter.reload(data)

                    binding.rvList.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = listAdapter

                        swipeRefreshLayout.setOnRefreshListener {
                            cowInfo(userId)
                            swipeRefreshLayout.isRefreshing = false
                        }

                        var swipeHelperCallback = SwipeHelperCallback().apply {
                            setClamp(200f)
                        }
                        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
                        itemTouchHelper.attachToRecyclerView(binding.rvList)
                        setOnTouchListener {v, event->
                            swipeHelperCallback.removePreviousClamp(binding.rvList)
                            false
                        }
                    }

                } else {
                }
            }

            override fun onFailure(call: Call<MutableList<MilkCowInfoModel>>, t: Throwable) {
                Log.d("로그",t.message.toString())
            }
        })
    }

}