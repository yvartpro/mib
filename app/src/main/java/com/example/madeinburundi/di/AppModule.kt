package com.example.madeinburundi.di

import com.example.madeinburundi.data.AuthRepository
import com.example.madeinburundi.data.repository.OrderRepository
import com.example.madeinburundi.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideHttpClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
      })
    }
  }

  @Provides
  @Singleton
  fun provideAuthRepository(client: HttpClient): AuthRepository {
    return AuthRepository(client)
  }

  @Provides
  @Singleton
  fun provideOrderRepository(client: HttpClient): OrderRepository =
    OrderRepository(client)

  @Provides
  @Singleton
  fun provideUserRepository(client: HttpClient): UserRepository {
    return UserRepository(client)
  }

}
