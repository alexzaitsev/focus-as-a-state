package com.substack.alexzaitsev.focusmanagement.model

import android.annotation.SuppressLint
import android.content.res.Resources

private const val MIN_MOUNTAINS_PER_RANGE = 5
private const val MAX_MOUNTAINS_PER_RANGE = 20

fun produceRanges(res: Resources, packageName: String) = listOf(
    "Rocky Mountains",
    "Carpathian Mountains",
    "Alps",
    "Himalayas",
    "Andes",
    "Atlas Mountains",
    "Great Dividing Range",
    "Appalachian Mountains",
    "Kunlun Mountains"
).mapIndexed { i, range ->
    MountainRange(
        id = i,
        name = range,
        mountains = (1..getRandomMountainsNumber()).map { j ->
            produceMountain(res = res, packageName = packageName, id = j)
        }
    )
}

private fun produceMountain(res: Resources, packageName: String, id: Int) = Mountain(
    id = id,
    name = "Mountain $id",
    image = res.getDrawableRes(packageName, getRandomPictureNumber())
)

private fun getRandomMountainsNumber() = (MIN_MOUNTAINS_PER_RANGE..MAX_MOUNTAINS_PER_RANGE).random()

private fun getRandomPictureNumber() = (1..MAX_MOUNTAINS_PER_RANGE).random()

@SuppressLint("DiscouragedApi")
private fun Resources.getDrawableRes(packageName: String, imageNo: Int) =
    getIdentifier("image${imageNo.getFullImageNo()}", "drawable", packageName)

private fun Int.getFullImageNo() = if (this < 10) "0$this" else "$this"
