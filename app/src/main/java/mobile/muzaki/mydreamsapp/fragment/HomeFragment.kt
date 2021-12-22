package mobile.muzaki.mydreamsapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.rv_keinginan
import mobile.muzaki.mydreamsapp.Helper
import mobile.muzaki.mydreamsapp.IOnBackPressed
import mobile.muzaki.mydreamsapp.MainActivity
import mobile.muzaki.mydreamsapp.R
import mobile.muzaki.mydreamsapp.adapter.KeinginanHorizontalAdapter
import mobile.muzaki.mydreamsapp.room.Keinginan
import mobile.muzaki.myfriendapp.AppDatabase

class HomeFragment : Fragment() , IOnBackPressed{
    private val TAG:String="HomeFragment"
    private lateinit var helper: Helper
    private val db by lazy { AppDatabase(requireContext()) }
    lateinit var keinginanAdapter: KeinginanHorizontalAdapter
    private var listKeinginan : List<Keinginan>? = null

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getTotalKeinginan("all")
        getTotalKeinginan("terpenuhi")
        loadDataBelumTerpenuhi()
    }


    override fun onBackPressed(): Boolean {
        return true
    }

    private fun getTotalKeinginan(jenis:String){
        if(jenis.equals("all")){
            db.keinginanDao().getKeinginan().observe(viewLifecycleOwner, { r ->
                val data = r.get(0)
                Log.e(TAG,"data all:$data")
                tvTotalKeinginan.setText(r.size.toString())
            })
        }else if(jenis.equals("terpenuhi")){
            db.keinginanDao().getKeinginanTerpenuhi().observe(viewLifecycleOwner, { r ->
                val data = r.get(0)
                Log.e(TAG,"data terpenuhi:$data")
                tvTotalTerpenuhi.setText(r.size.toString())
            })
        }

    }

    private fun loadDataBelumTerpenuhi(){
        listKeinginan = ArrayList()
        db.keinginanDao().getKeinginanBlmTerpenuhi().observe(viewLifecycleOwner, { r ->
            listKeinginan = r
            when {
                listKeinginan?.size == 0 -> {
                    lnBlmTerpenuhi.isVisible=false
                }
                else -> {
                    lnBlmTerpenuhi.isVisible=true
                    keinginanAdapter.setData(listKeinginan as ArrayList<Keinginan>)
                    keinginanAdapter.notifyDataSetChanged()
                }
            }
        })
    }


    private fun initView() {

        keinginanAdapter = KeinginanHorizontalAdapter(
            arrayListOf(),
            requireContext(),
            requireActivity(),
            object : KeinginanHorizontalAdapter.OnAdapterListener {
                override fun onClick(data: Keinginan) {
                }

                override fun onUpdate(data: Keinginan) {
                    (activity as MainActivity).setHalaman(KeinginanAddFragment.newInstance(data.id!!))
                }

                override fun onDelete(data: Keinginan) {
                }

            })

        rv_keinginan.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            adapter = keinginanAdapter
        }
    }
}