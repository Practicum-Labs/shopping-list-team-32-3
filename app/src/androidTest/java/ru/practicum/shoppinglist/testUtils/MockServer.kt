package ru.practicum.shoppinglist.testUtils

import androidx.compose.ui.test.IdlingResource
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import ru.practicum.shoppinglist.core.di.CoreDiKeys
import okhttp3.mockwebserver.Dispatcher

class MockServer: KoinTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var idlingResource: IdlingResource

    fun setUp(forRule: ComposeContentTestRule ) {
        mockWebServer = MockWebServer().apply {
            useHttps(serverCertificates.sslSocketFactory(), false)
            dispatcher = LoginSuccessDispatcher()
            start(8080)
        }

        loadKoinModules(testNetworkModule)

        okHttpClient = get()
        idlingResource = OkHttpComposeIdlingResource(okHttpClient)
        forRule.registerIdlingResource(idlingResource)
    }

    fun setDispatcher(dispatcher: Dispatcher) {
        mockWebServer.dispatcher = dispatcher
    }

    fun tearDown(forRule: ComposeContentTestRule ) {
        mockWebServer.shutdown()
        forRule.unregisterIdlingResource(idlingResource)
    }

    companion object {
        val localhostCertificate = HeldCertificate.Builder()
            .addSubjectAlternativeName("localhost")
            .build()

        val serverCertificates = HandshakeCertificates.Builder()
            .heldCertificate(localhostCertificate)
            .build()

        val clientCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(localhostCertificate.certificate)
            .build()

        private val testNetworkModule = module {

            single(createdAtStart = true, qualifier = named(CoreDiKeys.BASE_URL_KEY)) {
                "https://localhost:8080/"
            }

            single {
                get<OkHttpClient.Builder>()
                    .sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager)
                    .build()
            }
        }

    }
}

class OkHttpComposeIdlingResource(
    private val client: OkHttpClient
) : IdlingResource {

    override val isIdleNow: Boolean
        get() = client.dispatcher.runningCallsCount() == 0
}