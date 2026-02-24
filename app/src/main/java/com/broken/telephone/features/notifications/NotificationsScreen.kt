package com.broken.telephone.features.notifications

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.R
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.core.utils.findActivity
import com.broken.telephone.core.utils.isPostNotificationsGranted
import com.broken.telephone.core.utils.openNotificationSettings
import com.broken.telephone.features.notifications.content.NotificationsContent
import com.broken.telephone.features.notifications.model.NotificationsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.checkPermission(isGranted)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.checkPermission(context.isPostNotificationsGranted())
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                NotificationsSideEffect.RequestPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        context.openNotificationSettings()
                    }
                }
                NotificationsSideEffect.OpenNotificationSettings -> {
                    context.openNotificationSettings()
                }
            }
        }
    }

    NotificationsContent(
        state = state,
        onBackClick = onBackClick,
        onNotificationPermissionClick = {
            val isGranted = context.isPostNotificationsGranted()
            val shouldShowRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.findActivity()?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                } == true
            } else false
            viewModel.onNotificationPermissionClick(isGranted, shouldShowRationale)
        },
        onAllNotificationsToggle = viewModel::onAllNotificationsToggle,
        onNotificationToggle = viewModel::onNotificationToggle,
        modifier = modifier,
    )

    if (state.showRationaleDialog) {
        ConfirmDialog(
            title = stringResource(R.string.notifications_dialog_rationale_title),
            body = stringResource(R.string.notifications_dialog_rationale_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.notifications_dialog_rationale_confirm),
            onDismiss = viewModel::onRationaleDismiss,
            onConfirm = viewModel::onRationaleConfirm,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
        )
    }
}
