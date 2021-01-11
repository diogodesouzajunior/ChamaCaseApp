package com.diogo.chamacaseapp.model

import com.google.gson.annotations.SerializedName


data class Geometry(

    @SerializedName("location") val location: Location,
    @SerializedName("viewport") val viewport: Viewport
)