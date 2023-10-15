package com.example.ul_todo_android_app.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ul_todo_android_app.database.dataAccessObject.UsersDAO
import com.example.ul_todo_android_app.database.entities.UsersEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDAOTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var usersDAO: UsersDAO

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDatabase::class.java
        ).allowMainThreadQueries().build()

        usersDAO = appDatabase.usersDAO()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testInsertValidUserAndGetUserByEmail() {
        // Arrange
        val testEmail = "abc@gmail.com"
        val testPassword = "12345"

        // Act
        val usersEntity = UsersEntity(userEmail = testEmail, password = testPassword)
        usersDAO.insertUser(usersEntity)
        val userRecord = usersDAO.getUserByEmail(testEmail)

        // Assert
        assertEquals("User email insertion failed", testEmail, userRecord?.userEmail)
        assertEquals("User password insertion failed", testPassword, userRecord?.password)
    }

    @Test
    fun testGetAllUsersWhenUsersAreInserted() {
        // Arrange
        val testUserDetails = mapOf("abc@gmail.com" to "12345", "xyz@gmail.com" to "12345")

        // Act
        val usersEntity = mutableListOf<UsersEntity>()
        for ((email, password) in testUserDetails) {
            usersEntity.add(UsersEntity(userEmail = email, password = password))
        }
        for (userEntity in usersEntity) {
            usersDAO.insertUser(userEntity)
        }

        // Assert
        val insertedUsers = usersDAO.getAllUsers()
        assertEquals("Failed to get all the user records", 2, insertedUsers.size)
    }

    @Test
    fun testGetAllUsersWhenNoUserIsInserted() {
        // Assert
        val allUsersWithoutRecords = usersDAO.getAllUsers()
        assertEquals("Failed to get all the user records", 0, allUsersWithoutRecords.size)
    }
}