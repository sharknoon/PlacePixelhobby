package de.sharknoon.placepixelhobby.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.fragments.CountColorsFragment
import de.sharknoon.placepixelhobby.fragments.HistoryFragment
import de.sharknoon.placepixelhobby.fragments.ImagesFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigationBar()
        initDrawer()
    }

    /**
     * Initializes the bottom bottom_navigation_bar_menu bar
     */
    private fun initBottomNavigationBar() {
        //Handles the click on the bottom_navigation_bar_menu bar
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.navigation_images -> {
                    //title = "${getString(R.string.app_name)} - ${getString(R.string.images)}"
                    openFragment(ImagesFragment.getInstance())
                    true
                }
                R.id.navigation_history -> {
                    //title = "${getString(R.string.app_name)} - ${getString(R.string.history)}"
                    openFragment(HistoryFragment.getInstance())
                    true
                }
                R.id.navigation_counter -> {
                    //title = "${getString(R.string.app_name)} - ${getString(R.string.count_colors)}"
                    openFragment(CountColorsFragment.getInstance())
                    true
                }
                else -> false
            }
        }
        //Clicks the first item on the bottom_navigation_bar_menu bar at the start of the app
        bottom_navigation.selectedItemId = R.id.navigation_images
    }

    /**
     * Changes a Fragment of the bottom bottom_navigation_bar_menu bar
     */
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
                    Toast.makeText(applicationContext, "Kommt bald", Toast.LENGTH_LONG).show()
                    //val intent = Intent(this, SettingsActivity::class.java)
                    //startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
//                R.id.settings -> {
//                    Toast.makeText(applicationContext, "Settings", Toast.LENGTH_LONG).show()
//                    //val intent = Intent(this, AboutActivity::class.java)
//                    //startActivity(intent)
//                    drawerLayout.closeDrawers()
//                    true
//                }
//                R.id.mycart -> {
//                    Toast.makeText(applicationContext, "My Cart", Toast.LENGTH_LONG).show()
//                    //val intent = Intent(this, AboutActivity::class.java)
//                    //startActivity(intent)
//                    drawerLayout.closeDrawers()
//                    true
//                }
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
