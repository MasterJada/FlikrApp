package ua.com.bpst.flikrtest.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SearchHistory: RealmObject(){
    @PrimaryKey
    var query: String?= ""
}