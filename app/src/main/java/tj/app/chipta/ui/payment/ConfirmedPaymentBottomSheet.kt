import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tj.app.chipta.databinding.BottomSheetConfirmedPaymentBinding

class ConfirmedPaymentBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetConfirmedPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetConfirmedPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()
        val amount = args.getString("amount") ?: "0 TJS"
        val serviceName = args.getString("serviceName") ?: ""
        val dateTime = args.getString("dateTime") ?: ""
        val ticketCount = args.getInt("ticketCount", 0)
        val commission = args.getString("commission") ?: "0%"
        val paymentMethod = args.getString("paymentMethod") ?: ""

        binding.paymentSumText.text = "$amount TJS"
        binding.serviceNameTitle.text = serviceName
        binding.dateTimeTitle.text = dateTime
        binding.ticketCountText.text = "$ticketCount шт"
        binding.commissionTitle.text = commission
        binding.paymentTitle.text = paymentMethod

        binding.mainActivityBtn.setOnClickListener {
            dismiss()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}