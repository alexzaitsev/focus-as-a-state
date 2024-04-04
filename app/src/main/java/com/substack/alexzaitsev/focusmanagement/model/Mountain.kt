package com.substack.alexzaitsev.focusmanagement.model

import androidx.annotation.DrawableRes

data class Mountain(
    val id: Int,
    val name: String,
    @DrawableRes val image: Int
)
