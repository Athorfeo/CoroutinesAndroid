package com.athorfeo.source.app.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.athorfeo.source.util.CoroutinesTestRule
import com.athorfeo.source.repository.MovieRepository
import com.athorfeo.source.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class MainViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(MovieRepository::class.java)
    private lateinit var viewModel: MainViewModel

    @Before
    fun init() {
        viewModel = MainViewModel(repository)
    }

    @Test
    fun empty() {
        viewModel.movies.observeForever(mock())
        viewModel.searchMovies("")
        verify(repository).searchMovies("", 1)
    }
}