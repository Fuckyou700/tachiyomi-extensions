package eu.kanade.tachiyomi.extension.zh.dmzj

import eu.kanade.tachiyomi.source.model.SManga
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Response
import uy.kohesive.injekt.injectLazy
import java.net.URLDecoder

const val PREFIX_ID_SEARCH = "id:"

val json: Json by injectLazy()

inline fun <reified T> Response.parseAs(): T {
    return json.decodeFromString(body!!.string())
}

fun getMangaUrl(id: String) = "/comic/comic_$id.json?version=2.7.019"

fun String.extractMangaId(): String {
    val start = 13 // length of "/comic/comic_"
    return substring(start, indexOf('.', start))
}

fun String.formatList() = replace("/", ", ")

fun parseStatus(status: String): Int = when (status) {
    "连载中" -> SManga.ONGOING
    "已完结" -> SManga.COMPLETED
    else -> SManga.UNKNOWN
}

fun String.formatChapterName(): String {
    val replaced = removePrefix("连载")
    if (!replaced[0].isDigit()) return replaced
    return when (replaced.last()) {
        '话', '卷' -> "第$replaced"
        else -> replaced
    }
}

fun String.toHttps() = "https:" + substringAfter(':')

// see https://github.com/tachiyomiorg/tachiyomi-extensions/issues/3457
fun String.fixFilename() = if (endsWith(".jp")) this + 'g' else this

fun String.decodePath(): String = URLDecoder.decode(this, "UTF-8")

const val COMMENTS_FLAG = "COMMENTS"
