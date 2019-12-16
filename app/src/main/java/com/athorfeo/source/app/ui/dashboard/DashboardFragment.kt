package com.athorfeo.source.app.ui.dashboard
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
import com.athorfeo.source.testing.OpenForTesting

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class DashboardFragment: BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickListener = this

        val versionText = "v${BuildConfig.VERSION_NAME}"
        binding.textSubtitle.text = versionText
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_main -> {
                val direction = DashboardFragmentDirections.actionDashboardFragmentToMainFragment()
                findNavController().navigate(direction)
            }

            R.id.button_camerax -> {
                val direction = DashboardFragmentDirections.actionDashboardFragmentToCameraXFragment()
                findNavController().navigate(direction)
            }

            R.id.button_dialogs -> {
                val direction = DashboardFragmentDirections.actionDashboardFragmentToDialogsFragment()
                findNavController().navigate(direction)
            }

            R.id.button_custom -> {
                val direction = DashboardFragmentDirections.actionDashboardFragmentToCustomFragment()
                findNavController().navigate(direction)
            }
        }
    }
}