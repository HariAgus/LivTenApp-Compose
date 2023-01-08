package com.example.android.absensiapp.presentation.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.android.absensiapp.BuildConfig
import com.example.android.absensiapp.R
import com.example.android.absensiapp.databinding.FragmentProfileBinding
import com.example.android.absensiapp.dialog.MyDialog
import com.example.android.absensiapp.hawkstorage.HawkStorage
import com.example.android.absensiapp.model.LogoutResponse
import com.example.android.absensiapp.hawkstorage.networking.ApiServices
import com.example.android.absensiapp.presentation.changepass.ChangePasswordActivity
import com.example.android.absensiapp.presentation.login.LoginActivity
import com.example.android.absensiapp.presentation.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalMaterialApi
class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileScreen(
                    onClickChangePassword = {},
                    onClickChangeLanguage = {},
                    onClickLogout = {}
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("CheckResult")
    private fun updateView() {
        val user = HawkStorage.instance(context).getUser()
        val imageUrl = BuildConfig.BASE_IMAGE_URL + user.photo
        binding?.ivProfile?.let {
            Glide.with(requireContext()).load(imageUrl).placeholder(android.R.color.darker_gray)
                .into(
                    it
                )
        }
        binding?.tvNameProfile?.text = user.name
        binding?.tvEmailProfile?.text = user.email
    }

    private fun onClick() {
        binding?.btnChangePassword?.setOnClickListener {
            requireActivity().startActivity(Intent(context, ChangePasswordActivity::class.java))
        }

        binding?.btnChangeLanguage?.setOnClickListener {
            startActivity(Intent(ACTION_LOCALE_SETTINGS))
        }

        binding?.btnLogout?.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                    logoutRequest(dialog)
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun logoutRequest(dialog: DialogInterface) {
        val token = HawkStorage.instance(context).getToken()
        MyDialog.showProgressDialog(context)
        ApiServices.getLiveAttendanceServices()
            .logoutRequest("Bearer $token")
            .enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    dialog.dismiss()
                    MyDialog.hideDialog()
                    if (response.isSuccessful) {
                        HawkStorage.instance(context).deleteAll()
                        (activity as MainActivity).finishAffinity()
                        requireActivity().startActivity(Intent(context, LoginActivity::class.java))
                    } else {
                        MyDialog.dynamicDialog(
                            context,
                            getString(R.string.alert),
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    dialog.dismiss()
                    MyDialog.hideDialog()
                    MyDialog.dynamicDialog(
                        context,
                        getString(R.string.alert),
                        "Error : ${t.message}"
                    )
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}