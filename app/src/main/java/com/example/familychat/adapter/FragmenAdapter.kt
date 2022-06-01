package com.example.familychat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.familychat.fragment.StatusFragment
import com.example.familychat.fragment.callFragment
import com.example.familychat.fragment.chatFragment
import javax.net.ssl.SSLEngineResult

class FragmenAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return chatFragment()
            1 -> return StatusFragment()
            2 -> return callFragment()
            else -> return chatFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String =""
        if(position == 0){
            title = "CHAT"
        }
        if(position == 1){
            title = "STATUS"
        }
        if(position == 2){
            title = "CALLS "
        }
        return title
    }
}