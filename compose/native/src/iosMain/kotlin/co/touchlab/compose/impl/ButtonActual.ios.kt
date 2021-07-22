package co.touchlab.compose.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.BaseUIKitComposable
import androidx.compose.ui.platform.SubComposable
import androidx.compose.ui.platform.UIKitController
import androidx.compose.ui.platform.UIKitConvertible
import androidx.compose.ui.platform.UIKitView
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIColor
import platform.UIKit.UIControl
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.autoresizingMask
import platform.UIKit.overrideUserInterfaceStyle
import platform.objc.sel_registerName
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

@Composable
actual fun ButtonActual(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    SubComposable(
        viewFactory = {
            onWillAddSubview {
                it.userInteractionEnabled = false
            }
            createView {
                Control(onClick)
            }
        },
        modifier = modifier,
        viewUpdate = { control ->
            control.onClick = onClick
        },
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            content()
        }
    }
}

private class Control(var onClick: () -> Unit): UIControl(CGRectZero.readValue()) {
    private val clickedPointer = sel_registerName("clicked")

    init {
        addTarget(this, clickedPointer, UIControlEventTouchUpInside)
    }

    @ObjCAction
    fun clicked() {
        println("I've been clicked")
        onClick()
    }
}