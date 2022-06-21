package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsKtTest{
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero(){
        val task = listOf<Task>(Task("title","desec",isCompleted = false))

        val result = getActiveAndCompletedStats(task)
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompleteStats_ifTaskIsEmpty_retrunzeros(){

        val result = getActiveAndCompletedStats(emptyList())

         assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent,`is`(0f))
    }

    @Test
    fun getActiveAndCompleteStats_ifTaskIsNull_retrunzeros(){

        val result = getActiveAndCompletedStats(null)

        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent,`is`(0f))
    }
}