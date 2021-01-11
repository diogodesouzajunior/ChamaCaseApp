package com.diogo.chamacaseapp.ui.estabilishment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.diogo.chamacaseapp.R
import com.diogo.chamacaseapp.model.Estabilishment
import com.diogo.chamacaseapp.ui.ViewConstants
import com.diogo.chamacaseapp.ui.base.BaseFragment
import kotlinx.android.synthetic.main.estabilishment_fragment.*
import rx.lang.kotlin.subscribeBy
import javax.inject.Inject


class EstabilishmentFragment : BaseFragment(), EstabilishmentContract.View {

    @Inject
    lateinit var presenter: EstabilishmentContract.Presenter

    lateinit var searchType: String

    @Inject
    lateinit var adapter: EstabilishmentAdapter

    override fun setResults(results: List<Estabilishment>) {
        adapter.dataList = results
    }

    override fun showProgress(show: Boolean) {
        pbProgress.isVisible = show
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.attachView(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater!!.inflate(R.layout.estabilishment_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.let {
            configureRecyclerView()

            searchEstabilishments()
        }
    }

    private fun searchEstabilishments() {

        if (checkPermission(requireContext())) {

            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation == null) {
                Toast.makeText(
                    requireContext(),
                    "Falha ao detectar o Sinal do GPS",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                presenter.searchEstabilishments(
                    searchType,
                    lastKnownLocation.latitude.toString() + "," + lastKnownLocation.longitude.toString(),
                    resources.getString(R.string.API_KEY)
                )
            }
        }
    }

    private fun configureRecyclerView() {
        rcEstabilishment.adapter = adapter

        adapter.routeEvent.subscribeBy(
            onNext = {
                AlertDialog.Builder(context)
                    .setMessage(R.string.trace_route)
                    .setTitle("Atenção")
                    .setPositiveButton("Sim") { dialog, _ ->
                        traceRoute(it)
                    }
                    .setNegativeButton("Não") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        )
    }

    private fun traceRoute(it: Estabilishment) {
        val latitude = it.geometry.location.lat
        val longitude = it.geometry.location.lng


        val uri = Uri.parse("geo:0,0?q=${latitude},${longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)

        startActivity(mapIntent)
    }

    companion object {
        fun getInstance(searchType: String = ViewConstants.SEARCH_TYPE): EstabilishmentFragment {
            var fragment = EstabilishmentFragment()
            fragment.searchType = searchType
            return fragment
        }
    }


    fun checkPermission(context: Context?): Boolean {
        return (context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } === PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED)
    }

}