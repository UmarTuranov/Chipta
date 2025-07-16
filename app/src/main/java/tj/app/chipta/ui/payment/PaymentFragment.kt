package tj.app.chipta.ui.payment

import ConfirmedPaymentBottomSheet
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tj.app.chipta.R
import tj.app.chipta.databinding.FragmentPaymentBinding
import tj.app.chipta.databinding.FragmentSeatSelectionBinding

class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() = with(binding) {
        val totalPrice = arguments?.getInt("totalPrice") ?: 0
        val ticketCount = arguments?.getInt("ticketCount") ?: 0

        paymentSumEditText.text = "$totalPrice TJS".toEditable()
        ticketCountText.text = "За $ticketCount билета"

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        payButton.setOnClickListener {
            val bottomSheet = ConfirmedPaymentBottomSheet()

            val args = Bundle().apply {
                putString("amount", totalPrice.toString())
                putString("serviceName", "Билеты")
                putString("dateTime", "16.07.2025 | 14:00")
                putInt("ticketCount", ticketCount)
                putString("commission", "0%")
                putString("paymentMethod", "Кошелёк")
            }

            bottomSheet.arguments = args
            bottomSheet.show(parentFragmentManager, "ConfirmedPaymentBottomSheet")
        }
    }

    private fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }
}