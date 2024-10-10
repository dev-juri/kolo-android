package com.juri.kolo_android.presentation.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.juri.kolo_android.R
import com.juri.kolo_android.databinding.FragmentCheckoutBinding
import com.juri.kolo_android.utils.viewBinding

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private val binding by viewBinding(FragmentCheckoutBinding::bind)

    private val navArgs by navArgs<CheckoutFragmentArgs>()

    private lateinit var wbView: WebView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCheckout(navArgs.authUrl)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (wbView.canGoBack()) {
                        wbView.goBack() // Navigate back in the WebView's history
                    } else {
                        // No more history in WebView, close the fragment
                        findNavController().popBackStack()
                    }
                }
            })

    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun loadCheckout(authorizationUrl: String) {
        val webView: WebView = binding.paystackCheckoutWebview
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url: Uri? = request?.url

                if (url?.toString()?.contains("callback", ignoreCase = false) == true) {
                    findNavController().navigateUp()
                    return true
                }
                if (url?.toString()
                        ?.contains("cancel", ignoreCase = false) == true || url?.toString()
                        ?.contains("close", ignoreCase = false) == true
                ) {
                    findNavController().navigateUp()
                    return true
                }

                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        webView.loadUrl(authorizationUrl)
    }


}