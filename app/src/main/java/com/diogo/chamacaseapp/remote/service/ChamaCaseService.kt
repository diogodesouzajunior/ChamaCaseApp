package com.diogo.chamacaseapp.remote.service

import com.diogo.chamacaseapp.model.BaseResponse
import com.diogo.chamacaseapp.remote.RemoteConstants
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface ChamaCaseService {

    @GET(RemoteConstants.MAPS_URL)
    fun searchEstabilishments(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String
    ): Observable<BaseResponse>




}