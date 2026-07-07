package ru.practicum.shoppinglist.core.data.preferences

import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import java.io.InputStream
import java.io.OutputStream

@Suppress("TooGenericExceptionCaught", "SwallowedException")
class EncryptedAuthSerializer(private val aead: Aead) : Serializer<AuthPreferences> {

    override val defaultValue: AuthPreferences = AuthPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthPreferences {
        return try {
            val encryptedBytes = input.readBytes()
            if (encryptedBytes.isEmpty()) return defaultValue

            val decryptedBytes = aead.decrypt(encryptedBytes, null)
            AuthPreferences.parseFrom(decryptedBytes)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: AuthPreferences, output: OutputStream) {
        val clearBytes = t.toByteArray()
        val encryptedBytes = aead.encrypt(clearBytes, null)
        output.write(encryptedBytes)
    }
}
