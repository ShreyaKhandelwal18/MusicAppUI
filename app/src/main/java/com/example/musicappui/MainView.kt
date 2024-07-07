package com.example.musicappui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(){
    val scaffoldState:ScaffoldState= rememberScaffoldState()
    val scope:CoroutineScope= rememberCoroutineScope()
     val viewModel:MainViewModel= viewModel()
    val isSheetfullScreen by remember {
        mutableStateOf(false)
    }

    val modifier=if(isSheetfullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    //allow us to find on which view we currently are
    val controller:NavController= rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute=navBackStackEntry?.destination?.route

    val dialogOpen= remember {
        mutableStateOf(false)

    }

    val currentScreen= remember {
        viewModel.currentScreen.value
    }

    val title=remember{
        //  change to current screen.title
        mutableStateOf(currentScreen.title)
    }

    val modalSheetState= rememberModalBottomSheetState(
        initialValue =ModalBottomSheetValue.Hidden,)

    val roundedCornerRadius = if(isSheetfullScreen) 0.dp else 12.dp

    val bottombar:@Composable ()->Unit={
        if(currentScreen is Screen.DrawerScreen ||currentScreen==Screen.BottomScreen.Home){
               BottomNavigation(modifier = Modifier.wrapContentSize()) {
                   screensInBottom.forEach{
                       item->
                       val isSelected= currentRoute==item.bRoute
                       Log.d("Navigation","Item:${item.btitle},Current Route: $currentRoute,Is Selected: $isSelected")
                       val tint=  if(isSelected) Color.White else Color.Black
                       BottomNavigationItem(
                           selected = currentRoute==item.bRoute,
                           onClick = { controller.navigate(item.bRoute)
                                     title.value=item.btitle
                                     },
                           icon = {

                               Icon(tint=tint,painter = painterResource(id = item.icon), contentDescription =item.btitle ) },

                           label ={ Text(text = item.btitle, color = tint)},
                           selectedContentColor = Color.White,
                           unselectedContentColor = Color.Black,


                           )
                   }

               }
        }

    }
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart =roundedCornerRadius, topEnd = roundedCornerRadius ),
        sheetContent = {
        MoreBottomSheet(modifier=modifier)
    }) {
        Scaffold(
            bottomBar = bottombar,

            topBar = {

                TopAppBar(title = { Text(text = title.value, color = Color.White, fontWeight = FontWeight.SemiBold) },
                    backgroundColor= colorResource(id = R.color.purple_200),

                    actions = {
                              IconButton(onClick = {
                                  scope.launch {
                                      if(modalSheetState.isVisible){
                                          modalSheetState.hide()
                                      }else{
                                          modalSheetState.show()
                                      }
                                  }
                              }) {
                                   Icon(imageVector = Icons.Default.MoreVert, contentDescription =null )
                              }
                    },
                    navigationIcon = { IconButton(onClick = {
                        // open drawer
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription ="Menu",modifier = Modifier.padding(end=8.dp) )
                    }}
                )
            },
            scaffoldState=scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)){
                    items(screensInDrawer){
                            item->
                        DrawerItem(selected = currentRoute==item.dRoute, item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if(item.dRoute=="add_account"){
                                //open dialog
                                dialogOpen.value=true
                            }else{
                                controller.navigate(item.dRoute)
                                title.value=item.dTitle
                            }

                        }
                    }
                }
            }


        ) {
            Navigation(navController = controller, viewModel =viewModel , pd =it )

            AccountDialog(dialogOpen = dialogOpen)
        }
    }


    
}

@Composable
fun DrawerItem(
    selected:Boolean,
    item:Screen.DrawerScreen,
    onDrawerItemClicked:() -> Unit
){
    val background=if (selected) Color.LightGray else Color.White
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 14.dp)
        .background(background)
        .clickable {
            onDrawerItemClicked()
        }
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription =item.dTitle,
            Modifier.padding(end = 8.dp,top=4.dp),
            tint = Color.Black

        )
        Text(
            text =item.dTitle,
            style=MaterialTheme.typography.h5,
            color = Color.Black
        )

    }
}


@Composable
fun Navigation(navController: NavController,viewModel: MainViewModel,pd:PaddingValues){
    NavHost(navController =navController as NavHostController,
        startDestination =Screen.DrawerScreen.Account.route ,
        modifier =Modifier.padding(pd)
    ){
       composable(Screen.DrawerScreen.Account.route){
           AccountView()
       }
        composable(Screen.DrawerScreen.Subscription.route){
            SubscriptionView()
        }
        composable(Screen.BottomScreen.Home.bRoute){
           HomeView()
        }
        composable(Screen.BottomScreen.Library.bRoute){
          Library()
        }
        composable(Screen.BottomScreen.Browse.bRoute){
            Browse()
        }

    }
}
@Composable
fun MoreBottomSheet(modifier: Modifier){
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colors.primarySurface)) {
       Column(modifier=Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
           Row(modifier=Modifier.padding(16.dp)){
               Icon(painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription ="Settings" )
               Text(text = "Settings", fontSize = 20.sp, color = Color.White)
           }
           Row(modifier=Modifier.padding(16.dp)){
               Icon(painter = painterResource(id = R.drawable.baseline_share_24), contentDescription ="Share" )
               Text(text = "Share", fontSize = 20.sp, color = Color.White)
           }
           Row(modifier=Modifier.padding(16.dp)){
               Icon(painter = painterResource(id = R.drawable.baseline_help_center_24), contentDescription ="Help" )
               Text(text = "Help", fontSize = 20.sp, color = Color.White)
           }
           
       }
    }
}
