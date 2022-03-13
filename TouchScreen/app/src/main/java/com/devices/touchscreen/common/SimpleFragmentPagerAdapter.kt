package com.devices.touchscreen.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class SimpleFragmentPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: List<CharSequence>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    init {
        require(!(titles != null && titles.size != fragments.size)) {
            "Fragments and titles list size must match!"
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles?.get(position)

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }
}