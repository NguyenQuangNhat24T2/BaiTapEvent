package com.ute.baitapevent

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.ute.baitapevent.databinding.ActivityMainBinding
import com.ute.baitapevent.model.User
import com.ute.baitapevent.validator.FormValidator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastClickTime: Long = 0L
    private val CLICK_DEBOUNCE_DELAY = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRealtimeValidation()
        setupButtons()
    }

    // Xoa loi tuc thi khi nguoi dung go phim (Lab 5 - Bai 2)
    private fun setupRealtimeValidation() {
        binding.etFullName.doAfterTextChanged { binding.tilFullName.error = null }
        binding.etEmail.doAfterTextChanged { binding.tilEmail.error = null }
        binding.etPassword.doAfterTextChanged { binding.tilPassword.error = null }
        binding.etPhone.doAfterTextChanged { binding.tilPhone.error = null }
    }

    private fun setupButtons() {
        // Nut Dang Ky
        binding.btnSubmit.setOnClickListener {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime < CLICK_DEBOUNCE_DELAY) return@setOnClickListener
            lastClickTime = currentTime

            if (validateForm()) {
                // Success: Tao doi tuong User va chuyen sang man hinh trang chu
                val name = binding.etFullName.text.toString().trim()
                val email = binding.etEmail.text.toString().trim()
                val phone = binding.etPhone.text.toString().trim()
                val genderId = binding.rgGender.checkedRadioButtonId
                val gender = when (genderId) {
                    R.id.rbMale -> "Nam"
                    R.id.rbFemale -> "Nữ"
                    else -> "Khác"
                }

                val user = User(name, email, phone, gender)
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("EXTRA_USER", user)
                }
                startActivity(intent)
            } else {
                // Fail: Hien Snackbar bao loi
                Snackbar.make(binding.root, "Vui lòng kiểm tra lại thông tin nhập lỗi!", Snackbar.LENGTH_LONG).show()
            }
        }

        // Nut Huy / Lam moi: Xoa trang cac truong
        binding.btnCancel.setOnClickListener {
            resetForm()
            Snackbar.make(binding.root, "Đã xóa toàn bộ thông tin", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val name = binding.etFullName.text.toString().trim()
        if (!FormValidator.isValidName(name)) {
            binding.tilFullName.error = "Họ tên tối thiểu 3 ký tự"
            isValid = false
        }

        val email = binding.etEmail.text.toString().trim()
        if (!FormValidator.isValidEmail(email)) {
            binding.tilEmail.error = "Email không đúng định dạng chuẩn"
            isValid = false
        }

        val password = binding.etPassword.text.toString().trim()
        if (!FormValidator.isValidPassword(password)) {
            binding.tilPassword.error = "Mật khẩu tối thiểu 6 ký tự"
            isValid = false
        }

        val phone = binding.etPhone.text.toString().trim()
        if (!FormValidator.isValidPhone(phone)) {
            binding.tilPhone.error = "Số điện thoại Việt Nam không hợp lệ (10 số)"
            isValid = false
        }

        return isValid
    }

    private fun resetForm() {
        binding.etFullName.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etPhone.text?.clear()
        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilPhone.error = null
        binding.rgGender.check(R.id.rbMale)
        binding.etFullName.requestFocus()
    }
}