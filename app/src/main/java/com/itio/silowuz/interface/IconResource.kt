package com.itio.silowuz.`interface`

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing different types of icon resources used throughout the app.
 * Provides a unified way to handle both vector drawable icons (Material Icons) 
 * and bitmap/drawable icons (resources from XML).
 */
sealed class IconResource{
    /**
     * Represents an icon provided as a Material Design ImageVector.
     * Used for Material Icons like arrow, checkmark, add, etc.
     * 
     * @param imageVector The Material Design vector icon to display
     */
    data class Vector(val imageVector: ImageVector) : IconResource()

    /**
     * Represents an icon provided as a drawable resource ID.
     * Used for custom bitmap or vector drawables defined in res/drawable/.
     * 
     * @param resId The integer resource ID pointing to the drawable file
     */
    data class Drawable(val resId: Int) : IconResource()
}
