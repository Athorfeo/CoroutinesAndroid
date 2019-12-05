package com.athorfeo.source.app.ui.custom
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.athorfeo.source.BuildConfig
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentCustomBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ui.CustomDialogFragment

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class CustomFragment: BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCustomBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        init()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_open_dialog -> {
                CustomDialogFragment.newInstance(3).show(fragmentManager!!, "")
            }
        }
    }

    private fun init(){
        binding.clickListener = this
    }
}