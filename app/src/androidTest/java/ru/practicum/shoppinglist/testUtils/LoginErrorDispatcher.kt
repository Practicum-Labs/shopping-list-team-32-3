package ru.practicum.shoppinglist.testUtils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class LoginErrorDispatcher(val error: String) : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/auth/login" -> {
                MockResponse()
                    .setResponseCode(404)
                    .setBody(error)
                    .addHeader("Content-Type", "application/json")
            }

            else -> MockResponse().setResponseCode(404)
        }
    }
}