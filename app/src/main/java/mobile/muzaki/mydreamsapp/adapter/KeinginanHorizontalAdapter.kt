package mobile.muzaki.mydreamsapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_keinginan_horizontal.view.*
import mobile.muzaki.mydreamsapp.Helper
import mobile.muzaki.mydreamsapp.R
import mobile.muzaki.mydreamsapp.room.Keinginan

class KeinginanHorizontalAdapter (var keinginan: ArrayList<Keinginan>, context: Context, activity: Activity, var listener: OnAdapterListener) :
    RecyclerView.Adapter<KeinginanHorizontalAdapter.NoteViewHolder>(){
    private var helper: Helper = Helper.newInstance(context,activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.listview_keinginan_horizontal,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = keinginan.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val keinginan = keinginan[position]
        holder.view.tvNama.text = keinginan.nama
        holder.view.tvDeskripsi.text = keinginan.deskripsi

        var terpenuhi:Double = 0.0
        if(keinginan.terpenuhi != null){
            terpenuhi = keinginan.terpenuhi
        }
        holder.view.tvNominal.text = helper.rupiah(keinginan.harga)
        holder.view.tvTerkumpul.text = helper.rupiah(terpenuhi)
        holder.view.tvNominalCurrent.text = helper.rupiah(keinginan.harga-terpenuhi)
        var persentase:Int
        if(terpenuhi<keinginan.harga){
            persentase = (terpenuhi/keinginan.harga*100).toInt()
            holder.view.lnKurang.isVisible = true
            holder.view.lnTerpenuhi.isVisible = false
        }else{
            persentase=100
            holder.view.lnKurang.isVisible = false
            holder.view.lnTerpenuhi.isVisible = true

        }

        holder.view.progressBar.setProgress(persentase)
        holder.view.layout_utama.setOnClickListener {
            listener.onUpdate(keinginan)
        }
    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newList: List<Keinginan>) {
        keinginan.clear()
        keinginan.addAll(newList)

    }

    interface OnAdapterListener {
        fun onClick(note: Keinginan)
        fun onUpdate(note: Keinginan)
        fun onDelete(note: Keinginan)
    }
}