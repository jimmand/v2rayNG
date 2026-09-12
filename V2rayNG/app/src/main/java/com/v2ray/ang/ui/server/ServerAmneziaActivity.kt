package com.v2ray.ang.ui.server

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.v2ray.ang.R
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.ui.compose.FormTextField

class ServerAmneziaActivity : BaseServerActivity() {

    override val serverConfigType: EConfigType = EConfigType.AMNEZIA

    @Composable
    override fun ScreenContent() {
        val scope = rememberCoroutineScope()
        val uiState = rememberSaveable(saver = ServerUiState.Saver) {
            ServerUiState.from(
                initialConfig = initialConfig
            )
        }.apply {
            configType = EConfigType.AMNEZIA
        }

        ServerEditorScaffold(
            title = serverConfigType.toString(),
            onSaveClick = { saveServer(uiState) }
        ) {
            CommonBasicFields(uiState)
            AmneziaProtocolFields(uiState)

        }
    }

    @Composable
    private fun AmneziaProtocolFields(state: ServerUiState) {
        FormTextField(
            stringResource(R.string.server_lab_secret_key),
            state.secretKey,
            { state.secretKey = it }
        )
        FormTextField(
            stringResource(R.string.server_lab_public_key),
            state.publicKey,
            { state.publicKey = it }
        )
        FormTextField(
            stringResource(R.string.server_lab_preshared_key),
            state.preSharedKey,
            { state.preSharedKey = it }
        )
        FormTextField(
            stringResource(R.string.server_lab_reserved),
            state.reserved,
            { state.reserved = it }
        )
        FormTextField(
            stringResource(R.string.server_lab_local_address),
            state.localAddress,
            { state.localAddress = it }
        )
        FormTextField(
            stringResource(R.string.server_lab_local_mtu),
            state.mtu,
            { state.mtu = it },
            keyboardType = KeyboardType.Number
        )

        FormTextField(
            stringResource(R.string.server_lab_final_mask),
            state.finalMask,
            { state.finalMask = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_jc),
            state.jc,
            { state.jc = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_jmin),
            state.jMin,
            { state.jMin = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_jmax),
            state.jMax,
            { state.jMax = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_s1),
            state.s1,
            { state.s1 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_s2),
            state.s2,
            { state.s2 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_s3),
            state.s3,
            { state.s3 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_s4),
            state.s4,
            { state.s4 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_h1),
            state.h1,
            { state.h1 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_h2),
            state.h2,
            { state.h2 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_h3),
            state.h3,
            { state.h3 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_h4),
            state.h4,
            { state.h4 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_i1),
            state.i1,
            { state.i1 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_i2),
            state.i2,
            { state.i2 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_i3),
            state.i3,
            { state.i3 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_i4),
            state.i4,
            { state.i4 = it }
        )

        FormTextField(
            stringResource(R.string.server_lab_local_i5),
            state.i5,
            { state.i5 = it }
        )
    }
}

