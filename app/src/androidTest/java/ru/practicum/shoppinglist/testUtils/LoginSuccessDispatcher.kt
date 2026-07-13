package ru.practicum.shoppinglist.testUtils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class LoginSuccessDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/auth/login" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody("""{"user_id": 1, "access_token": "access_token", "refresh_token": "refresh_token"}""")
                    .addHeader("Content-Type", "application/json")
            }

            else -> MockResponse().setResponseCode(404)
        }
    }
}