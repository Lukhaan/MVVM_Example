package com.example.mvvm_example.model

import android.content.Context
import androidx.room.*
import com.example.mvvm_example.util.ServerManager.Companion.instance
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val userId: Int,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val body: String,
)

@Dao
interface PostDao {
    @Insert
    suspend fun insert(post: Post)

    @Query("Select * from posts")
    suspend fun query(): Array<Post>
}

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostRoomDatabase: RoomDatabase() {
    abstract fun dao(): PostDao

    companion object {
        @Volatile
        private var mInstance: PostRoomDatabase? = null

        fun getDatabase(context: Context): PostRoomDatabase {
            return mInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, PostRoomDatabase::class.java, "post_database").fallbackToDestructiveMigration().build()
                mInstance = instance
                return instance
            }
        }
    }
}