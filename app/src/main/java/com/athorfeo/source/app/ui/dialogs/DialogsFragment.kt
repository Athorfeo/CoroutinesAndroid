package com.athorfeo.source.app.ui.dialogs
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.athorfeo.source.BuildConfig
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentDashboardBinding
import com.athorfeo.source.databinding.FragmentDialogsBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ui.DialogHelper

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class DialogsFragment: BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentDialogsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialogs, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickListener = this
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_normal -> {
                activity?.let{
                    DialogHelper.normal(it)?.apply {
                        setTitle("Información")
                        setMessage("El diálogo se ha mostrado correctamente")
                        show()
                    }
                }
            }

            R.id.button_camerax -> {
            }

            R.id.button_custom -> {
            }
        }
    }
}