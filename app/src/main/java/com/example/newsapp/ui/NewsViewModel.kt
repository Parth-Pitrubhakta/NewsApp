package com.example.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {
    val technologyNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var technologyNewsPage = 1

    init {
        getTechnologyNews("us")
    }

    fun getTechnologyNews(countryCode: String) = viewModelScope.launch {
        technologyNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, technologyNewsPage)
        technologyNews.postValue(handleTechnologyNewsResponse(response))
    }

    private fun handleTechnologyNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}