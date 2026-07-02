package ru.practicum.shoppinglist.core.data.preferences

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager

object CryptoManager {
    init {
        AeadConfig.register()
    }

    fun getAead(context: Context, keysetName: String, keysetFile: String, masterkeyUri: String): Aead {
        return AndroidKeysetManager.Builder()
            .withSharedPref(context, keysetName, keysetFile)
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri(masterkeyUri)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }
}
