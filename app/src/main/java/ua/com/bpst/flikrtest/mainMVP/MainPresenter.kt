package ua.com.bpst.flikrtest.mainMVP

import ua.com.bpst.flikrtest.hideKeyboard


class MainPresenter(m: MainModel) {
    private val model: MainModel = m
    private var view: MainActivity? = null

    fun attachView(v: MainActivity) {
        view = v
        loadAutocomplete()
    }

    fun detachView() {
        view = null
    }

    private fun loadAutocomplete() {
        view?.loadAutocomplete(model.loadAutocomplete())
    }

    private fun addQueryToDB() {
        view?.query?.let {
            model.addQueryToDatabase(it)
        }
    }

    fun sendRequest() {
        view?.let { activity ->
            addQueryToDB()
            model.sendRequest(activity.query, {
                activity.adapter.addItems(it.photos.photo)
                activity.makeClearButton()
                model.page = it.photos.page
                activity.loaded()
            }, {
                activity.showError(it.message)
                activity.hideKeyboard()
            })
        }
    }

    fun loadMore() {
        view?.let { activity ->
            model.page++
            model.sendRequest(activity.query, {
                activity.adapter.addItems(it.photos.photo)
                activity.makeClearButton()
                model.page = it.photos.page
            }, {
                activity.showError(it.message)
            })
        }
    }

    fun resetSearch(){
        model.page = 0
    }



}