package com.diogo.chamacaseapp.data

import com.diogo.chamacaseapp.data.remote.SimpleBusEvent
import com.diogo.chamacaseapp.remote.service.ChamaCaseService


class DataManager constructor(
    private val service: ChamaCaseService,
    private val eventBus: SimpleBusEvent
) {

    fun searchEstabilishments(searchType: String, location: String, apiKey: String) =
        service.searchEstabilishments(location,5000, searchType, apiKey)
}
