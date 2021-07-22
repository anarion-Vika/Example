package lineup.example

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


fun <T> mutableLiveData(): MutableLiveData<T> = MutableLiveData<T>()

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

inline fun <reified L : LiveData<T>, T> Fragment.observe(
    livedata: LiveData<T>?, noinline block: (T) -> (Unit)
) {
    livedata?.observe(viewLifecycleOwner, Observer(block))
}

fun NavController.safeNavigate(directions: Int, bundle: Bundle): Boolean =
    try {
        navigate(directions, bundle)
        true
    } catch (e: IllegalArgumentException) {
        Timber.e(e, "safeNavigate to $directions")
        false
    }

fun <T> Fragment.getCurrentNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)