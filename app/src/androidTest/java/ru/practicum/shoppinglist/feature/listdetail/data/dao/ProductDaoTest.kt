package ru.practicum.shoppinglist.feature.listdetail.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.listdetail.data.entity.ProductEntity
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var listDao: ListDao
    private lateinit var productDao: ProductDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        listDao = database.listDao()
        productDao = database.productDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun deletingListCascadesToProducts() = runBlocking {
        val listId = listDao.upsert(ListEntity(name = "Тестовый список", userId = 1))

        productDao.upsert(
            ProductEntity(
                listId = listId,
                name = "Товар 1",
                quantity = 2.0,
                unit = "шт",
                isPurchased = false,
                position = 0
            )
        )

        productDao.upsert(
            ProductEntity(
                listId = listId,
                name = "Товар 2",
                quantity = 1.0,
                unit = "кг",
                isPurchased = false,
                position = 1
            )
        )

        // 3. Проверяем, что товары создались
        val productsBefore = productDao.observeByList(listId).first()
        TestCase.assertEquals(2, productsBefore.size)

        // 4. Удаляем список
        listDao.deleteById(listId)

        // 5. Проверяем — товаров нет (каскад сработал)
        val productsAfter = productDao.observeByList(listId).first()
        TestCase.assertTrue(productsAfter.isEmpty())
    }
}
