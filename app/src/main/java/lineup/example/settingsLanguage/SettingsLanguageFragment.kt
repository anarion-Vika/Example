package lineup.example.settingsLanguage

import lineup.example.BaseFragment
import lineup.example.R
import lineup.example.settingsLanguage.SettingsLanguageRecyclerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*

class SettingsLanguageFragment : BaseFragment(R.layout.fragment_settings_language) {
    private val binding: FragmentSettingsLanguageBinding by viewBinding(
        FragmentSettingsLanguageBinding::bind
    )

    private val languagesViewModelStrapi: SettingsAppLanguageStrapiViewModel by viewModels()
    private val screenViewModelStrapi: SettingsScreenLanguageStrapiViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        initListeners()
        initObservers()
    }

    override fun initObservers() {
        observe(languagesViewModelStrapi.screenData) {
            val list = arrayListOf<AppLanguagesScreen>()
            list.addAll(it)
            (binding.rvLanguages.adapter as SettingsLanguageRecyclerAdapter).setData(list)
        }
        observe(screenViewModelStrapi.screenData) {
            binding.tvTitle.text = it.tv_title
        }
    }

    private fun initAdapter() {
        binding.rvLanguages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLanguages.adapter = SettingsLanguageRecyclerAdapter(
            callback = object : SettingsLanguageRecyclerAdapter.Callback {
                override fun onChosen(language: AppLanguagesScreen) {
                    showChangeLanguageAlertDialog(
                        language.fullName ?: Locale.getDefault().displayLanguage
                    )
                }
            }
        )
    }

    private fun showChangeLanguageAlertDialog(
        newLanguageName: String
    ) {
        handleAlertDialog(::saveNewLanguageAndRestartApp, newLanguageName)
        findNavController().safeNavigate(
            R.id.alertDialog, screenViewModelStrapi.getBundleForRestartAppDialog()
        )
    }

    private fun handleAlertDialog(function: (newLanguage: String) -> Unit, newLanguage: String) {
        getCurrentNavigationResult<Boolean?>(AppConstants.AlertDialog.ALERT_DIALOG_BUTTON_OK_CLICKED)
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    function(newLanguage)
                }
            }
    }

    //in this project titles for all languages are storing at the Strapi.io
    private fun saveNewLanguageAndRestartApp(newLanguage: String) {
        prefsManager.languageName = newLanguage
        val intent = Intent(requireContext(), StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}