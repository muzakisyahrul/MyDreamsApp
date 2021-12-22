package mobile.muzaki.mydreamsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar_primary.*
import mobile.muzaki.mydreamsapp.IOnBackPressed
import mobile.muzaki.mydreamsapp.MainActivity
import mobile.muzaki.mydreamsapp.R


class BiodataFragment : Fragment() , IOnBackPressed {

    companion object {
        fun newInstance(): BiodataFragment {
            return BiodataFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_biodata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        toolbar_title.setText("About Us")
    }

    override fun onBackPressed(): Boolean {
        (activity as MainActivity).setBottomNavigationSelected(R.id.nav_home)
        return false
    }
}