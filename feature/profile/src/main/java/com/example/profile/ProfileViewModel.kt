package com.example.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.data.repository.UserRepository
import com.example.model.Transaction
import com.example.network.model.TransactionsResponse
import com.example.workmanager.PollingWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.example.workmanager.PollingForegroundService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _transactions = MutableStateFlow<Result<TransactionsResponse>>(Result.success(TransactionsResponse(false, emptyList())))
    val transactions: StateFlow<Result<TransactionsResponse>> = _transactions

    init {
        val pollingWork = PeriodicWorkRequestBuilder<PollingWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(pollingWork)
        val startIntent = Intent(applicationContext, PollingForegroundService::class.java)
        applicationContext.startForegroundService(startIntent)
        fetchUserTransactions()
    }

    private fun fetchUserTransactions() {
        viewModelScope.launch {
            // Assume getUserTransactions() returns a Result<TransactionsResponse>
            val result = userRepository.getUserTransactions()
            if (result != null) {
                _transactions.value = result
                Log.d("ProfileViewModel" ,"${result.getOrNull()?.transactions}")
            }
        }
    }
}