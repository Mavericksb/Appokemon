package com.rob.gab.appokemon.di

import androidx.paging.ExperimentalPagingApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rob.gab.appokemon.Constants.ENDPOINT
import com.rob.gab.appokemon.data.network.ApiService
import com.rob.gab.appokemon.data.network.PokemonsRemoteMediator
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalPagingApi val networkProvider = module {
    single { createOkHttpClient() }

    single { createWebService<ApiService>(get(), get()) }

    single { provideMoshi() }

//    single { PokemonPagingSource(get())}

    factory { PokemonsRemoteMediator(get(), get()) }
}

fun createOkHttpClient(/*mockInterceptor: MockInterceptor? = null*/): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
    val builder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
    return /*if (mockInterceptor != null) builder.addInterceptor(mockInterceptor).build() else*/ builder.build()
}

inline fun <reified T> createWebService(client: OkHttpClient, moshi: Moshi): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    return retrofit.create(T::class.java)
}

fun provideMoshi() : Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}