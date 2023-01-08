package com.example.android.absensiapp.presentation.component

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android.absensiapp.R
import com.example.android.absensiapp.presentation.attendance.AttendanceFragment
import com.example.android.absensiapp.presentation.history.HistoryFragment
import com.example.android.absensiapp.presentation.profile.ProfileFragment
import com.example.android.absensiapp.ui.theme.BgColor4
import com.example.android.absensiapp.ui.theme.ColorInActiveBottomBar
import com.example.android.absensiapp.ui.theme.PrimaryColor

@ExperimentalMaterialApi
@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        HistoryFragment(),
        AttendanceFragment(),
        ProfileFragment()
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.id
    val bottomBarDestination = navigationItems.any { it.id == currentRoute }

    if (bottomBarDestination) {
        BottomNavigation(
            backgroundColor = BgColor4,
            contentColor = ColorInActiveBottomBar,
            modifier = modifier
        ) {
            navigationItems.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_attendance),
                            contentDescription = ""
                        )
                    },
                    selected = currentRoute == item.id,
                    selectedContentColor = PrimaryColor,
                    unselectedContentColor = ColorInActiveBottomBar,
                    onClick = { /*TODO*/ },
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {

}