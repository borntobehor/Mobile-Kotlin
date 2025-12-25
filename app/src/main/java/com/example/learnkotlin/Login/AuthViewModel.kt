package com.example.learnkotlin.Login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.Store.DataStoreManger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreManger(application.applicationContext)
    private val repo = AuthRepository()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val token = dataStore.authToken.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val expiryEpochSeconds = dataStore.authExpiryEpochSeconds.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isLoggedIn: StateFlow<Boolean> = combine(token, expiryEpochSeconds) { token, exp ->
        if (token.isNullOrBlank()) return@combine false
        val now = System.currentTimeMillis() / 1000
        return@combine (exp == null) || (exp > now)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private var expiryJob: Job? = null

    init {
        viewModelScope.launch {
            combine(token, expiryEpochSeconds) { t, e -> t to e }.collect { (t, e) ->
                expiryJob?.cancel()
                if (!t.isNullOrBlank() && e != null) {
                    val now = System.currentTimeMillis() / 1000
                    val delaySeconds = (e - now).coerceAtLeast(0)
                    expiryJob = viewModelScope.launch {
                        delay(delaySeconds * 1000)
                        logout()
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        _error.value = null
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = repo.login(email, password)
                dataStore.saveAuth(result.token, result.expiryEpochSeconds)
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        _error.value = null
        _loading.value = true
        viewModelScope.launch {
            try {
                repo.register(name, email, password)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message ?: "Register failed"
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch { dataStore.clearAuth() }
    }
}