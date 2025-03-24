package com.example.pokedexapp682474.di

import android.content.Context
import com.example.pokedexapp682474.data.LocalDataSource
import com.example.pokedexapp682474.data.RemoteDataSource
import com.example.pokedexapp682474.interfaces.PokemonAPIService
import com.example.pokedexapp682474.mappers.PokemonDetailsMapper
import com.example.pokedexapp682474.mappers.PokemonListMapper
import com.example.pokedexapp682474.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonAPIService(): PokemonAPIService {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        api: PokemonAPIService,
        listMapper: PokemonListMapper,
        detailsMapper: PokemonDetailsMapper
    ) = RemoteDataSource(api, listMapper, detailsMapper)

    @Provides
    @Singleton
    fun provideLocalDataSource(
        @ApplicationContext context: Context
    ) = LocalDataSource(context)

    @Provides
    @Singleton
    fun providePokemonRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ) = PokemonRepository(remote, local)
}
