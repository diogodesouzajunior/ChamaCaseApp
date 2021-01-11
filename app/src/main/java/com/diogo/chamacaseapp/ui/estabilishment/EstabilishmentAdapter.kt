package com.diogo.chamacaseapp.ui.estabilishment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diogo.chamacaseapp.R
import com.diogo.chamacaseapp.model.Estabilishment
import kotlinx.android.synthetic.main.row_estabilishment.view.*
import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Inject


class EstabilishmentAdapter
@Inject
constructor() : RecyclerView.Adapter<EstabilishmentAdapter.ContainerViewHolder>() {
    lateinit var context: Context

    private val routeSubject = PublishSubject.create<Estabilishment>()
    val routeEvent: Observable<Estabilishment> = routeSubject

    var dataList: List<Estabilishment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_estabilishment, parent, false)
        context = parent.context
        return ContainerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {
        val item = dataList[position]


        holder.name.text = item.name
    }

    override fun getItemCount(): Int = dataList.size

    inner class ContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.tvName
        val route = itemView.ivRoute

        init {
            route.setOnClickListener {
                routeSubject.onNext(dataList[adapterPosition])
            }
        }
    }
}