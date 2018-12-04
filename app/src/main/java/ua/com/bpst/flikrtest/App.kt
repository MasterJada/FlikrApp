package ua.com.bpst.flikrtest

import android.app.Application
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.autoAndroidModule
import io.realm.Realm
import io.realm.RealmConfiguration
import ua.com.bpst.flikrtest.dependencies.apiModule
import ua.com.bpst.flikrtest.mainMVP.MainModel
import ua.com.bpst.flikrtest.mainMVP.MainPresenter
 val kodein = Kodein {
    import(apiModule)
    bind<MainModel>() with provider { MainModel() }
    bind<MainPresenter>() with provider { MainPresenter(instance()) }

}
class App: Application() {


    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

    }
}