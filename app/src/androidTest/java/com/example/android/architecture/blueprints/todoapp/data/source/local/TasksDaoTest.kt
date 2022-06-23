package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

   private lateinit var database: ToDoDatabase

   @Before
   fun initDb(){
       database = Room.inMemoryDatabaseBuilder(getApplicationContext(),ToDoDatabase::class.java)
           .allowMainThreadQueries()
           .build()
   }
    @After
    fun closeDb(){database.close()}

    @Test
    fun insertTaskAndGetById() = runBlocking {
        val task = Task("title","description")
        database.taskDao().insertTask(task)

        val res = database.taskDao().getTaskById(task.id)

        assertThat<Task>(res as Task ,notNullValue())
        assertThat(res.id, `is`(task.id))
        assertThat(res.title, `is`(task.title))
        assertThat(res.description, `is`(task.description))
        assertThat(res.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlocking {
        // 1. Insert a task into the DAO.
        val task1 = Task("title1","description1")
        database.taskDao().insertTask(task1)
        // 2. Update the task by creating a new task with the same ID but different attributes.
        val task = Task("title2","description2",true,task1.id)
        database.taskDao().updateTask(task)

        // 3. Check that when you get the task by its ID, it has the updated values.
        val res =database.taskDao().getTaskById(task1.id)
        assertThat<Task>(res as Task ,notNullValue())
        assertThat(res.id, `is`(task.id))
        assertThat(res.title, `is`(task.title))
        assertThat(res.description, `is`(task.description))
        assertThat(res.isCompleted, `is`(task.isCompleted))

    }
}