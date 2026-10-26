package com.ute.baitapevent // Nhớ đổi theo package của project bạn

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.ute.baitapevent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastClickTime: Long = 0L
    private val CLICK_DEBOUNCE_DELAY = 1000L // Chống click đúp trong 1 giây[cite: 4, 5]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Lắng nghe thao tác gõ phím trên EditText (Hiển thị Toast tương tác)[cite: 7]
        binding.etFullName.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                Toast.makeText(this, "Đang nhập: $text", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Lắng nghe sự kiện chọn RadioGroup Giới tính[cite: 9]
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            val gender = when (checkedId) {
                R.id.rbMale -> "Nam"
                R.id.rbFemale -> "Nữ"
                else -> "Khác"
            }
            Toast.makeText(this, "Đã chọn giới tính: $gender", Toast.LENGTH_SHORT).show()
        }

        // 3. Lắng nghe sự kiện CheckBox Mạng xã hội[cite: 8, 9]
        val socialListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val socialName = buttonView.text.toString()
            val status = if (isChecked) "Đã chọn" else "Đã bỏ chọn"
            Toast.makeText(this, "$status: $socialName", Toast.LENGTH_SHORT).show()
        }
        binding.cbFacebook.setOnCheckedChangeListener(socialListener)
        binding.cbZalo.setOnCheckedChangeListener(socialListener)
        binding.cbInstagram.setOnCheckedChangeListener(socialListener)

        // 4. Xử lý nút Đăng ký (Có chống click đúp & chuyển màn hình kèm dữ liệu)[cite: 4, 5]
        binding.btnSubmit.setOnClickListener {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime < CLICK_DEBOUNCE_DELAY) {
                return@setOnClickListener // Bấm quá nhanh, bỏ qua[cite: 5]
            }
            lastClickTime = currentTime

            val name = binding.etFullName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val genderId = binding.rgGender.checkedRadioButtonId
            val gender = when (genderId) {
                R.id.rbMale -> "Nam"
                R.id.rbFemale -> "Nữ"
                R.id.rbOther -> "Khác"
                else -> "Chưa chọn"
            }

            val socials = mutableListOf<String>()
            if (binding.cbFacebook.isChecked) socials.add("Facebook")
            if (binding.cbZalo.isChecked) socials.add("Zalo")
            if (binding.cbInstagram.isChecked) socials.add("Instagram")
            val socialsStr = if (socials.isNotEmpty()) socials.joinToString(", ") else "Không chọn"

            // Chuyển sang DetailActivity và truyền dữ liệu qua Intent
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("EXTRA_NAME", name)
                putExtra("EXTRA_GENDER", gender)
                putExtra("EXTRA_SOCIALS", socialsStr)
            }
            startActivity(intent)
        }
    }
}