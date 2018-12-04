package ua.com.bpst.flikrtest.dependencies

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ua.com.bpst.flikrtest.helpers.ApiHelper

val apiModule = Kodein.Module {
    bind<String>("baseUrl") with instance("https://api.flickr.com/services/")
    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .baseUrl(instance<String>("baseUrl"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    bind<ApiHelper>() with singleton { instance<Retrofit>().create(ApiHelper::class.java) }
}