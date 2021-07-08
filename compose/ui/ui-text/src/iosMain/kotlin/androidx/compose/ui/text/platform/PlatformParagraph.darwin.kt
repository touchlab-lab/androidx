package androidx.compose.ui.text.platform

import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.Density

internal actual fun ActualParagraph(
        text: String,
        style: TextStyle,
        spanStyles: List<AnnotatedString.Range<SpanStyle>>,
        placeholders: List<AnnotatedString.Range<Placeholder>>,
        maxLines: Int,
        ellipsis: Boolean,
        width: Float,
        density: Density,
        resourceLoader: Font.ResourceLoader
): Paragraph = TODO()

internal actual fun ActualParagraph(
        paragraphIntrinsics: ParagraphIntrinsics,
        maxLines: Int,
        ellipsis: Boolean,
        width: Float
): Paragraph = TODO()


internal actual fun ActualParagraphIntrinsics(
        text: String,
        style: TextStyle,
        spanStyles: List<AnnotatedString.Range<SpanStyle>>,
        placeholders: List<AnnotatedString.Range<Placeholder>>,
        density: Density,
        resourceLoader: Font.ResourceLoader
): ParagraphIntrinsics = TODO()