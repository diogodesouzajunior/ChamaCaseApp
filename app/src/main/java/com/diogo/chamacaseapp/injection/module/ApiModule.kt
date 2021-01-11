package com.diogo.chamacaseapp.injection.module


import com.diogo.chamacaseapp.data.remote.SimpleBusEvent
import com.diogo.chamacaseapp.remote.interceptor.DownloadProgressInterceptor
import com.diogo.chamacaseapp.remote.interceptor.RequestInterceptor
import com.diogo.chamacaseapp.remote.service.ChamaCaseService
import com.diogo.chamacaseapp.util.UnsafeOkHttpClient
import com.google.gson.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class ApiModule {

    /**
     * Prove o parser de Json para a aplicação
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()

        builder.registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
            json?.asJsonPrimitive?.asLong?.let {
                return@JsonDeserializer Date(it)
            }
        })

        builder.registerTypeAdapter(Date::class.java, JsonSerializer<Date> { date, _, _ ->
            JsonPrimitive(date.time)
        })

        return builder.create()
    }


    /**
     * Prove o interceptor das requisições. Utilizado para adicionar header de token, por exemplo.
     */
    @Provides
    @Singleton
    fun provideRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    @Provides
    @Singleton
    fun provideStreamingInterceptor(eventBus: SimpleBusEvent): DownloadProgressInterceptor {
        return DownloadProgressInterceptor(eventBus)
    }

    @Provides
    @Singleton
    fun provideSimpleEventBus() = SimpleBusEvent()

    /**
     * Provê o interceptor de logging das requisições
     */
    @Provides
    @Singleton
    fun provideLogInterceptor(): HttpLoggingInterceptor {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return logInterceptor
    }

    /**
     * Provê o httpClient padrão para o App
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor,
                            logInterceptor: HttpLoggingInterceptor,
                            doownLoadInterceptor: DownloadProgressInterceptor): OkHttpClient {

        val builder = UnsafeOkHttpClient.getUnsafeOkHttpClient()


        builder.addInterceptor(logInterceptor)
        builder.addInterceptor(requestInterceptor)
        builder.addInterceptor(doownLoadInterceptor)

        builder.connectTimeout(2, TimeUnit.MINUTES)
        builder.readTimeout(1, TimeUnit.MINUTES)
        builder.readTimeout(1, TimeUnit.MINUTES)

        return builder.build()
    }

    /**
     * Provê o ChamaCaseService para a aplicação
     */
    @Provides
    @Singleton
    fun provideChamaCaseService(okHttpClient: OkHttpClient, gson: Gson): ChamaCaseService {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(ChamaCaseService::class.java)
    }

}
