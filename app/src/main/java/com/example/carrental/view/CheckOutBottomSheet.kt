package com.example.carrental.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrental.databinding.CheckoutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


//class CheckOutBottomSheet : BottomSheetDialogFragment() {
//
//    private var binding: CheckoutBottomSheetBinding? = null

//    private var selectedAddressText: String? = null
//
//    private val userViewModel : UserViewModel by viewModels()
//
//    private  lateinit var  deliveryAddressAdapter: DeliveryAddressAdapter
//    private  lateinit var  driveThroughAddressAdapter: DeliveryAddressAdapter
//    private  lateinit var  storePickUpAddressAdapter: DeliveryAddressAdapter
//    private  lateinit var  curbPickUpAddressAdapter: DeliveryAddressAdapter
//
//    var onAddressSelected: ((AddressData) -> Unit)? = null // Callback to send selected address




//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {

//        binding =  CheckoutBottomSheetBinding.inflate(inflater, container, false)
//
//        // Retrieve the selected address text from the fragment arguments
//        selectedAddressText = arguments?.getString("selectedAddress")
//
//
//        binding?.recVHomeDelivery?.layoutManager = LinearLayoutManager(requireContext(),
//            LinearLayoutManager.VERTICAL,false)
//        deliveryAddressAdapter = DeliveryAddressAdapter(arrayListOf(),{ address ->
//            onAddressSelected?.invoke(address)
//            dismiss()
//        })
//        binding!!.recVHomeDelivery.adapter = deliveryAddressAdapter
//
//
//        binding?.recVDriveThrough?.layoutManager = LinearLayoutManager(requireContext(),
//            LinearLayoutManager.VERTICAL,false)
//        driveThroughAddressAdapter = DeliveryAddressAdapter(arrayListOf(),{ address ->
//            onAddressSelected?.invoke(address)
//            dismiss()
//        })
//        binding!!.recVDriveThrough.adapter = driveThroughAddressAdapter
//
//
//        binding?.recVStorePickUp?.layoutManager = LinearLayoutManager(requireContext(),
//            LinearLayoutManager.VERTICAL,false)
//        storePickUpAddressAdapter = DeliveryAddressAdapter(arrayListOf(),{ address ->
//            onAddressSelected?.invoke(address)
//            dismiss()
//        })
//        binding!!.recVStorePickUp.adapter = storePickUpAddressAdapter
//
//
//        binding?.recVCurbPickUp?.layoutManager = LinearLayoutManager(requireContext(),
//            LinearLayoutManager.VERTICAL,false)
//        curbPickUpAddressAdapter = DeliveryAddressAdapter(arrayListOf(),{ address ->
//            onAddressSelected?.invoke(address)
//            dismiss()
//        })
//        binding!!.recVCurbPickUp.adapter = curbPickUpAddressAdapter
//
//
//        userViewModel.address.observe(viewLifecycleOwner, Observer { data->
//            if (data.size >= 2) { // Ensure at least 2 items are available
//                val shuffledData = data.shuffled() // Shuffle the list randomly
//
//                fun getSafeSubList(startIndex: Int, count: Int): List<AddressData> {
//                    return shuffledData.drop(startIndex).take(count) // Safely get elements
//                }
//
//                deliveryAddressAdapter.updateAddressData(getSafeSubList(0, 2))
//                driveThroughAddressAdapter.updateAddressData(getSafeSubList(2, 2))
//                storePickUpAddressAdapter.updateAddressData(getSafeSubList(1, 2))
//                curbPickUpAddressAdapter.updateAddressData(getSafeSubList(3, 2))
//            }
//        })
//
//
//
//        binding?.btnHomeAddressAdd?.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString("deliveryType", "Home Delivery")
//            }
//            dismiss()
//            findNavController().navigate(R.id.pickUpAddressAddFragment, bundle)
//        }
//
//        binding?.btnDriveThroughAddressAdd?.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString("deliveryType", "Drive Through")
//            }
//            dismiss()
//            findNavController().navigate(R.id.pickUpAddressAddFragment, bundle)
//        }
//
//        binding?.btnStorePickUpAddressAdd?.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString("deliveryType", "Store Pickup")
//            }
//            dismiss()
//            findNavController().navigate(R.id.pickUpAddressAddFragment, bundle)
//        }
//
//
//        binding?.btnCurbPickUpAddressAdd?.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString("deliveryType", "Curb Pickup")
//            }
//            dismiss()
//            findNavController().navigate(R.id.pickUpAddressAddFragment, bundle)
//        }
//
//        return binding!!.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        val behavior = BottomSheetBehavior.from(bottomSheet!!)
//
//        // Set the initial height to default
//        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
//
//        setupToggleVisibility(behavior)
//    }
//
//
//    private fun setupToggleVisibility(behavior: BottomSheetBehavior<View>) {
//        // For Home Delivery
//        binding?.ivHomeDelivery?.setOnClickListener {
//            toggleVisibility(binding?.llHomeAddress, binding?.ivHomeDelivery)
//            expandBottomSheet(behavior)
//        }
//
//        // For Drive Through
//        binding?.ivDriveThrough?.setOnClickListener {
//            toggleVisibility(binding?.llDriveThroughAddress, binding?.ivDriveThrough)
//            expandBottomSheet(behavior)
//        }
//
//        // For Store Pickup
//        binding?.ivStorePickup?.setOnClickListener {
//            toggleVisibility(binding?.llStoreAddress, binding?.ivStorePickup)
//            expandBottomSheet(behavior)
//        }
//
//        // For Curb Pickup
//        binding?.ivCurbPickup?.setOnClickListener {
//            toggleVisibility(binding?.llCurbAddress, binding?.ivCurbPickup)
//            expandBottomSheet(behavior)
//        }
//    }
//
//    private fun toggleVisibility(selectedLayout: LinearLayout?, selectedImageView: ImageView?) {
//        if (selectedLayout?.visibility == View.VISIBLE) {
//            // If the selected layout is already visible, hide it
//            selectedLayout.visibility = View.GONE
//            selectedImageView?.rotation = 270f // Reset rotation
//        } else {
//            // Hide all other layouts and reset their images
//            val allLayouts = listOf(
//                binding?.llHomeAddress,
//                binding?.llDriveThroughAddress,
//                binding?.llStoreAddress,
//                binding?.llCurbAddress
//            )
//
//            val allImageViews = listOf(
//                binding?.ivHomeDelivery,
//                binding?.ivDriveThrough,
//                binding?.ivStorePickup,
//                binding?.ivCurbPickup
//            )
//
//            allLayouts.forEach { it?.visibility = View.GONE }
//            allImageViews.forEach { it?.rotation = 270f }
//
//            // Show only the selected layout
//            selectedLayout?.visibility = View.VISIBLE
//            selectedImageView?.rotation = -270f
//        }
//    }
//
//    private fun expandBottomSheet(behavior: BottomSheetBehavior<View>) {
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED // Expand fully
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//
//        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog = dialogInterface as BottomSheetDialog
//            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//
//            bottomSheet?.let {
//                val layoutParams = it.layoutParams
//                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                it.layoutParams = layoutParams
//                it.elevation = resources.getDimension(R.dimen.close_icon_elevation)
//            }
//
////            // Add a close button to the dialog window
//            addCloseButtonToDialog(bottomSheetDialog)
//        }
//
//        return dialog
//    }
//
//    private fun addCloseButtonToDialog(bottomSheetDialog: BottomSheetDialog) {
//        val closeButton = ImageView(requireContext()).apply {
//            setImageResource(R.drawable.close) // Your close icon in drawables
//
//            // Convert 24dp to pixels
//            val sizeInPx = resources.getDimensionPixelSize(R.dimen.close_icon_size)
//
//            layoutParams = ViewGroup.LayoutParams(sizeInPx, sizeInPx) // Set width and height
//            setOnClickListener { dismiss() } // Close the BottomSheet
//        }
//
//        val parentView = bottomSheetDialog.window?.decorView as? FrameLayout ?: return
//        val closeContainer = FrameLayout(requireContext()).apply {
//            layoutParams = FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL // Position at top-right
//                topMargin = resources.getDimensionPixelSize(R.dimen.close_icon_top_margin) // Adjust top margin
//                elevation = resources.getDimension(R.dimen.close_icon_elevation)
//            }
//            addView(closeButton)
//        }
//
//        parentView.addView(closeContainer)
//    }
//
//
//
//
//
//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//
//        // When the BottomSheet is dismissed, pass the selected address text to the parent fragment
//        (parentFragment as? PaymentFragment)?.apply {
//            selectedAddressText = this@CheckOutBottomSheet.selectedAddressText
//        }
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null // Avoid memory leaks
//    }
