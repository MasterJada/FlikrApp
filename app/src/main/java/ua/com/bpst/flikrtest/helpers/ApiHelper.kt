package ua.com.bpst.flikrtest.helpers

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

import ua.com.bpst.flikrtest.model.ImagesResult


interface ApiHelper {

    @GET("rest?method=flickr.photos.search&api_key=ec15462562adfe9055227ecdbf89a428&format=json&per_page=20&nojsoncallback=1")
    fun search(@Query("text") text: String, @Query("page") page: Int = 0): Observable<ImagesResult>
}

