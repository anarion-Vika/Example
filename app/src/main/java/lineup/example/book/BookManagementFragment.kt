package lineup.example.book

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import lineup.example.BaseFragment
import lineup.example.R
import lineup.example.observe
import lineup.example.viewBinding

class BookManagementFragment : BaseFragment(R.layout.fragment_book_managment) {
    private val binding: FragmentBookManagmentBinding by viewBinding(FragmentBookManagmentBinding::bind)

    private val userBookViewModel: UserBooksViewModel by viewModels()
    private val viewModelStrapi: BookManagementStrapiViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        initListeners()
        initObservers()
    }

    override fun initListeners() {
        binding.tvAddNewBook.setOnClickListener {
            goToAddEditBook()
        }
    }


    override fun initObservers() {
        userBookViewModel.loadBooks()
        userBookViewModel.apply {
            observe(listServerOfAllBooks) {
                (binding.rvBooks.adapter as BookManagementRecyclerAdapter).setData(it)
            }
            observe(isShowLoadingBookScreen) {
                if (it)
                    showLoadingDialog()
                else
                    hideLoadingDialog()
            }
        }
        //in this project titles for all languages are storing at the Strapi.io
        observe(viewModelStrapi.screenData) {
            (binding.rvBooks.adapter as BookManagementRecyclerAdapter).translation =
                it

            binding.tvTitle.text = it.tv_title
            binding.tvAddNewBook.text = it.tv_addNewBook
        }
    }

    private fun initAdapter() {
        binding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBooks.adapter = BookManagementRecyclerAdapter(
            callback = object : BookManagementRecyclerAdapter.Callback {
                override fun onEditClicked(book: BookResponse, position: Int) {
                    goToAddEditBook(book)
                }

                override fun onRemoveClicked(book: BookResponse) {
                    showDeleteBookAlertDialog(book)
                }
            }
        )
    }

    private fun showDeleteBookAlertDialog(
        book: BookResponse
    ) {
        handleAlertDialog(userBookViewModel::deleteBook, book.id)
        findNavController().safeNavigate(
            R.id.action_toAlertDialog, viewModelStrapi.getBundleForAlertDialog()
        )
    }

    private fun handleAlertDialog(function: (bookId: Long) -> Unit, id: Long) {
        getCurrentNavigationResult<Boolean?>(AppConstants.AlertDialog.ALERT_DIALOG_BUTTON_OK_CLICKED)
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    function(id)
                }
            }
    }

    private fun goToAddEditBook(book: BookResponse? = null) {
        findNavController().safeNavigate(
            R.id.toOnboarding,
            bundleOf(IS_FROM_BOOK_MANAGEMENT to true, BOOK_EDIT to book)
        )
    }

}