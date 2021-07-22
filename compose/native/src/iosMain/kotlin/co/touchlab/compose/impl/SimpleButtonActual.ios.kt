package co.touchlab.compose.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UIKitView
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControl
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.objc.sel_registerName

@Composable
actual fun SimpleButtonActual(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit,
) {
    UIKitView(
        factory = { UIControlWrapper(UIButton().apply {
            setTitleColor(UIColor.blackColor, UIControlStateNormal)
        }) },
        modifier = modifier,
        update = { wrapper ->
            wrapper.updateOnClick(onClick)
            wrapper.control.setTitle(title, UIControlStateNormal)
        }
    )
}

private class UIControlWrapper<T: UIControl>(val control: T): UIView(CGRectZero.readValue()) {
    private val clickedPointer = sel_registerName("clicked")
    private var onClick: () -> Unit = { }

    init {
        addSubview(control)
        control.translatesAutoresizingMaskIntoConstraints = false
        listOf(
            control.leadingAnchor.constraintEqualToAnchor(leadingAnchor),
            control.topAnchor.constraintEqualToAnchor(topAnchor),
            control.trailingAnchor.constraintEqualToAnchor(trailingAnchor),
            control.bottomAnchor.constraintEqualToAnchor(bottomAnchor),
        ).forEach {
            it.active = true
        }
    }

    fun updateOnClick(onClick: () -> Unit) {
        this.onClick = onClick

        if (!control.allTargets.contains(this)) {
            control.addTarget(this, clickedPointer, UIControlEventTouchUpInside)
        }
    }

    @ObjCAction
    fun clicked() {
        onClick()
    }
}
