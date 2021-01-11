package com.diogo.chamacaseapp.model

import com.google.gson.annotations.SerializedName

data class BaseResponse (

    @SerializedName("html_attributions") val html_attributions : List<String>,
    @SerializedName("next_page_token") val next_page_token : String,
    @SerializedName("results") val results : List<Estabilishment>,
    @SerializedName("status") val status : String
)