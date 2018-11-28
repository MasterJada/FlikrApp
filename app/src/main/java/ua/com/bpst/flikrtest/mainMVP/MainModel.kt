package ua.com.bpst.flikrtest.mainMVP


import io.realm.Realm
import io.realm.kotlin.where
import ua.com.bpst.flikrtest.ImagesResult
import ua.com.bpst.flikrtest.SearchHistory
import ua.com.bpst.flikrtest.helpers.ApiHelper
import ua.com.bpst.flikrtest.send
import java.lang.Exception

class MainModel {
    var page = 0
    fun loadAutocomplete(): ArrayList<String>{
        val autoComplete = ArrayList<String>()
        Realm.getDefaultInstance().use { realm ->
            val realmResult = realm.where<SearchHistory>().findAll()
            realmResult.forEach {
                autoComplete.add(it.query!!)
            }
        }
        return autoComplete
    }
    fun addQueryToDatabase(query: String): ArrayList<String>{
        Realm.getDefaultInstance().use { realm ->
            val itm = SearchHistory()
            itm.query = query
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(itm)
            realm.commitTransaction()
        }
        return loadAutocomplete()
    }
    fun sendRequest(query: String,  callback: (ImagesResult)->Unit, error: (Throwable)->Unit){
        ApiHelper.instance.search(query, page).send({
            callback.invoke(it)
        },{
           error.invoke(it)
        })
    }

}