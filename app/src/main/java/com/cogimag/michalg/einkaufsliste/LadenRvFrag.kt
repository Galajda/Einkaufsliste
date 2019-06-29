package com.cogimag.michalg.einkaufsliste

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_laden_rv.*
import kotlinx.android.synthetic.main.laden_rv_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_laden_rv.view.*


// TO DO Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LadenRvFrag.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class LadenRvFrag : Fragment() {
//    private var listener: OnFragmentInteractionListener? = null
    private val KLASSE_LOG_TAG = "LadenRvFrag"

    private lateinit var toolbar: Toolbar

    private lateinit var dataset:ArrayList<LadenModell>
    private lateinit var ladenRvAdapter:LadenRvAdapter
    //    private lateinit var dbUtility:LocalDb  //remove as class var. instantiate where needed
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyRv: TextView
    private lateinit var viewModel: SharedViewModel


    //viewModel = ViewModelProviders.of(this).get(TestFragViewModel::class.java)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
//        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG onActivityCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        toolbar.inflateMenu(R.menu.menu_laden_hinzufuegen)
        toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
//                Log.i(getString(R.string.app_log_tag), CLASS_LOG_TAG + " toolbar onMenuItemClick id " + menuItem!!.itemId)
                if ((menuItem?.itemId ?: -1) == R.id.menu_item_laden_hinzufügen) {
                    val intentLadenHinzufügen: Intent = Intent(context, LadenHinzufuegen::class.java)
                    //so 45518139 how to make an intent in Kotlin
                    startActivity(intentLadenHinzufügen)
                }
                return true //consume event
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG onCreateView")
        val rootView:View = inflater.inflate(R.layout.fragment_laden_rv, container, false)
        toolbar = rootView.findViewById(R.id.laden_rv_frag_toolbar)
//        toolbar = laden_rv_frag_toolbar
        recyclerView = rootView.findViewById(R.id.laden_rv_frag_rv_element)
        emptyRv = rootView.findViewById(R.id.laden_rv_frag_empty_rv)


//        val testBtn: Button = rootView.findViewById(R.id.laden_rv_frag_btn_test_rv_contents)
//        testBtn.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG click on laden_rv_frag_btn_test_rv_contents. es gibt ${recyclerView.childCount} rv elemente")
//            //                val sharedViewModel = ViewModelProviders.of(activity?).get(SharedViewModel::class.java)
////                activity?.let {
////                    val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
////                    Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG laden id = ${sharedViewModel.ladenId.value}")
////                // this works
////                }
////                recyclerView.childCount
//
//            //                Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG laden id = ${sharedViewModel.ladenId.value}")
//            }
//        })

        return rootView
    }


    override fun onResume() {
//        Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " on resume")
        super.onResume()
        dataset = ArrayList()

        ladenRvAdapter = LadenRvAdapter(dataset, LadenRvClickListener())
//        ladenRvAdapter = LadenRvAdapter(dataset, LadenRvClickListenerTest())
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = ladenRvAdapter
            //so 30397460 add global layout listener. derived from 7517636
            viewTreeObserver.addOnGlobalLayoutListener ( object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
//                    Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG on global layout")
                    activity?.also {
                        val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
//            Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG on resume view model id = ${sharedViewModel.ladenId.value}")
                        if (sharedViewModel.ladenId.value != null) {
                            listeMarkieren(sharedViewModel.ladenId.value as Long)
//                            scrollToPosition()
                        }
                    }
                    viewTreeObserver.removeOnGlobalLayoutListener { this }
                    //still gets called twice
                }
            })
        }
        context?.also {
            LokaleDb(it).apply {
                if (alleLaden().size > 0) {
                    dataset.clear()
                    dataset.addAll(alleLaden())
                }
            }
        }
        recyclerView.visibility = if(dataset.isEmpty()) View.GONE else View.VISIBLE
        emptyRv.visibility = if(dataset.isEmpty()) View.VISIBLE else View.GONE
    }


    inner class LadenRvClickListenerTest(val listPosition:Int, val ladenId:Long):View.OnClickListener {
        //want to pass list pos and laden id to constructor
        private val KLASSE_LOG_TAG = "LadenRvClickListenerTest"

        override fun onClick(v: View?) {
//            Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on rv ")

            if (v != null) {
                when (v.id) {
                    R.id.laden_rv_layout_hintergrund_btn_loeschen -> {
        //                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on delete btn")
                        context?.let {
                            LokaleDb(it).apply {
                                val listPosition:Int = v.getTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_list_position) as Int
                                val ladenId:Long = v.getTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_laden_id) as Long
                                val affectedRows:Int = ladenRecordLoeschen(ladenId)
                                if (affectedRows > 0) {
                                    dataset.removeAt(listPosition)
                                    ladenRvAdapter.notifyItemRemoved(listPosition)
                                }
                            }
                        }
                    }
                    R.id.laden_rv_layout_hintergrund_btn_bearbeiten -> {
        //                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on edit btn")
                        val intentLadenVerarbeiten:Intent = Intent(context, LadenBearbeiten::class.java)
                        val ladenId:Long =v.getTag(R.id.laden_rv_layout_hintergrund_btn_bearbeiten_tag_laden_id) as Long
                        intentLadenVerarbeiten.putExtra(LadenModell.FELD_LADEN_ID, ladenId)
                        startActivity(intentLadenVerarbeiten)
                    }
                    R.id.laden_rv_layout_vordergrund -> {
//                        Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on vordergrund")
                        val listPosition:Int = v.getTag(R.id.laden_rv_layout_vordergrund_tag_list_position) as Int
                        val ladenId:Long = v.getTag(R.id.laden_rv_layout_vordergrund_tag_db_id) as Long
//                        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG list pos $listPosition laden id $ladenId")
                    }
                    else -> {
                        Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click on unidentified element " + v.id)
                    }
                }
            }
        }
    }

    inner class LadenRvClickListener:View.OnClickListener {

        private val KLASSE_LOG_TAG = "LadenRvClickListener"

        override fun onClick(v: View?) {
//            Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on rv ")

            if (v != null) {
                when (v.id) {
                    R.id.laden_rv_layout_hintergrund_btn_loeschen -> {
        //                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on delete btn")
                        context?.also {
                            LokaleDb(it).apply {
                                val listPosition:Int = v.getTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_list_position) as Int
                                val ladenId:Long = v.getTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_laden_id) as Long
                                val affectedRows:Int = ladenRecordLoeschen(ladenId)
                                if (affectedRows > 0) {
                                    dataset.removeAt(listPosition)
                                    ladenRvAdapter.notifyItemRemoved(listPosition)
                                }
                            }
                        }
                    }
                    R.id.laden_rv_layout_hintergrund_btn_bearbeiten -> {
        //                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on edit btn")
                        val intentLadenBearbeiten:Intent = Intent(context, LadenBearbeiten::class.java)
                        val ladenId:Long =v.getTag(R.id.laden_rv_layout_hintergrund_btn_bearbeiten_tag_laden_id) as Long
                        intentLadenBearbeiten.putExtra(LadenModell.FELD_LADEN_ID, ladenId)

                        startActivity(intentLadenBearbeiten)
                    }
                    R.id.laden_rv_layout_vordergrund -> {
                        listItemAuswaehlen(v)
                    }
                    else -> {
                        Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click on unidentified element " + v.id)
                    }
                }
            }
        }
    }

    private fun listItemAuswaehlen(v: View) {
        val listPosition:Int = v.getTag(R.id.laden_rv_layout_vordergrund_tag_list_position) as Int
        val ladenId:Long = v.getTag(R.id.laden_rv_layout_vordergrund_tag_db_id) as Long
//        Log.i(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG list pos $listPosition laden id $ladenId")
        activity?.let {
            val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            sharedViewModel.ladenId.value = ladenId //use this for main thread, postValue for background thread
            listeMarkieren(ladenId)
        }
    }

    private fun listeMarkieren(herausgewaehlterLadenId: Long) {

        //recyclerView.indexOfChild(recyclerView.focusedChild)always -1
        (0 until ladenRvAdapter.itemCount).forEach { i ->
            //recyclerView.childCount nicht verwended
            //SO 49859347 rv count represents the displayed items. adapter count represents all in the dataset
            recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.
                laden_rv_layout_vordergrund?.apply {
                if (getTag(R.id.laden_rv_layout_vordergrund_tag_db_id) as Long == herausgewaehlterLadenId)
                    setBackgroundColor(Color.GRAY) else setBackgroundColor(Color.LTGRAY)

            }

        }

    }



}
