package de.sharknoon.placepixelhobby.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import de.sharknoon.placepixelhobby.R

class AtlasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atlas, container, false)
    }

    override fun onResume() {
        super.onResume()
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val a = requireActivity()
        val webView = a.findViewById<WebView>(R.id.web_view_fragment_atlas)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://draemm.li/various/place-atlas/")
    }

    companion object {
        private val self = AtlasFragment()
        fun getInstance() = self
    }
}
