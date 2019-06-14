package com.cogimag.michalg.einkaufsliste

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.TextView


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
        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
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
                if (menuItem!!.itemId == R.id.menu_item_laden_hinzufügen) {
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
        val rootView:View = inflater.inflate(R.layout.fragment_laden_rv, container, false)
        toolbar = rootView.findViewById(R.id.laden_rv_frag_toolbar)
        recyclerView = rootView.findViewById(R.id.laden_rv_frag_rv_element)
        emptyRv = rootView.findViewById(R.id.laden_rv_frag_empty_rv)

        return rootView
    }


    override fun onResume() {
//        Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " on resume")
        super.onResume()
        dataset = ArrayList()
//        ladenRvAdapter = LadenRvAdapter(dataset)
//        ladenRvAdapter = LadenRvAdapter(dataset, object :View.OnClickListener {
//            override fun onClick(v: View?) {
//                Log.i(AppConstants.APP_LOG_TAG, CLASS_LOG_TAG + " click received on id " + v!!.resources.getResourceName(v!!.id) +
//                        "\n\tlist position " + v!!.getTag(R.id.laden_rv_layout_hintergrund_btn_delete_tag_list_position))
//
////                dataset.removeAt(v!!.getTag(R.id.laden_rv_layout_hintergrund_btn_delete_tag_list_position))
//            }
//        })
        ladenRvAdapter = LadenRvAdapter(dataset, LadenRvHintergrundClickListener())
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = ladenRvAdapter
        }
        val dbUtility = LokaleDb(context!!)
        if (dbUtility.alleLaden().size > 0) {
            dataset.clear()
            dataset.addAll(dbUtility.alleLaden())
        }
//        if (dbUtility.openDbForRead()) {
//            dataset.clear()
//            dataset.addAll(dbUtility.alleLaden())
//            dbUtility.closeDb()
//        }
        recyclerView.visibility = if(dataset.isEmpty()) View.GONE else View.VISIBLE
        emptyRv.visibility = if(dataset.isEmpty()) View.VISIBLE else View.GONE

    }



    inner class LadenRvHintergrundClickListener:View.OnClickListener {

        private val KLASSE_LOG_TAG = "LadenRvHintergrundClickListener"

        override fun onClick(v: View?) {
            Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on rv hintergrund ")

            when (v!!.id) {
                R.id.laden_rv_layout_hintergrund_btn_loeschen -> {
                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on delete btn")

                    LokaleDb(context!!).run {
                        val listPosition:Int = v!!.getTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_list_position) as Int
                        val ladenId:Long = v!!.getTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_laden_id) as Long
                        val affectedRows:Int = deleteLadenRecord(ladenId)
                        if (affectedRows > 0) {
                            dataset.removeAt(listPosition)
                            ladenRvAdapter.notifyItemRemoved(listPosition)
                        }
                    }
                }
                R.id.laden_rv_layout_hintergrund_btn_bearbeiten -> {
                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click received on edit btn")
                    val intentLadenVerarbeiten:Intent = Intent(context, LadenBearbeiten::class.java)
                    val ladenId:Long =v!!.getTag(R.id.laden_rv_layout_hintergrund_btn_bearbeiten_tag_laden_id) as Long
                    intentLadenVerarbeiten.putExtra(LadenModell.FELD_LADEN_ID, ladenId)
                    startActivity(intentLadenVerarbeiten)
                }
                else -> {
                    Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " click on unidentified element " + v!!.id)
                }

            }
        }

    }





    // TO DO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnFragmentInteractionListener {
//        // TO DO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

}
