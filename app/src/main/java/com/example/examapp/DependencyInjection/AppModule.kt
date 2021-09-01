package com.example.examapp.DependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.examapp.Adapter.ExamsAdapter
import com.example.examapp.Repo.Repository
import com.example.examapp.Repo.RepositoryInterface
import com.example.examapp.Room.Dao
import com.example.examapp.Room.ExamDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ExamDatabase::class.java,
        "exam_database"
    ).build()

    @Singleton
    @Provides
    fun getDao(examDatabase: ExamDatabase) = examDatabase.dao()

    @Singleton
    @Provides
    fun getRepository(dao: Dao): RepositoryInterface = Repository(dao)

    @Singleton
    @Provides
    fun getExamsAdapter(@ApplicationContext context: Context) = ExamsAdapter(context)

}