package com.diogo.chamacaseapp.ui.estabilishment

import com.diogo.chamacaseapp.model.Estabilishment
import com.diogo.chamacaseapp.ui.base.BasePresenter
import com.diogo.chamacaseapp.ui.base.MvpView


class EstabilishmentContract {

    interface View : MvpView {
        fun setResults(results: List<Estabilishment>)
        fun showProgress(show : Boolean)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun searchEstabilishments(searchType: String, location: String, apiKey: String)
    }
}