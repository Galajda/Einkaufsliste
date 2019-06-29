package com.cogimag.michalg.einkaufsliste

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_waren_rv.*
import kotlinx.android.synthetic.main.fragment_waren_rv.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class WarenRvFrag : Fragment() {
    private val KLASSE_LOG_TAG = "WarenRvFrag"

    private lateinit var toolbar: Toolbar
    private lateinit var dataset:ArrayList<WarenModell>
    private lateinit var warenRvAdapter:WarenRvAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyRv: TextView
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            val sharedViewModel: SharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            sharedViewModel.ladenId.observe(this, Observer<Long> { ladenId ->
                Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG observe shared view model laden id $ladenId")
                waren_rv_frag_TV_laden_id.text = ladenId.toString()

                context?.also {ctxt ->
                    LokaleDb(ctxt).apply {
                        ladenId?.also { id ->
                            if (alleWarenEinesLadens(id).size > 0) {
                                //how to check initialization SO 37618737 & kotlin ref checking whether a lateinit var is initialized
                                if ((::dataset.isInitialized) and (::warenRvAdapter.isInitialized)) {
                                    Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG adding ${alleWarenEinesLadens(id).size} Läden to dataset")
                                    dataset.clear()
                                    dataset.addAll(alleWarenEinesLadens(id))
                                    warenRvAdapter.notifyDataSetChanged()
                                }
                                waren_rv_frag_rv_element.visibility = View.VISIBLE
                                waren_rv_frag_empty_rv.visibility = View.GONE
                            }
                            else {
                                waren_rv_frag_rv_element.visibility = View.GONE
                                waren_rv_frag_empty_rv.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        toolbar.inflateMenu(R.menu.menu_ware_hinzufuegen)
        toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                if ((menuItem?.itemId ?: -1) == R.id.menu_item_ware_hinzufügen) {
                    //so 45518139 how to make an intent in Kotlin
                    val intentWareHinzufügen: Intent = Intent(context, WareHinzufuegen::class.java)
                    activity?.let {
                        val ladenId:Long =
                        ViewModelProviders.of(it).get(SharedViewModel::class.java).ladenId.value ?: -1
                        if (ladenId > 0) {
                            intentWareHinzufügen.putExtra(WarenModell.FELD_WAREN_LADEN, ladenId)
                            startActivity(intentWareHinzufügen)
                        }
                        else {
                            Log.e(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG laden id ungültig")
                        }

                    }


                }
                return true //consume event
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_waren_rv, container, false)
        toolbar = rootView.waren_rv_frag_toolbar
        //rootView.findViewById(R.id.waren_rv_frag_toolbar)
        recyclerView = rootView.findViewById(R.id.waren_rv_frag_rv_element)
        emptyRv = rootView.findViewById(R.id.waren_rv_frag_empty_rv)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        var ladenId:Long = -1
        activity?.let {
            val sharedViewModel: SharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            ladenId = sharedViewModel.ladenId.value ?: -1
        }

        dataset = ArrayList()
        warenRvAdapter = WarenRvAdapter(dataset, WarenRvClickListener())
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = warenRvAdapter
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
//                    Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG on global layout")
                    activity?.also {
                        val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
                        ladenId = sharedViewModel.ladenId.value ?: -1
                        if (ladenId > 0) {
//                            Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG on resume global layout listener got laden id $ladenId")
                        }
                    }
                    viewTreeObserver.removeOnGlobalLayoutListener { this }
                }
            })
        }
        context?.also {
            LokaleDb(it).apply {
                if ((ladenId > 0) and (alleWarenEinesLadens(ladenId).size > 0)) {
                    dataset.clear()
                    dataset.addAll(alleWarenEinesLadens(ladenId))
                }
            }
        }
        recyclerView.visibility = if (dataset.isEmpty()) View.GONE else View.VISIBLE
        emptyRv.visibility = if (dataset.isEmpty()) View.VISIBLE else View.GONE

    }

    inner class WarenRvClickListener: View.OnClickListener {

        private val KLASSE_LOG_TAG = "WarenRvClickListener"

        override fun onClick(v: View?) {
            if (v != null) {

                when (v.id) {
                    R.id.waren_rv_layout_hintergrund_btn_loeschen -> {
                        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG click on löschen")
                        context?.also {
                            LokaleDb(it).apply {
                                val listPosition:Int = v.getTag(R.id.waren_rv_layout_hintergrund_btn_loeschen_tag_list_position) as Int
                                val warenId:Long = v.getTag(R.id.waren_rv_layout_hintergrund_btn_loeschen_tag_waren_id) as Long
                                val affectedRows:Int = warenRecordLoeschen(warenId)
                                if (affectedRows > 0) {
                                    dataset.removeAt(listPosition)
                                    warenRvAdapter.notifyItemRemoved(listPosition)
                                }
                            }
                        }
                    }
                    R.id.waren_rv_layout_hintergrund_btn_bearbeiten -> {
                        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG click on bearbeiten")
                        val intentWarenBearbeiten:Intent = Intent(context, WarenBearbeiten::class.java)
                        val warenId:Long =v.getTag(R.id.waren_rv_layout_hintergrund_btn_bearbeiten_tag_waren_id) as Long
                        intentWarenBearbeiten.putExtra(WarenModell.FELD_WAREN_ID, warenId)

                        startActivity(intentWarenBearbeiten)
                    }
                    R.id.waren_rv_layout_vordergrund -> {
                        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG click on vordergrund")

                    }
                    else -> {
                        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG click on unidentified view")
                    }
                }



            }



        }
    }


}
