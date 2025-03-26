package com.example.pokedexapp682474.components

import androidx.compose.animation.*
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = hostState.currentSnackbarData != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        SnackbarHost(
            hostState = hostState,
            modifier = modifier,
            snackbar = { data ->
                Snackbar(data)
            }
        )
    }
}