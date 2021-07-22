package lineup.example

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    private var loadingDialog: LoadingDialog? = null
    private val onlineChatViewModel: OnlineChatViewModel by activityViewModels()

    protected val prefsManager = PrefsManager()

    protected fun showLoadingDialog(
        title: String? = null,
        isCancelable: Boolean = false
    ) {
        childFragmentManager.findFragmentByTag(LoadingDialog.TAG)?.let {
            updateLoadingDialogTitle(title)
        } ?: kotlin.run {
            loadingDialog?.let {
                updateLoadingDialogTitle(title)
            } ?: kotlin.run {
                createNewLoadingDialog(title, isCancelable)
            }
        }
    }

    private fun createNewLoadingDialog(
        title: String? = null,
        isCancelable: Boolean = false
    ) {
        loadingDialog = LoadingDialog()

        loadingDialog?.arguments = Bundle().apply {
            putBoolean(LoadingDialog.IS_CANCELABLE, isCancelable)
            putString(LoadingDialog.TITLE, title)
        }
        loadingDialog?.show(childFragmentManager, LoadingDialog.TAG)
    }

    private fun updateLoadingDialogTitle(title: String? = null) {
        val dialog: LoadingDialog? =
            childFragmentManager.findFragmentByTag(LoadingDialog.TAG) as LoadingDialog?
        dialog?.let {
            dialog.setTitle(title)
        }
    }

    protected fun hideLoadingDialog() {
        (childFragmentManager.findFragmentByTag(LoadingDialog.TAG) as LoadingDialog?)?.dismiss()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    open fun initViews() {}
    open fun initObservers() {}
    open fun initListeners() {}
    open fun initList() {}
    open fun subscribeOnBackStackEntry() {}

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog = null
    }
}