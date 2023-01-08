package com.example.android.absensiapp.presentation.attendance

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.android.absensiapp.BuildConfig
import com.example.android.absensiapp.R
import com.example.android.absensiapp.databinding.BottomSheetAttendanceBinding
import com.example.android.absensiapp.databinding.FragmentAttendanceBinding
import com.example.android.absensiapp.date.MyDate
import com.example.android.absensiapp.dialog.MyDialog
import com.example.android.absensiapp.hawkstorage.HawkStorage
import com.example.android.absensiapp.hawkstorage.networking.ApiServices
import com.example.android.absensiapp.model.AttendanceResponse
import com.example.android.absensiapp.model.HistoryResponse
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalMaterialApi
class AttendanceFragment : Fragment(), OnMapReadyCallback {

    private val mapPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val cameraPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    //Config Maps
    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private var locationSettingRequest: LocationSettingsRequest? = null
    private var settingsClient: SettingsClient? = null
    private var mapAttendance: SupportMapFragment? = null
    private var map: GoogleMap? = null
    private var currentLocation: Location? = null
    private var locationCallback: LocationCallback? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    //UI
    private var bindingBottomSheet: BottomSheetAttendanceBinding? = null
    private var binding: FragmentAttendanceBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var currentPhotoPath = ""
    private var isCheckIn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        bindingBottomSheet = binding?.layoutBottomSheet
        return binding?.root*/

        return ComposeView(requireContext()).apply {
            setContent {
                BottomSheetAttendance(
                    onClickCheckIn = {}
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        bindingBottomSheet = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (currentLocation != null && locationCallback != null) {
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback!!)
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfAlreadyPresent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*setupMaps()
        init()
        onClick()*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (currentPhotoPath.isNotEmpty()) {
                    val uri = Uri.parse(currentPhotoPath)
                    bindingBottomSheet?.ivCapturePhoto?.setImageURI(uri)
                    bindingBottomSheet?.ivCapturePhoto?.adjustViewBounds = true
                }
            } else {
                if (currentPhotoPath.isNotEmpty()) {
                    val file = File(currentPhotoPath)
                    file.delete()
                    currentPhotoPath = ""
                }
            }
        }
    }

    private fun onClick() {
        binding?.fabGetCurrentLocation?.setOnClickListener {
            goToCurrentLocation()
        }

        bindingBottomSheet?.ivCapturePhoto?.setOnClickListener {
            if (checkPermissionCamera()) {
                openCamera()
            } else {
                setRequestPermissionCamera()
            }
        }

        bindingBottomSheet?.btnCheckIn?.setOnClickListener {
            val token = HawkStorage.instance(context).getToken()
            if (checkValidation()) {
                if (isCheckIn) {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.are_you_sure))
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            sendDataAttendance(token, "out")
                        }
                        .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.are_you_sure))
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            sendDataAttendance(token, "in")
                        }
                        .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    private fun sendDataAttendance(token: String, type: String) {
        val params = HashMap<String, RequestBody>()
        MyDialog.showProgressDialog(context)
        if (currentLocation != null && currentPhotoPath.isNotEmpty()) {
            val latitude = currentLocation?.latitude.toString()
            val longitude = currentLocation?.longitude.toString()
            val address = bindingBottomSheet?.tvCurrentLocation?.text.toString()

            val file = File(currentPhotoPath)
            val uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".fileprovider", file
            )
            val typeFile = context?.contentResolver?.getType(uri)

            val mediaTypeText = MultipartBody.FORM
            val mediaTypeFile = typeFile?.toMediaType()

            val requestLatitude = latitude.toRequestBody(mediaTypeText)
            val requestLongitude = longitude.toRequestBody(mediaTypeText)
            val requestAddress = address.toRequestBody(mediaTypeText)
            val requestType = type.toRequestBody(mediaTypeText)

            params["lat"] = requestLatitude
            params["long"] = requestLongitude
            params["address"] = requestAddress
            params["type"] = requestType

            val requestPhotoFile = file.asRequestBody(mediaTypeFile)
            val multipartBody =
                MultipartBody.Part.createFormData("photo", file.name, requestPhotoFile)
            ApiServices.getLiveAttendanceServices()
                .attend("Bearer $token", params, multipartBody)
                .enqueue(object : Callback<AttendanceResponse> {
                    override fun onResponse(
                        call: Call<AttendanceResponse>,
                        response: Response<AttendanceResponse>
                    ) {
                        MyDialog.hideDialog()
                        if (response.isSuccessful) {
                            val attendanceResponse = response.body()
                            currentPhotoPath = ""
                            bindingBottomSheet?.ivCapturePhoto?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.ic_baseline_add_circle_24
                                )
                            )
                            bindingBottomSheet?.ivCapturePhoto?.adjustViewBounds = false

                            if (type == "in") {
                                MyDialog.dynamicDialog(
                                    context,
                                    getString(R.string.success_check_in),
                                    attendanceResponse?.message.toString()
                                )

                            } else {
                                MyDialog.dynamicDialog(
                                    context,
                                    getString(R.string.success_check_out),
                                    attendanceResponse?.message.toString()
                                )
                            }
                            checkIfAlreadyPresent()
                        } else {
                            MyDialog.dynamicDialog(
                                context,
                                getString(R.string.alert),
                                getString(R.string.something_wrong)
                            )
                        }
                    }

                    override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                        Log.e(TAG, "Error : ${t.message}")
                    }
                })
        }
    }

    private fun checkIfAlreadyPresent() {
        val token = HawkStorage.instance(context).getToken()
        val currentDate = MyDate.getCurrentDateForServer()

        ApiServices.getLiveAttendanceServices()
            .getHistoryAttendance("Bearer $token", currentDate, currentDate)
            .enqueue(object : Callback<HistoryResponse> {
                override fun onResponse(
                    call: Call<HistoryResponse>,
                    response: Response<HistoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val histories = response.body()?.histories
                        if (histories != null && histories.isNotEmpty()) {
                            if (histories[0]?.status == 1) {
                                isCheckIn = false
                                checkIsCheckIn()
                                bindingBottomSheet?.btnCheckIn?.isEnabled = false
                                bindingBottomSheet?.btnCheckIn?.text =
                                    getString(R.string.your_already_present)
                            }
                        } else {
                            isCheckIn = true
                            checkIsCheckIn()
                        }
                    }
                }

                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                    Log.e(TAG, "Error : ${t.message}")
                }
            })
    }

    private fun checkIsCheckIn() {
        if (isCheckIn) {
            bindingBottomSheet?.btnCheckIn?.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_check_out)
            bindingBottomSheet?.btnCheckIn?.text = getString(R.string.check_out)
        } else {
            bindingBottomSheet?.btnCheckIn?.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_primary)
            bindingBottomSheet?.btnCheckIn?.text = getString(R.string.check_in)
        }
    }

    private fun checkValidation(): Boolean {
        if (currentPhotoPath.isNotEmpty()) {
            MyDialog.dynamicDialog(
                context,
                getString(R.string.alert),
                getString(R.string.please_take_your_photo)
            )
            return false
        }
        return true
    }

    private fun openCamera() {
        context?.let { context ->
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                val photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoUri = FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".fileprovider", it
                    )
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun init() {
        //Setup Location
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        settingsClient = LocationServices.getSettingsClient(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest()
            .setInterval(10000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        locationSettingRequest = builder.build()

        //Setup BottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bindingBottomSheet!!.bottomSheetAttendance)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_MAP_PERMISSIONS -> {
                var isHasPermission = false
                val permissionNotGranted = StringBuilder()

                for (i in permissions.indices) {
                    isHasPermission = grantResults[i] == PackageManager.PERMISSION_GRANTED

                    if (!isHasPermission) {
                        permissionNotGranted.append("${permissions[i]}\n")
                    }
                }

                if (isHasPermission) {
                    setupMaps()
                } else {
                    val message =
                        permissionNotGranted.toString() + "\n" + getString(R.string.not_granted)
                    MyDialog.dynamicDialog(
                        context,
                        getString(R.string.required_permission),
                        message
                    )
                }
            }
            REQUEST_CODE_CAMERA_PERMISSIONS -> {
                var isHasPermission = false
                val permissionNotGranted = StringBuilder()

                for (i in permissions.indices) {
                    isHasPermission = grantResults[i] == PackageManager.PERMISSION_GRANTED

                    if (!isHasPermission) {
                        permissionNotGranted.append("${permissions[i]}\n")
                    }
                }

                if (isHasPermission) {
                    openCamera()
                } else {
                    val message =
                        permissionNotGranted.toString() + "\n" + getString(R.string.not_granted)
                    MyDialog.dynamicDialog(
                        context,
                        getString(R.string.required_permission),
                        message
                    )
                }
            }
        }
    }

    private fun setupMaps() {
        mapAttendance =
            childFragmentManager.findFragmentById(R.id.map_attendance) as SupportMapFragment
        mapAttendance?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_map_style_json))
        if (checkPermission()) {
            val university = LatLng(-6.252261, 107.002559)
            map?.moveCamera(CameraUpdateFactory.newLatLng(university))
            map?.animateCamera(CameraUpdateFactory.zoomTo(20f))

            goToCurrentLocation()
        } else {
            setRequestPermission()
        }
    }

    private fun goToCurrentLocation() {
        bindingBottomSheet?.tvCurrentLocation?.text = getString(R.string.search_your_location)
        if (checkPermission()) {
            if (isLocationEnabled()) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = false

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        currentLocation = locationResult.lastLocation

                        if (currentLocation != null) {
                            val latitude = currentLocation?.latitude
                            val longitude = currentLocation?.longitude

                            if (latitude != null && longitude != null) {
                                val latLng = LatLng(latitude, longitude)
                                map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                                map?.animateCamera(CameraUpdateFactory.zoomTo(20F))

                                val address = getAddress(latitude, longitude)
                                if (address != null && address.isNotEmpty()) {
                                    bindingBottomSheet?.tvCurrentLocation?.text = address
                                }
                            }
                        }
                    }
                }
                fusedLocationProviderClient?.requestLocationUpdates(
                    locationRequest!!, locationCallback!!, Looper.myLooper()
                )
            } else {
                gotoTurnOnGps()
            }
        } else {
            setRequestPermission()
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String? {
        val result: String
        context?.let {
            val geocode = Geocoder(it, Locale.getDefault())
            val addresses = geocode.getFromLocation(latitude, longitude, 1)

            if (addresses!!.size > 0) {
                result = addresses[0].getAddressLine(0)
                return result
            }
        }
        return null
    }


    private fun gotoTurnOnGps() {
        settingsClient?.checkLocationSettings(locationSettingRequest!!)
            ?.addOnSuccessListener {
                goToCurrentLocation()
            }?.addOnFailureListener {
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvableApiException = it as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                requireActivity(), REQUEST_CODE_LOCATION
                            )
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                            Log.e(TAG, "Error: ${ex.message}")
                        }
                    }
                }
            }
    }

    private fun isLocationEnabled(): Boolean {
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!! ||
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!
        ) {
            return true
        }
        return false
    }

    private fun checkPermission(): Boolean {
        var isHasPermission = false
        context?.let {
            for (permission in mapPermissions) {
                isHasPermission = ActivityCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        return isHasPermission
    }

    private fun setRequestPermission() {
        requestPermissions(mapPermissions, REQUEST_CODE_MAP_PERMISSIONS)
    }

    private fun setRequestPermissionCamera() {
        requestPermissions(cameraPermissions, REQUEST_CODE_CAMERA_PERMISSIONS)
    }

    private fun checkPermissionCamera(): Boolean {
        var isHasPermission = false
        context?.let {
            for (permission in cameraPermissions) {
                isHasPermission = ActivityCompat.checkSelfPermission(it, permission) ==
                        PackageManager.PERMISSION_GRANTED
            }
        }
        return isHasPermission
    }

    companion object {
        private const val REQUEST_CODE_MAP_PERMISSIONS = 1000
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 1001
        private const val REQUEST_CODE_LOCATION = 2000
        private const val REQUEST_CODE_IMAGE_CAPTURE = 2001
        private val TAG = AttendanceFragment::class.java.simpleName
    }

}