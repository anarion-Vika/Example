package lineup.example.settingsLanguage

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SettingsLanguageRecyclerAdapter(
    private var listOfLanguages: ArrayList<AppLanguagesScreen> = ArrayList(),
    private val callback: Callback,
) : RecyclerView.Adapter<SettingsLanguageRecyclerAdapter.SettingsLanguageAdapterViewHolder>() {

    override fun onBindViewHolder(holder: SettingsLanguageAdapterViewHolder, position: Int) {
        holder.bind(listOfLanguages[position], callback)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingsLanguageAdapterViewHolder {
        return SettingsLanguageAdapterViewHolder(
            parent.viewBinding(ListSettingsLanguageBinding::inflate)
        )
    }

    override fun getItemCount(): Int {
        return listOfLanguages.size
    }


    fun setData(
        newData: ArrayList<AppLanguagesScreen>
    ) {
        listOfLanguages.clear()
        listOfLanguages.addAll(newData)
        notifyDataSetChanged()
    }

    inner class SettingsLanguageAdapterViewHolder(
        private val binding: ListSettingsLanguageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            entity: AppLanguagesScreen,
            callback: Callback
        ) {
            binding.tvTitle.text = entity.title
            binding.root.setOnClickListener {
                callback.onChosen(entity)
            }
        }
    }

    interface Callback {
        fun onChosen(language: AppLanguagesScreen)
    }
}