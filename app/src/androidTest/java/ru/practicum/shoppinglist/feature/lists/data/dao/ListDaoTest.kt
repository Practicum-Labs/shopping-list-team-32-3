package ru.practicum.shoppinglist.feature.lists.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.listdetail.data.dao.ProductDao
import ru.practicum.shoppinglist.feature.listdetail.data.entity.ProductEntity
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity
import kotlin.collections.emptyList

@RunWith(AndroidJUnit4::class)
class ListDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var listDao: ListDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        listDao = database.listDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun user1CannotSeeUser2Lists() = runTest {
        //Given
        listDao.upsert(TEST_OBJECT_0.lists[0])
        listDao.upsert(TEST_OBJECT_1.lists[0])

        //When
        val result = listDao.observeAll(TEST_OBJECT_0.userId).first().map { it.name }

        //Then
        assertEquals(
            "Пользователь 0 должен видеть только свои списки",
            listOf(TEST_OBJECT_0.lists[0].name), result
        )
    }

    @Test
    fun deleteAllDeletesOnlyForUser1() = runTest {
        //Given
        listDao.upsert(TEST_OBJECT_0.lists[0])
        listDao.upsert(TEST_OBJECT_1.lists[0])

        //When
        listDao.deleteAll(TEST_OBJECT_0.userId)
        val result1 = listDao.observeAll(TEST_OBJECT_0.userId).first().map { it.name }
        val result2 = listDao.observeAll(TEST_OBJECT_1.userId).first().map { it.name }

        //Then
        assertEquals(
            "Списки пользователя 0 должны быть удаллены",
            emptyList<String>(), result1
        )

        assertEquals(
            "Списки пользователя 1 должны быть сохранены",
            listOf(TEST_OBJECT_1.lists[0].name), result2
        )
    }

    companion object {
        data class ListDaoTestObject(
            val userId: Long,
            val lists: List<ListEntity>
        )
        val TEST_OBJECT_0 = ListDaoTestObject(
            userId = 0,
            lists = listOf(
                ListEntity(name = "Shopping", userId = 0),
                ListEntity(name = "Meal", userId = 0),
            )
        )
        val TEST_OBJECT_1 = ListDaoTestObject(
            userId = 1,
            lists = listOf(
                ListEntity(name = "Cheese", userId = 1),
                ListEntity(name = "Wines", userId = 1),
            )
        )

    }
}