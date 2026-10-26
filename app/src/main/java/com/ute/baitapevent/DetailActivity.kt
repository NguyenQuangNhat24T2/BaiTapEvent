package com.ute.baitapevent // Nhớ đổi theo package của project bạn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ute.baitapevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent gửi sang
        val name = intent.getStringExtra("EXTRA_NAME")
        val gender = intent.getStringExtra("EXTRA_GENDER")
        val socials = intent.getStringExtra("EXTRA_SOCIALS")

        // Đưa dữ liệu lên các TextView
        binding.tvResultName.text = "Họ tên: $name"
        binding.tvResultGender.text = "Giới tính: $gender"
        binding.tvResultSocials.text = "Mạng xã hội: $socials"
    }
}