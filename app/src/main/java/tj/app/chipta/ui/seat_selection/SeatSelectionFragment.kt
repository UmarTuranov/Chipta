package tj.app.chipta.ui.seat_selection

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tj.app.chipta.R
import tj.app.chipta.databinding.FragmentSeatSelectionBinding
import tj.app.chipta.model.CinemaSessionModel
import tj.app.chipta.model.SeatModel
import tj.app.chipta.model.TicketTypeModel
import tj.app.chipta.ui.payment.PaymentFragment

class SeatSelectionFragment : Fragment() {

    private lateinit var binding: FragmentSeatSelectionBinding
    private lateinit var viewModel: SeatSelectionViewModel

    private var standardPrice: Int = 0
    private var comfortPrice: Int = 0
    private var vipPrice: Int = 0
    private var selectedSeats = ArrayList<SeatModel>()

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
        binding.zoomLayout.setHasClickableChildren(true)
    }

    private fun observeViewModel(){
        viewModel.sessionData.observe(viewLifecycleOwner, Observer { session ->
            session?.let {
                drawSeatMap(it)
                setupViews(session)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorPair ->
            errorPair?.let {
                showAlert(it.first, it.second)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(session: CinemaSessionModel) = with(binding){
        hallNameText.text = session.hallName ?: ""

        standardPrice = getPrice(session.seatsType ?: listOf(), "STANDARD") ?: 0
        comfortPrice = getPrice(session.seatsType ?: listOf(), "COMFORT") ?: 0
        vipPrice = getPrice(session.seatsType ?: listOf(), "VIP") ?: 0

        standardPriceText.text = "$standardPrice TJS"
        comfortPriceText.text = "$comfortPrice TJS"
        vipPriceText.text = "$vipPrice TJS"

        val freeSeatsCount = session.seats?.count { it.bookedSeats == 0 } ?: 0
        freeSeatsCountText.text = "Осталось мест: $freeSeatsCount"

        TransitionManager.beginDelayedTransition(binding.root)
    }

    private fun getPrice(ticketList: List<TicketTypeModel>, seatType: String): Int? {
        return ticketList.find { it.seatType == seatType }?.price?.div(100)
    }

    private fun drawSeatMap(session: CinemaSessionModel) = with(binding) {
        seatMapContainer.post {
            seatMapContainer.removeAllViews()

            val mapWidth = session.mapWidth ?: 1
            val mapHeight = session.mapHeight ?: 1

            val scaleFactor = 2f
            val pixelWidth = (mapWidth * scaleFactor).toInt()
            val pixelHeight = (mapHeight * scaleFactor).toInt()

            seatMapContainer.layoutParams = FrameLayout.LayoutParams(pixelWidth, pixelHeight)

            session.seats?.forEach { seat ->
                val left = (seat.left ?: 0) * scaleFactor
                val top = (seat.top ?: 0) * scaleFactor

                when (seat.objectType) {
                    "seat" -> {
                        val seatSize = 50

                        if (seat.bookedSeats == 1) {
                            val bookedView = ImageView(requireContext()).apply {
                                layoutParams = FrameLayout.LayoutParams(seatSize, seatSize)
                                x = left
                                y = top
                                setImageResource(R.drawable.ic_cancel)
                                imageTintList = ColorStateList.valueOf(Color.GRAY)
                            }
                            seatMapContainer.addView(bookedView)
                        } else {
                            val seatFrame = FrameLayout(requireContext()).apply {
                                layoutParams = FrameLayout.LayoutParams(seatSize, seatSize)
                                x = left
                                y = top
                            }

                            val seatIcon = ImageView(requireContext()).apply {
                                layoutParams = FrameLayout.LayoutParams(seatSize, seatSize)
                                setImageResource(R.drawable.ic_seat)
                                imageTintList = ColorStateList.valueOf(getSeatColor(seat.seatType))
                            }

                            val seatLabel = TextView(requireContext()).apply {
                                layoutParams = FrameLayout.LayoutParams(seatSize, seatSize)
                                text = seat.place ?: ""
                                textSize = 12F
                                gravity = Gravity.CENTER
                                setBackgroundResource(R.drawable.text_background)
                                setTextColor(Color.WHITE)
                                visibility = View.GONE
                            }

                            seatFrame.addView(seatIcon)
                            seatFrame.addView(seatLabel)

                            var isSelected = false

                            seatFrame.setOnClickListener {
                                isSelected = !isSelected
                                seatIcon.visibility = if (isSelected) View.GONE else View.VISIBLE
                                seatLabel.visibility = if (isSelected) View.VISIBLE else View.GONE

                                if (isSelected) {
                                    selectedSeats.add(seat)
                                } else {
                                    selectedSeats.remove(seat)
                                }
                                setupPaymentCard()
                            }

                            seatMapContainer.addView(seatFrame)
                        }
                    }

                    "label" -> {
                        val labelView = TextView(requireContext()).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                            )
                            x = left
                            y = top
                            text = seat.objectTitle ?: ""
                            textSize = 15F
                            setTextColor(Color.GRAY)
                        }
                        seatMapContainer.addView(labelView)
                    }
                }
            }
        }
    }

    private fun setupPaymentCard() = with(binding) {
        if (selectedSeats.isNotEmpty()) {
            if (paymentCard.visibility != View.VISIBLE) {
                paymentCard.visibility = View.INVISIBLE
                paymentCard.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        paymentCard.viewTreeObserver.removeOnPreDrawListener(this)
                        paymentCard.translationY = paymentCard.height.toFloat()
                        paymentCard.visibility = View.VISIBLE
                        paymentCard.animate()
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                        return true
                    }
                })
            }

            var totalPrice = 0
            var ticketCount = 0
            for (seat in selectedSeats) {
                ticketCount++
                totalPrice += when (seat.seatType) {
                    "STANDARD" -> standardPrice
                    "COMFORT" -> comfortPrice
                    "VIP" -> vipPrice
                    else -> 0
                }
            }
            totalPriceText.text = "$totalPrice TJS"
            ticketCountText.text = "За $ticketCount билета"
            TransitionManager.beginDelayedTransition(binding.root)

            paymentButton.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("totalPrice", totalPrice)
                    putInt("ticketCount", ticketCount)
                }

                val fragment = PaymentFragment()
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            if (paymentCard.visibility == View.VISIBLE) {
                paymentCard.animate()
                    .translationY(paymentCard.height.toFloat())
                    .setDuration(200)
                    .setInterpolator(AccelerateInterpolator())
                    .withEndAction {
                        paymentCard.visibility = View.GONE
                        paymentCard.translationY = 0f
                    }
                    .start()
            }
        }
    }

    private fun getSeatColor(type: String?): Int {
        return when (type) {
            "STANDARD" -> ContextCompat.getColor(requireContext(), R.color.green)
            "COMFORT" -> ContextCompat.getColor(requireContext(), R.color.orange)
            "VIP" -> ContextCompat.getColor(requireContext(), R.color.blue)
            else -> Color.GRAY
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