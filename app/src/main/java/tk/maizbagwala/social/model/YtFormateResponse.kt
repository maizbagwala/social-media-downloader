package tk.maizbagwala.social.model

data class YtFormateResponse(
    val error: Boolean,
    val formats: Formats,
    val status: String
) {
    data class Formats(
        val audio: List<Audio>,
        val basename: String,
        val duration: Int,
        val id: String,
        val thumbnail: String,
        val title: String,
        val video: List<Video>
    ) {
        data class Audio(
            val description: Description,
            val fileSize: Int,
            val fileType: String,
            val formatId: String,
            val needConvert: Boolean,
            val quality: String,
            val url: String
        ) {
            data class Description(
                val block: Boolean,
                val fragment: String
            )
        }

        data class Video(
            val description: Description,
            val fileSize: Int,
            val fileType: String,
            val filename: String,
            val formatId: String,
            val needConvert: Boolean,
            val quality: String,
            val url: String
        ) {
            data class Description(
                val block: Boolean,
                val fragment: String
            )
        }
    }
}