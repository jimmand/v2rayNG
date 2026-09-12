package com.v2ray.ang.fmt

import com.v2ray.ang.AppConfig
import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.extension.idnHost
import com.v2ray.ang.extension.nullIfBlank
import com.v2ray.ang.extension.removeWhiteSpace
import com.v2ray.ang.util.Utils
import java.net.URI

object AmneziaFmt : FmtBase() {
    /**
     * Parses a URI string into a ProfileItem object.
     *
     * @param str the URI string to parse
     * @return the parsed ProfileItem object, or null if parsing fails
     */
    fun parse(str: String): ProfileItem? {
        val config = ProfileItem.create(EConfigType.AMNEZIA)

        val uri = URI(Utils.fixIllegalUrl(str))
        if (uri.rawQuery.isNullOrEmpty()) return null
        val queryParam = getQueryParam(uri)

        config.remarks = Utils.decodeURIComponent(uri.fragment.orEmpty()).let { it.ifEmpty { "none" } }
        config.server = uri.idnHost
        config.serverPort = uri.port.toString()

        config.secretKey = uri.userInfo.orEmpty()
        config.localAddress = queryParam["address"] ?: AppConfig.WIREGUARD_LOCAL_ADDRESS_V4
        config.publicKey = queryParam["publickey"].orEmpty()
        config.preSharedKey = queryParam["presharedkey"]?.nullIfBlank()
        config.mtu = Utils.parseInt(queryParam["mtu"] ?: AppConfig.WIREGUARD_LOCAL_MTU)
        config.reserved = queryParam["reserved"] ?: "0,0,0"
        config.jc = Utils.parseInt(queryParam["jc"].orEmpty())
        config.jMin = Utils.parseInt(queryParam["jmin"].orEmpty())
        config.jMax = Utils.parseInt(queryParam["jmax"].orEmpty())
        config.s1 = Utils.parseInt(queryParam["s1"].orEmpty())
        config.s2 = Utils.parseInt(queryParam["s2"].orEmpty())
        config.s3 = Utils.parseInt(queryParam["s3"].orEmpty())
        config.s4 = Utils.parseInt(queryParam["s4"].orEmpty())
        config.h1 = queryParam["h1"].orEmpty()
        config.h2 = queryParam["h2"].orEmpty()
        config.h3 = queryParam["h3"].orEmpty()
        config.h4 = queryParam["h4"].orEmpty()
        config.i1 = queryParam["i1"].orEmpty()
        config.i2 = queryParam["i2"].orEmpty()
        config.i3 = queryParam["i3"].orEmpty()
        config.i4 = queryParam["i4"].orEmpty()
        config.i5 = queryParam["i5"].orEmpty()

        return config
    }

    /**
     * Parses an Amnezia configuration file string into a ProfileItem object.
     *
     * @param str the Amnezia configuration file string to parse
     * @return the parsed ProfileItem object, or null if parsing fails
     */
    fun parseAmneziaConfFile(str: String): ProfileItem {
        val config = ProfileItem.create(EConfigType.AMNEZIA)

        val interfaceParams: MutableMap<String, String> = mutableMapOf()
        val peerParams: MutableMap<String, String> = mutableMapOf()

        var currentSection: String? = null

        str.lines().forEach { line ->
            val trimmedLine = line.trim()

            if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                return@forEach
            }

            when {
                trimmedLine.startsWith("[Interface]", ignoreCase = true) -> currentSection = "Interface"
                trimmedLine.startsWith("[Peer]", ignoreCase = true) -> currentSection = "Peer"
                else -> {
                    if (currentSection != null) {
                        val parts = trimmedLine.split("=", limit = 2).map { it.trim() }
                        if (parts.size == 2) {
                            val key = parts[0].lowercase()
                            val value = parts[1]
                            when (currentSection) {
                                "Interface" -> interfaceParams[key] = value
                                "Peer" -> peerParams[key] = value
                            }
                        }
                    }
                }
            }
        }

        config.secretKey = interfaceParams["privatekey"].orEmpty()
        config.remarks = System.currentTimeMillis().toString()
        config.localAddress = interfaceParams["address"] ?: AppConfig.WIREGUARD_LOCAL_ADDRESS_V4
        config.mtu = Utils.parseInt(interfaceParams["mtu"] ?: AppConfig.WIREGUARD_LOCAL_MTU)
        config.jc = Utils.parseInt(interfaceParams["jc"].orEmpty())
        config.jMin = Utils.parseInt(interfaceParams["jmin"].orEmpty())
        config.jMax = Utils.parseInt(interfaceParams["jmax"].orEmpty())
        config.s1 = Utils.parseInt(interfaceParams["s1"].orEmpty())
        config.s2 = Utils.parseInt(interfaceParams["s2"].orEmpty())
        config.s3 = Utils.parseInt(interfaceParams["s3"].orEmpty())
        config.s4 = Utils.parseInt(interfaceParams["s4"].orEmpty())
        config.h1 = interfaceParams["h1"].orEmpty()
        config.h2 = interfaceParams["h2"].orEmpty()
        config.h3 = interfaceParams["h3"].orEmpty()
        config.h4 = interfaceParams["h4"].orEmpty()
        config.i1 = interfaceParams["i1"].orEmpty()
        config.i2 = interfaceParams["i2"].orEmpty()
        config.i3 = interfaceParams["i3"].orEmpty()
        config.i4 = interfaceParams["i4"].orEmpty()
        config.i5 = interfaceParams["i5"].orEmpty()
        config.publicKey = peerParams["publickey"].orEmpty()
        config.preSharedKey = peerParams["presharedkey"]?.nullIfBlank()
        val endpoint = peerParams["endpoint"].orEmpty()
        val endpointParts = endpoint.split(":", limit = 2)
        if (endpointParts.size == 2) {
            config.server = endpointParts[0]
            config.serverPort = endpointParts[1]
        } else {
            config.server = endpoint
            config.serverPort = ""
        }
        config.reserved = peerParams["reserved"] ?: "0,0,0"

        return config
    }


    /**
     * Converts a ProfileItem object to a URI string.
     *
     * @param config the ProfileItem object to convert
     * @return the converted URI string
     */
    fun toUri(config: ProfileItem): String {
        val dicQuery = HashMap<String, String>()

        dicQuery["publickey"] = config.publicKey.orEmpty()
        if (config.reserved != null) {
            dicQuery["reserved"] = config.reserved.removeWhiteSpace().orEmpty()
        }
        dicQuery["address"] = config.localAddress.removeWhiteSpace().orEmpty()
        if (config.mtu != null) {
            dicQuery["mtu"] = config.mtu.toString()
        }
        if (config.preSharedKey != null) {
            dicQuery["presharedkey"] = config.preSharedKey.removeWhiteSpace().orEmpty()
        }
        if (config.jc != null) {
            dicQuery["jc"] = config.jc.toString()
        }
        if (config.jMin != null) {
            dicQuery["jmin"] = config.jMin.toString()
        }
        if (config.jMax != null) {
            dicQuery["jmax"] = config.jMax.toString()
        }
        if (config.s1 != null) {
            dicQuery["s1"] = config.s1.toString()
        }
        if (config.s2 != null) {
            dicQuery["s2"] = config.s2.toString()
        }
        if (config.s3 != null) {
            dicQuery["s3"] = config.s3.toString()
        }
        if (config.s4 != null) {
            dicQuery["s4"] = config.s4.toString()
        }
        if (config.h1 != null) {
            dicQuery["h1"] = config.h1.removeWhiteSpace().orEmpty()
        }
        if (config.h2 != null) {
            dicQuery["h2"] = config.h2.removeWhiteSpace().orEmpty()
        }
        if (config.h3 != null) {
            dicQuery["h3"] = config.h3.removeWhiteSpace().orEmpty()
        }
        if (config.h4 != null) {
            dicQuery["h4"] = config.h4.removeWhiteSpace().orEmpty()
        }
        if (config.i1 != null) {
            dicQuery["i1"] = config.i1.removeWhiteSpace().orEmpty()
        }
        if (config.i2 != null) {
            dicQuery["i2"] = config.i2.removeWhiteSpace().orEmpty()
        }
        if (config.i3 != null) {
            dicQuery["i3"] = config.i3.removeWhiteSpace().orEmpty()
        }
        if (config.i4 != null) {
            dicQuery["i4"] = config.i4.removeWhiteSpace().orEmpty()
        }
        if (config.i5 != null) {
            dicQuery["i5"] = config.i5.removeWhiteSpace().orEmpty()
        }
        return toUri(config, config.secretKey, dicQuery)
    }
}
