package com.curdfirestore.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun <T> NavGraphBuilder.composableWithArgument(
    route: String,
    argument: T,
    content: @Composable (T) -> Unit
) {
    composable(route) {
        content(argument)
    }
}
