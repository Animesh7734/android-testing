package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Root
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase
    private lateinit var localDataSource: TasksLocalDataSource


    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(),ToDoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        localDataSource = TasksLocalDataSource(database.taskDao(),Dispatchers.Main)

    }

    @After
    fun closeDb(){database.close()}

    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN  - Task retrieved by ID.
        val result = localDataSource.getTask(newTask.id)

        // THEN - Same task is returned.
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.description, `is`("description"))
        assertThat(result.data.isCompleted, `is`(false))
    }

    @Test
    fun completeTask_retrievedTaskIsComplete()= runBlocking{
        // 1. Save a new active task in the local data source.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)
        // 2. Mark it as complete.
            localDataSource.completeTask(newTask.id)
        // 3. Check that the task can be retrieved from the local data source and is complete.
        val result = localDataSource.getTask(newTask.id)
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.isCompleted, `is`(true))

    }

}