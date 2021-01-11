package com.diogo.chamacaseapp.model

import com.google.gson.annotations.SerializedName

data class Southwest (

	@SerializedName("lat") val lat : Double,
	@SerializedName("lng") val lng : Double
)