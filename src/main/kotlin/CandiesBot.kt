import actions.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.*
import net.mamoe.mirai.join
import java.io.FileReader

var bingKey: String? = ""

suspend fun main() {
    val json = FileReader("config.json").use {
        Json.parseToJsonElement(it.readText()).jsonObject
    }

    val qq = json["qq"]?.jsonPrimitive?.long

    val password = json["password"]?.jsonPrimitive?.content

    bingKey = json["bingKey"]?.jsonPrimitive?.content

    checkNotNull(qq)
    checkNotNull(password)

    val bot = Bot( // JVM 下也可以不写 `QQAndroid.` 引用顶层函数
        qq,
        password
    ) {
        // 覆盖默认的配置
        fileBasedDeviceInfo("device.json") // 使用 "device.json" 保存设备信息
        // networkLoggerSupplier = { SilentLogger } // 禁用网络层输出
    }.alsoLogin()

    registerActions()

    bot.messageDSL()
    bot.join()//等到直到断开连接
}

/**
 * 在这里提供所有的action
 */
private val actions = arrayOf(
    HelpAction,
    PubAction,
    BingAction,
    MusicAction,
    ImageAction
)

/**
 * 注册方法
 */
private fun registerActions() {
    for (action in actions) {
        HelpAction.registerAction(action)
    }
}

/**
 * 使用 dsl 监听消息事件
 *
 * @see subscribeFriendMessages
 * @see subscribeMessages
 * @see subscribeGroupMessages
 * @see subscribeTempMessages
 *
 * @see MessageSubscribersBuilder
 */
fun Bot.messageDSL() {

    subscribeMessages {
        for (action in HelpAction.actions) {
            if (action.noArg)
                case(action.prefix, trim = true, onEvent = action::invokeMessage)
            else
                startsWith(action.prefix, trim = true, onEvent = action::invokeMessage)
        }
    }

    subscribeGroupMessages {
        for (action in HelpAction.actions) {
            if (action.noArg)
                case(action.prefix, trim = true, onEvent = action::invoke)
            else
                startsWith(action.prefix, trim = true, onEvent = action::invoke)
        }
    }
}