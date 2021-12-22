package mobile.muzaki.mydreamsapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_keinginan.*
import kotlinx.android.synthetic.main.toolbar_primary.*
import kotlinx.coroutines.*
import mobile.muzaki.mydreamsapp.Helper
import mobile.muzaki.mydreamsapp.IOnBackPressed
import mobile.muzaki.mydreamsapp.MainActivity
import mobile.muzaki.mydreamsapp.R
import mobile.muzaki.mydreamsapp.adapter.KeinginanAdapter
import mobile.muzaki.mydreamsapp.room.Keinginan
import mobile.muzaki.myfriendapp.AppDatabase

class KeinginanFragment : Fragment(), IOnBackPressed,View.OnClickListener {
    private val TAG:String="KeinginanFragment"
    private lateinit var helper: Helper
    private val db by lazy { AppDatabase(requireContext()) }
    lateinit var keinginanAdapter: KeinginanAdapter
    private var listKeinginan : List<Keinginan>? = null

    companion object {
        fun newInstance(): KeinginanFragment {
            return KeinginanFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = Helper.newInstance(requireContext(),requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keinginan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData()
    }

    private fun loadData(){
        listKeinginan = ArrayList()
        db.keinginanDao().getKeinginan().observe(viewLifecycleOwner, { r ->
            listKeinginan = r
            when {
                listKeinginan?.size == 0 -> {
                    rv_keinginan.isVisible=false
                    helper.tampilToast("Belum ada data")
                }
                else -> {
                    rv_keinginan.isVisible=true
                    keinginanAdapter.setData(listKeinginan as ArrayList<Keinginan>)
                    keinginanAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun initView() {
        toolbar_title.setText("Daftar Keinginan")
        btnTambah!!.setOnClickListener(this)

        keinginanAdapter = KeinginanAdapter(
            arrayListOf(),
            requireContext(),
            requireActivity(),
            object : KeinginanAdapter.OnAdapterListener {
                override fun onClick(data: Keinginan) {

                }

                override fun onUpdate(data: Keinginan) {
                    (activity as MainActivity).setHalaman(KeinginanAddFragment.newInstance(data.id!!))
                }

                override fun onDelete(data: Keinginan) {
                    deleteAlert(data)
                }

            })

        rv_keinginan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = keinginanAdapter
        }
    }

    override fun onBackPressed(): Boolean {
        (activity as MainActivity).setBottomNavigationSelected(R.id.nav_home)
        return false
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnTambah -> {
                    (activity as MainActivity).setHalaman(KeinginanAddFragment.newInstance(null))
                }
            }
        }
    }

    private fun deleteAlert(data: Keinginan){
        val dialog = AlertDialog.Builder(requireContext())
        dialog.apply {
            setTitle("Konfirmasi Hapus")
            setMessage("Yakin hapus ${data.nama}?")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus") { dialogInterface, i ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.keinginanDao().deleteKeinginan(data)
                    dialogInterface.dismiss()
                }
            }
        }

        dialog.show()
    }
}