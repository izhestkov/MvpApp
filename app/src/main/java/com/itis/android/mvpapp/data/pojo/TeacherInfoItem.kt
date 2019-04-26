package com.itis.android.mvpapp.data.pojo

import com.google.firebase.database.PropertyName
import com.itis.android.mvpapp.presentation.model.UserRole

data class TeacherInfoItem(
        var id: Int? = null,
        var email: String? = null,
        var first_name: String? = null,
        var last_name: String? = null,
        var middle_name: String? = null,
        var birth_date: String? = null,
        var phone: String? = null,
        var photo: String? = null,
        var role: UserRole? = null
)