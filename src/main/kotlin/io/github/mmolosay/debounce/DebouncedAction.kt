package io.github.mmolosay.debounce

interface DebouncedAction : () -> Unit {
    val isReady: Boolean
}