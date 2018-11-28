package ua.com.bpst.flikrtest.model
import com.google.gson.annotations.SerializedName


data class ImagesResult(
    @SerializedName("photos")
    var photos: Photos = Photos(),
    @SerializedName("stat")
    var stat: String = ""
) {
    data class Photos(
        @SerializedName("page")
        var page: Int = 0,
        @SerializedName("pages")
        var pages: String = "",
        @SerializedName("perpage")
        var perpage: Int = 0,
        @SerializedName("photo")
        var photo: List<Photo> = listOf(),
        @SerializedName("total")
        var total: String = ""
    ) {
        data class Photo(
            @SerializedName("farm")
            var farm: Int = 0,
            @SerializedName("id")
            var id: String = "",
            @SerializedName("isfamily")
            var isfamily: Int = 0,
            @SerializedName("isfriend")
            var isfriend: Int = 0,
            @SerializedName("ispublic")
            var ispublic: Int = 0,
            @SerializedName("owner")
            var owner: String = "",
            @SerializedName("secret")
            var secret: String = "",
            @SerializedName("server")
            var server: String = "",
            @SerializedName("title")
            var title: String = ""
        ){
            var url: String = ""
            get() = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"

            var smallUrl: String =""
            get() = "https://farm$farm.staticflickr.com/$server/${id}_${secret}_q.jpg"
        }
    }
}