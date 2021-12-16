package com.example.mvvm_example.model

import android.content.Context
import androidx.room.*

@Entity(tableName = "comments")
class Comment(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val postId: Int,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val email: String,
    @ColumnInfo
    val body: String,
)

@Dao
interface CommentDao {
    @Insert
    fun insert(comment: Comment)
    @Query("Select * from comments where postId = :postId")
    fun query(postId: Int): Array<Comment>
}

@Database(entities = [Comment::class], version = 1, exportSchema = false)
abstract class CommentRoomDatabase: RoomDatabase() {
    abstract fun dao() : CommentDao

    companion object {
        @Volatile
        private var mInstance: CommentRoomDatabase? = null

        fun getDatabase(context: Context): CommentRoomDatabase {
            return mInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, CommentRoomDatabase::class.java, "comment_database").fallbackToDestructiveMigration().build()
                mInstance = instance
                return instance
            }
        }
    }
}