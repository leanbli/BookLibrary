//package di
//import dagger.Module
// dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import data.local.BookDao
//import data.repository.BookRepositoryImpl
//import domain.BookRepository
//import javax.inject.Singleton
//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//    @Provides
//    @Singleton
//    fun provideBookRepository(bookDao: BookDao): BookRepository {
//        return BookRepositoryImpl(bookDao) } }