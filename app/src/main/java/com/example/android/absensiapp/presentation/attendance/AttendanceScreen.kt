package com.example.android.absensiapp.presentation.attendance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.R
import com.example.android.absensiapp.presentation.component.ButtonCommon
import com.example.android.absensiapp.presentation.component.CardDateAndTime
import com.example.android.absensiapp.ui.theme.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@ExperimentalMaterialApi
@Composable
fun BottomSheetAttendance(
    modifier: Modifier = Modifier,
    onClickCheckIn: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        modifier = modifier,
        sheetShape = RoundedCornerShape(
            topStart = DIMENS_12dp, topEnd = DIMENS_12dp
        ),
        scaffoldState = scaffoldState,
        sheetElevation = DIMENS_8dp,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_my_location),
                    contentDescription = stringResource(
                        id = R.string.icon_my_location
                    ),
                )
            }
        },
        sheetContent = {
            BottomSheetAttendanceContent(
                onClickCheckIn = { onClickCheckIn.invoke() }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                GoogleMapContent()

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = DIMENS_24dp),
                    shape = RoundedCornerShape(DIMENS_12dp),
                    color = Color.Transparent
                ) {
                    CardDateAndTime()
                }
            }
        }
    )
}

@Composable
fun BottomSheetAttendanceContent(
    modifier: Modifier = Modifier,
    onClickCheckIn: () -> Unit
) {
    Column(
        modifier = modifier
            .background(BgColor1)
            .fillMaxWidth()
            .padding(top = DIMENS_18dp, bottom = DIMENS_18dp)
    ) {
        Divider(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(DIMENS_4dp)
                .width(DIMENS_80dp)
                .shadow(shape = RoundedCornerShape(DIMENS_8dp), elevation = DIMENS_2dp),
            color = TextColorSecond,
        )

        Spacer(modifier = Modifier.height(DIMENS_12dp))

        Text(
            modifier = Modifier.padding(start = DIMENS_16dp),
            text = stringResource(id = R.string.current_location),
            fontSize = TEXT_SIZE_12sp,
            fontWeight = FontWeight.Bold,
            color = TextColorPrimary
        )

        Spacer(modifier = Modifier.height(DIMENS_10dp))

        Row(
            modifier = Modifier.padding(start = DIMENS_16dp, end = DIMENS_16dp),
        ) {
            Image(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.image_location),
                colorFilter = ColorFilter.tint(color = TextColorPrimary)
            )

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.sample_location),
                fontSize = TEXT_SIZE_10sp,
                fontWeight = FontWeight.Medium,
                color = TextColorPrimary
            )
        }

        Spacer(modifier = Modifier.height(DIMENS_12dp))

        Text(
            modifier = Modifier.padding(start = DIMENS_16dp),
            text = stringResource(id = R.string.your_photo),
            fontSize = TEXT_SIZE_12sp,
            fontWeight = FontWeight.Bold,
            color = TextColorPrimary
        )

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DIMENS_16dp, end = DIMENS_16dp),
            backgroundColor = BgColor4,
            border = BorderStroke(width = DIMENS_1dp, color = TextColorSecond),
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(DIMENS_12dp)
                    .size(DIMENS_24dp),
                imageVector = Icons.Default.AddCircle,
                contentDescription = stringResource(R.string.image_add_photo),
                colorFilter = ColorFilter.tint(color = SecondaryColor)
            )
        }

        Spacer(modifier = Modifier.height(DIMENS_12dp))

        ButtonCommon(
            backgroundColor = PrimaryColor,
            title = stringResource(id = R.string.check_in)
        ) {
            onClickCheckIn.invoke()
        }
    }
}

@Composable
fun GoogleMapContent(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.google_map_style_json
                )
            )
        )
    }
    val stmikBaniSaleh = LatLng(-6.252988600385395, 107.00312353736732)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(stmikBaniSaleh, 20f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings.copy(zoomControlsEnabled = false),
        properties = properties
    ) {
        Marker(
            state = MarkerState(position = stmikBaniSaleh),
            title = "STMIK Bani Saleh",
            snippet = "Marker in STMIK Bani Saleh"
        )
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun BottomSheetAttendancePreview() {
    BottomSheetAttendance(
        onClickCheckIn = {}
    )
}