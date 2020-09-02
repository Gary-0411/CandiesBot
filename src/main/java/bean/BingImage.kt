package bean

/**
 * @author Created by sdx on 2020/9/2.
 */
data class BingImage(
    var webSearchUrl: String? = "",
    var totalEstimatedMatches: String? = "",
    var value: List<BingImageValue>? = null
)

data class BingImageValue(
    var contentUrl: String? = null
)