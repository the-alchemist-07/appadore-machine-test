package com.appadore.test.ui.schedule

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appadore.test.core.common.Constants.ONE_SECOND
import com.appadore.test.core.managers.DataStoreManager
import com.appadore.test.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private lateinit var timer: Timer
    var hour: Int? = null
    var minute: Int? = null
    var second: Int? = null

    private val _state = Channel<ScheduleState>()
    val state = _state.receiveAsFlow()

    fun validateTime() {
        if (hour == null || minute == null || second == null)
            viewModelScope.launch {
                _state.send(ScheduleState.ShowMessage("Enter a valid time"))
            }
        else saveTimeInDatastore()
    }

    private fun saveTimeInDatastore() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.HOUR_OF_DAY, hour!!)
        calendar.set(Calendar.MINUTE, minute!!)
        calendar.set(Calendar.SECOND, second!!)

        if (calendar.time.time < System.currentTimeMillis()) {
            viewModelScope.launch {
                _state.send(ScheduleState.ShowMessage("Past time is not allowed"))
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            questionRepository.saveScheduledTime(calendar.time.time)

            withContext(Dispatchers.Main) {
                _state.send(ScheduleState.OnTimeScheduled)
            }
        }

        startTimer(calendar.time.time)
    }

    private fun startTimer(scheduledMillis: Long) {
        timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                val newValue = (scheduledMillis - System.currentTimeMillis()) / 1000
                if (newValue.toInt() <= 20) {
                    viewModelScope.launch {
                        _state.send(ScheduleState.OnChallengeTickStarted(newValue.toInt()))
                    }
                }
                if (newValue.toInt() == 0)
                    timer.cancel()
            }
        }, ONE_SECOND, ONE_SECOND)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}
