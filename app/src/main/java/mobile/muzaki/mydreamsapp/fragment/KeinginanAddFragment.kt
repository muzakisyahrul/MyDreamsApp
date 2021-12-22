package mobile.muzaki.mydreamsapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_keinginan.*
import kotlinx.android.synthetic.main.fragment_keinginan_add.*
import kotlinx.android.synthetic.main.toolbar_primary.*
import kotlinx.coroutines.*
import mobile.muzaki.mydreamsapp.Helper
import mobile.muzaki.mydreamsapp.MainActivity
import mobile.muzaki.mydreamsapp.R
import mobile.muzaki.mydreamsapp.room.Keinginan
import mobile.muzaki.myfriendapp.AppDatabase

private const val ID_EDIT = "ID_EDIT"

class KeinginanAddFragment : Fragment(),View.OnClickListener {
    private lateinit var helper: Helper
    private val TAG:String="KeinginanAddFragment"

    private var nama : String = ""
    private var deskripsi : String = ""
    private var harga : String = ""
    private var terpenuhi : String = ""

    private val db by lazy { AppDatabase(requireContext()) }
    private var id_keinginan = 0

    companion object {
        @JvmStatic
        fun newInstance(param1: Int?) =
            KeinginanAddFragment().apply {
                arguments = Bundle().apply {
                    if (param1 != null) {
                        putInt(ID_EDIT, param1)
                    }
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id_keinginan = it.getInt(ID_EDIT)
        }
        helper = Helper.newInstance(requireContext(),requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_keinginan_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if(id_keinginan==0){
            toolbar_title.setText("Tambah Daftar Keinginan")
            lnTerpenuhi.isVisible = false
        }else{
            toolbar_title.setText("Edit Daftar Keinginan")
            getDetail()
            lnTerpenuhi.isVisible = true
        }
        (activity as MainActivity).setSupportActionBar(app_toolbar)
        (activity as MainActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        (activity as MainActivity).onSupportNavigateUp()
        btnSave!!.setOnClickListener(this)
    }

//    private fun getDetail() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val data = db.keinginanDao().getKeinginan(id_keinginan).get(0)
//            Log.e(TAG,"DETAIL DATA: "+data);
//            etNama.setText(data.nama)
//        }
//
//    }

    @DelicateCoroutinesApi
    private fun getDetail(){
        db.keinginanDao().getKeinginan(id_keinginan).observe(viewLifecycleOwner, { r ->
            val data = r.get(0)
            Log.e(TAG,"data:$data")
            etNama.setText(data.nama)
            etDeskripsi.setText(data.deskripsi)
            etHarga.setText(helper.rupiah_koma(data.harga))
            if(data.terpenuhi!=null){
                etTerpenuhi.setText(helper.rupiah_koma(data.terpenuhi))
            }
        })
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnSave -> {
                    validasiInput()
                }
            }
        }
    }

    private fun validasiInput() {
        nama = etNama.text.toString()
        deskripsi = etDeskripsi.text.toString()
        harga = helper.parse_rupiah(etHarga.text.toString()).toString()
        terpenuhi = helper.parse_rupiah(etTerpenuhi.text.toString()).toString()
        when{
            nama.isEmpty() -> etNama.error = "Nama tidak boleh kosong"
            deskripsi.isEmpty() -> etDeskripsi.error = "Deskripsi tidak boleh kosong"
            harga.isEmpty() -> etHarga.error = "Harga tidak boleh kosong"
            else -> {

                if(id_keinginan==0){
                    val data = Keinginan(nama = nama, deskripsi = deskripsi, harga = harga.toDouble())
                    tambahData(data)
                }else{
                    if(terpenuhi.isEmpty()) { etTerpenuhi.error = "Terkumpul tidak boleh kosong"}
                    val data = Keinginan(id = id_keinginan,nama = nama, deskripsi = deskripsi, harga = harga.toDouble(), terpenuhi=terpenuhi.toDouble())
                    updateData(data)
                }
                (activity as MainActivity).onBackPressed()
            }
        }

    }

    private fun tambahData(data: Keinginan) : Job {

        return GlobalScope.launch {
            db.keinginanDao().tambahKeinginan(data)
        }

    }

    private fun updateData(data: Keinginan) : Job {

        return GlobalScope.launch {
            db.keinginanDao().updateKeinginan(data)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }

}