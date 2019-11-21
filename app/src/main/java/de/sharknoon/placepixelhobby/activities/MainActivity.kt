package de.sharknoon.placepixelhobby.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.fragments.*
import de.sharknoon.placepixelhobby.utils.replaceChildFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        initBottomNavigationBar()
        initDrawer()
    }

    private var selectedTab = R.id.navigation_images

    /**
     * Initializes the bottom bottom_navigation_bar_menu bar
     */
    private fun initBottomNavigationBar() {
        //Handles the click on the bottom_navigation_bar_menu bar
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            selectedTab = item.itemId
            when (item.itemId) {
                R.id.navigation_images -> {
                    replaceChildFragment(
                        R.id.frame_layout_fragment_count_colors_container,
                        ImagesFragment.getInstance()
                    )
                    true
                }
                R.id.navigation_place -> {
                    replaceChildFragment(
                        R.id.frame_layout_fragment_count_colors_container,
                        PlaceFragment.getInstance()
                    )
                    true
                }
                R.id.navigation_history -> {
                    replaceChildFragment(
                        R.id.frame_layout_fragment_count_colors_container,
                        HistoryFragment.getInstance()
                    )
                    true
                }
                R.id.navigation_atlas -> {
                    replaceChildFragment(
                        R.id.frame_layout_fragment_count_colors_container,
                        AtlasFragment.getInstance()
                    )
                    true
                }
                R.id.navigation_counter -> {
                    replaceChildFragment(
                        R.id.frame_layout_fragment_count_colors_container,
                        CountColorsFragment.getInstance()
                    )
                    true
                }
                else -> false
            }
        }
        //Clicks the first item on the bottom_navigation_bar_menu bar at the start of the app
        bottom_navigation.selectedItemId = selectedTab
    }

    private fun initDrawer() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.activity_main)
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.drawer)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.account -> {
                    val intent = Intent(this, SourcesActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

        toggle = actionBarDrawerToggle
    }

    private var toggle: ActionBarDrawerToggle? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle?.onOptionsItemSelected(item) == true) true else super.onOptionsItemSelected(
            item
        )
    }


}
