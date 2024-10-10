package com.juri.kolo_android.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
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

        wbView = binding.paystackCheckoutWebview
        wbView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        wbView.webViewClient = MyWebViewClient()
        wbView.loadUrl(navArgs.authUrl)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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

    inner class MyWebViewClient : WebViewClient() {

        private fun checkInternetConnection(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url?.scheme == "http" || request?.url?.scheme == "https") {
                // Open external links in a browser
                view?.loadUrl(request.url.toString())
                return true
            }
            return false
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            Toast.makeText(requireContext(), error?.description.toString(), Toast.LENGTH_SHORT)
                .show()
        }
    }

}