package id.unware.poken.ui.store.nonregisteredseller.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.unware.poken.R
import id.unware.poken.tools.Utils
import kotlinx.android.synthetic.main.fragment_non_seller.*


class NonSellerFragment : Fragment() {

    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnNonSellerFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_non_seller, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        parentNonSellerCall.setOnClickListener({
            Utils.openDialer(context, "+6281281231103")
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNonSellerFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnNonSellerFragmentInteractionListener {
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                NonSellerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
