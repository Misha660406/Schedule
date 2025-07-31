package com.example.schedule.libs.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BackstackNavigator {

    object EmptyScreen : Screen {

        @Composable
        override fun Render() {
            Text(text = "Сегодня солнечная погода, жарко очень, но терпимо")
        }
    }

    private val backstack = mutableListOf<Screen>()

    private val _currentScreen = MutableStateFlow<Screen>(EmptyScreen)
    val currentScreen: StateFlow<Screen> = _currentScreen

    fun open(screen: Screen) {
        backstack.add(screen)
        _currentScreen.value = screen
    }

    fun replace(screen: Screen) {
        backstack.removeLastOrNull()
        backstack.add(screen)
        _currentScreen.value = screen
    }

    fun pop() {
        backstack.removeLastOrNull()
        _currentScreen.value = backstack.lastOrNull() ?: EmptyScreen
    }

    fun popToRoot() {
        val root = backstack.firstOrNull()
        if (root == null) {
            _currentScreen.value = EmptyScreen
        } else {
            backstack.clear()
            open(root)
        }
    }

    fun sendResult(result: Any) {
        backstack[backstack.lastIndex - 1].handleResult(result)
    }
}