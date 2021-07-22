package lineup.example.book

import lineup.example.mutableLiveData
import lineup.example.singleLiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class UserBooksViewModel(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository by lazy { BookRepository() }

    private val _isShowLoadingBookScreen = singleLiveData<Boolean>()
    val isShowLoadingBookScreen = _isShowLoadingBookScreen

    private val _showErrorMsg = singleLiveData<String>()
    val showErrorMsg = _showErrorMsg


    private val _listServerOfAllBooks = mutableLiveData<ArrayList<BookResponse>>()
    val listServerOfAllBooks = _listServerOfAllBooks


    fun loadBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowLoadingBookScreen.postValue(true)
            val response = bookRepository.getAllBooks()
            response.onSuccess {
                distributeResponseData(it)
            }
            isShowLoadingBookScreen.postValue(false)
        }.onError {
            showErrorMsg.postValue(it.message ?: "error")
            isShowLoadingBookScreen.postValue(false)
        }.onHTTPError {
            showErrorMsg.postValue(it.error?.message)
            isShowLoadingBookScreen.postValue(false)
        }
    }

    private fun distributeResponseData(serverBooks: List<BookResponse>) {
        val list = arrayListOf<BookResponse>()
        list.addAll(serverBooks.sortedBy { it.id })
        listServerOfAllBooks.postValue(list)
    }
}