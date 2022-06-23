package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.not


/**
 * @RunWith(AndroidJUnit4::class)
 * A test runner is a JUnit component that runs tests. Without a test runner, your tests would not run.
 * There's a default test runner provided by JUnit that you get automatically. @RunWith swaps out that default test runner.
 * */

class TasksViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var tasksRepository: FakeTestRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel(){
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)
    }



    @Test
    fun addNewTask_setsNewTaskEvent(){
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(
            value?.getContentIfNotHandled(), (not(nullValue()))
        )
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))

    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)

        // Mark the task as complete task.
        tasksViewModel.completeTask(task, true)

        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}