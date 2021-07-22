import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.BaseUIKitComposable
import androidx.compose.ui.platform.UIViewControllerConvertible

@ThreadLocal
val IosApp: UIViewControllerConvertible<*> = IosAppImpl()

private class IosAppImpl: BaseUIKitComposable() {
    @Composable
    override fun Content() {
        ComposeNativeExample.HelloWorld()
    }
}