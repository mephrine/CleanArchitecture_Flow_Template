package mephrine.template.core.presentations.base

import androidx.lifecycle.*
import kotlinx.coroutines.flow.map
import mephrine.template.utility.errors.CacheError
import mephrine.template.utility.errors.Failure
import mephrine.template.utility.errors.NetworkDisconnect
import mephrine.template.utility.errors.ServerError

interface BaseViewModel


abstract class BaseViewModelImpl : ViewModel(), BaseViewModel {
  protected val TAG: String = BaseViewModelImpl::class.java.simpleName

  protected val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
  val isLoading: LiveData<Boolean>
    get() = _isLoading

  protected val _failure: MutableLiveData<Throwable> = MutableLiveData()
  val failure: LiveData<Failure>
    get() = _failure.asFlow()
      .map {
        when (it) {
          is ServerError -> Failure.ServerFailure(it.message)
          is NetworkDisconnect -> Failure.NetworkConnectionFailure
          is CacheError -> Failure.CacheFailure
          else -> Failure.ServerFailure()
        }
      }.asLiveData()

  override fun onCleared() {
    clearViewModel()
    super.onCleared()
  }

  abstract fun clearViewModel()
}

class EmptyViewModel : BaseViewModel