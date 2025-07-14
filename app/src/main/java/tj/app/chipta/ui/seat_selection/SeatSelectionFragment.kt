package tj.app.chipta.ui.seat_selection

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tj.app.chipta.R
import tj.app.chipta.databinding.FragmentSeatSelectionBinding

class SeatSelectionFragment : Fragment() {

    private lateinit var binding: FragmentSeatSelectionBinding
    private lateinit var viewModel: SeatSelectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeatSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SeatSelectionViewModel::class.java]

        observeViewModel()
        viewModel.loadSession()
    }

    private fun observeViewModel() {
        viewModel.sessionData.observe(viewLifecycleOwner, Observer { session ->
            session?.let {
                drawSeatMap(it)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorPair ->
            errorPair?.let {
                showAlert(it.first, it.second)
            }
        })
    }

    private fun drawSeatMap(session: tj.app.chipta.model.CinemaSessionModel) = with(binding) {
        seatMapContainer.post {
            seatMapContainer.removeAllViews()

            val containerWidth = seatMapContainer.width.toFloat()
            val containerHeight = seatMapContainer.height.toFloat()

            val scaleX = containerWidth / (session.mapWidth ?: 1)
            val scaleY = containerHeight / (session.mapHeight ?: 1)

            session.seats?.forEach { seat ->
                val left = seat.left ?: 0
                val top = seat.top ?: 0
                when (seat.objectType) {
                    "seat" -> {
                        val seatView = ImageView(requireContext()).apply {
                            layoutParams = FrameLayout.LayoutParams(40, 40)
                            x = left * scaleX
                            y = top * scaleY
                            setImageResource(R.drawable.ic_android_black_24dp)
                            contentDescription = seat.objectDescription
                        }
                        seatMapContainer.addView(seatView)
                    }

                    "label" -> {
                        val labelView = TextView(requireContext()).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                            )
                            x = left * scaleX
                            y = top * scaleY
                            text = seat.objectTitle ?: ""
                            textSize = 12f
                            setTextColor(Color.GRAY)
                        }
                        seatMapContainer.addView(labelView)
                    }
                }
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        val buttonTitle = requireActivity().getString(R.string.alert_button_ok)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonTitle, null)
            .show()
    }
}