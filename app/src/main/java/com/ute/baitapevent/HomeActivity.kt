package com.ute.baitapevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ute.baitapevent.databinding.ActivityHomeBinding
import com.ute.baitapevent.model.User

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhan du lieu User tu Intent
        val user = intent.getSerializableExtra("EXTRA_USER") as? User
        user?.let {
            binding.tvName.text = "Họ tên: ${it.fullName}"
            binding.tvEmail.text = "Email: ${it.email}"
            binding.tvPhone.text = "Số điện thoại: ${it.phone}"
            binding.tvGender.text = "Giới tính: ${it.gender}"
        }
    }
}