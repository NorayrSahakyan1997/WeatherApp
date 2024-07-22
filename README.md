package com.samsung.android.app.shealth.tracker.myheart.util

import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.graphics.Typeface
import android.text.style.StyleSpan
import com.samsung.android.app.shealth.app.helper.ContextHolder
import com.samsung.android.app.shealth.tracker.sleep.util.Utils


object MyHearthChartTextFormatUtils {
    /**

    Creates a styled SpannableString for the artery health score summary text.

    @param healthResult The input string containing the health result text.

    @param scoreValueSizeInSp The font size for the score value in SP.

    @param unitSizeInSp The font size for the unit in SP.

    @return A SpannableString with the score value and unit styled appropriately.
     */
    fun getArteryHealthScoreSummaryText(
        healthResult: String,
        scoreValueSizeInSp: Int,
        unitSizeInSp: Int
    ): SpannableString {
        val context = ContextHolder.getContext() ?: return SpannableString("")

        val sizeForValue = Utils.convertSpToPx(context, scoreValueSizeInSp, true).toInt()
        val sizeForUnit = Utils.convertSpToPx(context, unitSizeInSp).toInt()

        val numberIndex = healthResult.indexOfFirst { it.isDigit() }
        if (numberIndex == -1) {
            return SpannableString(healthResult)
        }
        val spannableString = SpannableString(healthResult)
        val numberEndIndex =
            healthResult.indexOfFirst { !it.isDigit() }.takeIf { it != -1 } ?: healthResult.length

        spannableString.apply {
            setSpan(
                AbsoluteSizeSpan(sizeForValue),
                numberIndex,
                numberEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (numberEndIndex < healthResult.length) {
                setSpan(
                    AbsoluteSizeSpan(sizeForUnit),
                    numberEndIndex,
                    healthResult.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannableString
    }

    /**

    Creates a styled SpannableString for the artery health state text.

    @param healthResult The input string containing the health result text.

    @param unitSizeInSp The font size for the unit in SP.

    @return A SpannableString with the health state text styled appropriately.
     */
    fun getArteryHealthStateText(healthResult: String, unitSizeInSp: Int): SpannableString {
        val context = ContextHolder.getContext() ?: return SpannableString("")

        val sizeForUnit = Utils.convertSpToPx(context, unitSizeInSp).toInt()

        val numberIndex = healthResult.indexOfFirst { it.isDigit() }
        if (numberIndex == -1) {
            return SpannableString(healthResult)
        }

        val numberEndIndex = healthResult.indexOfFirst { !it.isDigit() }.takeIf { it > numberIndex }
            ?: healthResult.length

        val spannableString = SpannableString(healthResult)

        spannableString.apply {
            setSpan(
                AbsoluteSizeSpan(sizeForUnit),
                numberIndex,
                numberEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD),
                numberIndex,
                numberEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }

}
