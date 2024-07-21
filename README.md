
    fun getArteryHealthScoreSummeryText(
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

    fun getArteryHealthStateText(healthResult: String, unitSizeInSp: Int): SpannableString {
        val context = ContextHolder.getContext() ?: return SpannableString("")

        val sizeForUnit = Utils.convertSpToPx(context, unitSizeInSp).toInt()

        val numberIndex = healthResult.indexOfFirst { it.isDigit() }
        if (numberIndex == -1) {
            return SpannableString(healthResult)
        }

        val numberEndIndex =
            healthResult.indexOfFirst { !it.isDigit() }.takeIf { it > numberIndex }
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
