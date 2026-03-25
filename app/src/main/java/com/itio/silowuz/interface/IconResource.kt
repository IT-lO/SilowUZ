package com.itio.silowuz.`interface`

import androidx.compose.ui.graphics.vector.ImageVector

sealed class IconResource{
    data class Vector(val imageVector: ImageVector) : IconResource()
    data class Drawable(val resId: Int) : IconResource()
}