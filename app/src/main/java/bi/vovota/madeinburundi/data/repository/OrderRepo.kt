package bi.vovota.madeinburundi.data.repository

import bi.vovota.madeinburundi.data.model.NewOrder
import bi.vovota.madeinburundi.data.model.Order
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.remote.ApiService
import bi.vovota.madeinburundi.utils.safeApiCall
import javax.inject.Inject


interface OrderRepo {
  suspend fun getOrders(): Result<List<Order>>
  suspend fun placeOrder(order: NewOrder): Result<Order>
}
class OrderRepoImpl @Inject constructor(
  private val api: ApiService,
  private val tokenManager: TokenManager
): OrderRepo {
  override suspend fun getOrders(): Result<List<Order>> {
    val token = tokenManager.getAccessToken()
    if (token != null) {
      return safeApiCall { api.getOrders(token) }
    } else {
      return Result.failure(Exception("No token found"))
    }
  }

  override suspend fun placeOrder(order: NewOrder): Result<Order> {
    TODO("Not yet implemented")
  }
}