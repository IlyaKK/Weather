package com.elijah.weather.presentation.permissions_requests

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.elijah.weather.R
import com.elijah.weather.databinding.FragmentPermissionsRequestBinding
import com.google.android.material.snackbar.Snackbar

class PermissionRequestFragment : Fragment() {

    private val args by navArgs<PermissionRequestFragmentArgs>()

    private var _binding: FragmentPermissionsRequestBinding? = null
    private val binding: FragmentPermissionsRequestBinding
        get() = _binding ?: throw RuntimeException("FragmentPermissionsRequestBinding is null")

    private val locationRationalSnackBar: Snackbar by lazy {
        Snackbar.make(
            binding.frameLayout,
            R.string.location_permission_rationale,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.ok) {
                locationRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
    }

    private val backgroundLocationRationalSnackBar: Snackbar by lazy {
        Snackbar.make(
            binding.frameLayout,
            R.string.background_location_permission_rationale,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.ok) {
                locationBackgroundPermissionRequest.launch(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
    }

    private val locationBackgroundPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (!isGranted) {
                backgroundLocationRationalSnackBar.show()
            } else {
                completeRequestPermissions()
            }

        }

    private val locationRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] != true) {
                locationRationalSnackBar.show()
            } else {
                if (args.permissionType.contains(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    initialiseRequestBackgroundPermission()
                } else {
                    completeRequestPermissions()
                }
            }
        }

    private fun completeRequestPermissions() {
        findNavController().navigate(R.id.action_permissionsRequestFragment_to_sliderWeatherCitiesFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionsRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissions = args.permissionType
        val sizePermission = permissions.size
        if (sizePermission == 2) {
            initialiseRequestsAllPermissions()
        } else {
            if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                initialiseRequestFineLocationPermission()
            } else {
                initialiseRequestBackgroundPermission()
            }
        }
    }

    private fun initialiseRequestFineLocationPermission() {
        binding.detailsTextView.setText(R.string.location_access_rationale_details_text)
        binding.permissionRequestButton.setText(R.string.enable_fine_location_button_text)
        binding.permissionRequestButton.setOnClickListener {
            locationRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun initialiseRequestBackgroundPermission() {
        binding.detailsTextView.setText(R.string.background_location_access_rationale_details_text)
        binding.permissionRequestButton.setText(R.string.enable_background_location_button_text)
        binding.permissionRequestButton.setOnClickListener {
            locationBackgroundPermissionRequest.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    private fun initialiseRequestsAllPermissions() {
        binding.detailsTextView.setText(R.string.all_location_permission_rationale_details)
        binding.permissionRequestButton.setText(R.string.enable_all_permission_button_text)
        binding.permissionRequestButton.setOnClickListener {
            locationRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}