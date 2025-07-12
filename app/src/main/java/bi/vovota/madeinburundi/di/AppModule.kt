package bi.vovota.madeinburundi.di

import android.content.Context
import bi.vovota.madeinburundi.data.AuthRepository
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.repository.OrderRepository
import bi.vovota.madeinburundi.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
  fun provideHttpClient(): HttpClient {
    val client = HttpClient(CIO) {
      install(ContentNegotiation) {
        json(Json {
          ignoreUnknownKeys = true
          isLenient = true
          prettyPrint = true
        })
      }
    }
    println("Provided: ${client.hashCode()}")
    return client
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
  fun provideUserRepository(
    client: HttpClient,
    @ApplicationContext context: Context,
    tokenManager: TokenManager
  ): UserRepository {
    return UserRepository(client, context, tokenManager)
  }

  @Provides
  @Singleton
  fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
    TokenManager.init(context)
    return TokenManager
  }

}
