package ru.practicum.shoppinglist.core.data.network
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
class StatusCodeException(val code: Int, message: String?) : IOException(message)

class SuccessCheckInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())

        when (response.isSuccessful) {
            true -> return response
            else -> throw StatusCodeException(response.code, response.body?.string())
        }
    }
}
