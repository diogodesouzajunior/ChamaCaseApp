package com.diogo.chamacaseapp.model


import com.google.gson.annotations.SerializedName

data class Estabilishment (

	@SerializedName("business_status") val business_status : String,
	@SerializedName("geometry") val geometry : Geometry,
	@SerializedName("icon") val icon : String,
	@SerializedName("name") val name : String,
	@SerializedName("opening_hours") val opening_hours : Opening_hours,
	@SerializedName("photos") val photos : List<Photos>,
	@SerializedName("place_id") val place_id : String,
	@SerializedName("plus_code") val plus_code : Plus_code,
	@SerializedName("rating") val rating : Double,
	@SerializedName("reference") val reference : String,
	@SerializedName("scope") val scope : String,
	@SerializedName("types") val types : List<String>,
	@SerializedName("user_ratings_total") val user_ratings_total : Int,
	@SerializedName("vicinity") val vicinity : String
)