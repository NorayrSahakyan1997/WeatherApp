
    private fun createGuideLines(): List<GuideLine> {
        val guideLines: MutableList<GuideLine> = ArrayList()
        val color = ContextCompat.getColor(context, R.color.common_color_sub_gray)
        guideLines.add(
            createGuideLine(
                MID_MY_HEART_VALUE,
                color,
                context.getString(R.string.map_result_steady)
            )
        )
        return guideLines
    }
