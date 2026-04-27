//package di
//import android.content.Context
//import androidx.room.Room
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import data.local.BookDao
//import data.local.BookDatabase
//import javax.inject.Singleton
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase {
//        return BookDatabase.getInstance(context)
//    }

 //   @Provides
//    fun provideBookDao(database: BookDatabase): BookDao {
//        return database.bookDao()
//    }
//}