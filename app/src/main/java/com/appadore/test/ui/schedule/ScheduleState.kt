package com.appadore.test.ui.schedule

sealed class ScheduleState {
    data class ShowMessage(val message: String): ScheduleState()
    data class OnChallengeTickStarted(val countdown: Int): ScheduleState()
    data object OnTimeScheduled: ScheduleState()
}