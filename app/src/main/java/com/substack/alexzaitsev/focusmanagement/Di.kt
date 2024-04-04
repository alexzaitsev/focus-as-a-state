package com.substack.alexzaitsev.focusmanagement

import com.substack.alexzaitsev.focusmanagement.model.produceRanges
import com.substack.alexzaitsev.focusmanagement.ui.NavController
import com.substack.alexzaitsev.focusmanagement.ui.screen.mountain.MountainViewModel
import com.substack.alexzaitsev.focusmanagement.ui.screen.ranges.RangesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::NavController)
    viewModelOf(::RangesViewModel)
    viewModelOf(::MountainViewModel)
    factory { produceRanges(androidContext().resources, androidContext().packageName) }
}
