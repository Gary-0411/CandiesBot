package actions

import bean.BingImage
import bingKey
import com.google.gson.Gson
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection


object ImageAction : Action {
    override val noArg: Boolean = true

    override val prefix: String = "/小姐姐"

    override suspend fun invokeMessage(event: MessageEvent, params: String) {
        if (params.isBlank()) {
            event.reply("不加参数是坏文明！")
            return
        }
//        event.reply("接收到指令 小姐姐")

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .proxy(Proxy.NO_PROXY)
            .build()
        val request =
            Request.Builder()
                .apply {
                    url(
                        "https://api.cognitive.microsoft.com/bing/v7.0/images/search?q=${
                            URLEncoder.encode(
                                "美女",
                                "UTF-8"
                            )
                        }"
                    )
                        .addHeader("Ocp-Apim-Subscription-Key", bingKey!!)
                        .get()
                }.build()

        val url = URL(
            "https://api.cognitive.microsoft.com/bing/v7.0/images/search?q=${
                URLEncoder.encode(
                    "小姐姐",
                    "UTF-8"
                )
            }"
        );
        val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", bingKey)

        val stream: InputStream = connection.inputStream
        val response: String = Scanner(stream).useDelimiter("\\A").next()
        val fromJson = Gson().fromJson(response, BingImage::class.java)
        print(fromJson?.totalEstimatedMatches)

//        val get = fromJson?.value?.get(2)

        val size = fromJson.value?.size ?: 1
        print("size = $size")
        val get = fromJson?.value?.get(Random().nextInt(size - 1))


        val connection2: HttpURLConnection =
            URL(get?.contentUrl).openConnection() as HttpURLConnection
        val inputStream = connection2.inputStream

        event.sendImage(inputStream)
    }

    override suspend fun invoke(event: GroupMessageEvent, params: String) {
        if (params.isBlank()) {
            event.reply("不加参数是坏文明！")
            return
        }
//        event.reply("接收到指令 小姐姐")

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .proxy(Proxy.NO_PROXY)
            .build()
        val request =
            Request.Builder()
                .apply {
                    url(
                        "https://api.cognitive.microsoft.com/bing/v7.0/images/search?q=${
                            URLEncoder.encode(
                                "小姐姐",
                                "UTF-8"
                            )
                        }"
                    )
                        .addHeader("Ocp-Apim-Subscription-Key", bingKey!!)
                        .get()
                }.build()

        val url = URL(
            "https://api.cognitive.microsoft.com/bing/v7.0/images/search?q=${
                URLEncoder.encode(
                    "美女",
                    "UTF-8"
                )
            }"
        );
        val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", bingKey)

        val stream: InputStream = connection.inputStream
        val response: String = Scanner(stream).useDelimiter("\\A").next()
        val fromJson = Gson().fromJson(response, BingImage::class.java)
        print(fromJson?.totalEstimatedMatches)
        val size = fromJson.value?.size ?: 1
        print("size = $size")
        val get = fromJson?.value?.get(Random().nextInt(size - 1))

        val connection2: HttpURLConnection =
            URL(get?.contentUrl).openConnection() as HttpURLConnection
        val inputStream = connection2.inputStream

        event.sendImage(inputStream)

    }

    override fun helperText(): String {
        return "/小姐姐"
    }
}