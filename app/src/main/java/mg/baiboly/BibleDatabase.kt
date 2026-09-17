package mg.baiboly

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Verse::class], version = 1, exportSchema = false)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun dao(): BibleDao

    companion object {
        @Volatile private var instance: BibleDatabase? = null

        fun get(context: Context): BibleDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                BibleDatabase::class.java,
                "baiboly.db"
            ).createFromAsset("baiboly.db").build().also { instance = it }
        }
    }
}
