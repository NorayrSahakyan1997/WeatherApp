class TrackerBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : HBarChart(context, attrs) {

    private var height = GRAPH_HEIGHT_DEFAULT
    private var heightSelected = GRAPH_HEIGHT_SELECTED
    private val selectedColorArray = intArrayOf(
        context.getColor(ArteryHeathChartDataMapping.WORSE.chartDotColor),
        context.getColor(ArteryHeathChartDataMapping.SLIGHTLY_WORSE.chartDotColor),
        context.getColor(ArteryHeathChartDataMapping.STEADY.chartDotColor),
        context.getColor(ArteryHeathChartDataMapping.SLIGHTLY_BETTER.chartDotColor),
        context.getColor(ArteryHeathChartDataMapping.BETTER.chartDotColor)
    )
    private val defaultColor = context.getColor(R.color.common_gray_scale_five)
    private var mGraph: HBarGraph? = null
    private val mDataList: MutableList<ChartData> = ArrayList()

    companion object {
        private const val GRAPH_HEIGHT_DEFAULT = 10f
        private const val GRAPH_HEIGHT_SELECTED = 14f

        private const val GRAPH_MARGIN_START = 0
        private const val GRAPH_MARGIN_TOP = 0
        private const val GRAPH_MARGIN_END = 0
        private const val GRAPH_MARGIN_BOTTOM = 0

        private const val GRAPH_PADDING_START = 0
        private const val GRAPH_PADDING_TOP = 0
        private const val GRAPH_PADDING_END = 0
        private const val GRAPH_PADDING_BOTTOM = 0

        private const val BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE = 0.02f

        private const val MIN_RANGE = 0f
        private const val MAX_RANGE = 5f

        private var PREV_ZONE_START = 3f
        private var PREV_ZONE_END = 4f
    }

    init {
        initGraph()
        setData()
    }

    fun setHeights(default: Float, selectedHeight: Float) {
        this.height = default
        this.heightSelected = selectedHeight
        setData()
    }

    private fun getIndex(range: FloatArray): Int {
        return if (range[0] == 0f && range[1] == 1f) 0
        else if (range[0] == 1f && range[1] == 2f) 1
        else if (range[0] == 2f && range[1] == 3f) 2
        else if (range[0] == 3f && range[1] == 4f) 3
        else 4
    }

    private fun initGraph() {
        axis.setDataRange(MIN_RANGE, MAX_RANGE)
        setGraphMargins(GRAPH_MARGIN_START, GRAPH_MARGIN_TOP, GRAPH_MARGIN_END, GRAPH_MARGIN_BOTTOM)
        setGraphPadding(
            GRAPH_PADDING_START,
            GRAPH_PADDING_TOP,
            GRAPH_PADDING_END,
            GRAPH_PADDING_BOTTOM
        )

        mGraph = HBarGraph(axis)
        graph = mGraph
    }

    private fun getNormalBullet(floatArray: FloatArray): BarBullet {
        return BarBullet(mContext, getDataRectAttribute(defaultColor, height, floatArray))
    }

    private fun getFocusedBullet(color: Int, range: FloatArray): BarBullet {
        PREV_ZONE_START = range[0]
        PREV_ZONE_END = range[1]

        val cornerRadius = if (PREV_ZONE_START == 0f && PREV_ZONE_END == 1f) {
            floatArrayOf(heightSelected / 2, 0f, 0f, heightSelected / 2)
        } else if (PREV_ZONE_START == 4f && PREV_ZONE_END == 5f) {
            floatArrayOf(0f, heightSelected / 2, heightSelected / 2, 0f)
        } else {
            floatArrayOf(0f, 0f, 0f, 0f)
        }

        return BarBullet(
            mContext,
            getDataRectAttribute(
                color,
                heightSelected,
                cornerRadius,
//                        floatArrayOf(3f, 3f, 3f, 3f),
            )
        )
    }

    fun setZone(mapping: ArteryHeathChartDataMapping?) {
        setData()
        val range = when (mapping) {
            ArteryHeathChartDataMapping.WORSE -> floatArrayOf(0f, 1f)
            ArteryHeathChartDataMapping.SLIGHTLY_WORSE -> floatArrayOf(1f, 2f)
            ArteryHeathChartDataMapping.STEADY -> floatArrayOf(2f, 3f)
            ArteryHeathChartDataMapping.SLIGHTLY_BETTER -> floatArrayOf(3f, 4f)
            ArteryHeathChartDataMapping.BETTER -> floatArrayOf(4f, 5f)
            else -> return
        }
        val indexPrev = getIndex(floatArrayOf(PREV_ZONE_START, PREV_ZONE_END))
        val dataPrev = mDataList[indexPrev]
        when (indexPrev) {
            0 ->
                dataPrev.setBullet(
                    State.NORMAL,
                    getNormalBullet(floatArrayOf(height / 2, 0f, 0f, height / 2))
                )

            4 -> dataPrev.setBullet(
                State.NORMAL,
                getNormalBullet(floatArrayOf(0f, height / 2, height / 2, 0f))
            )

            else -> dataPrev.setBullet(
                State.NORMAL,
                getNormalBullet(floatArrayOf(0f, 0f, 0f, 0f))
            )
        }

        val indexCur = getIndex(range)
        val dataCur = mDataList[indexCur]
        dataCur.setBullet(State.NORMAL, getFocusedBullet(selectedColorArray[indexCur], range))

        graph.data = mDataList
    }

    private fun setData() {
        mDataList.clear()
        val chartData1 = ChartData(
            0f,
            floatArrayOf(0f, 1f - BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE),
            getNormalBullet(floatArrayOf(height / 2, 0f, 0f, height / 2))
        )
        val chartData2 = ChartData(
            0f,
            floatArrayOf(
                1f + BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE,
                2f - BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE
            ),
            getNormalBullet(floatArrayOf(0f, 0f, 0f, 0f))
        )
        val chartData3 = ChartData(
            0f,
            floatArrayOf(
                2f + BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE,
                3f - BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE
            ),
            getNormalBullet(floatArrayOf(0f, 0f, 0f, 0f))
        )
        val chartData4 = ChartData(
            0f,
            floatArrayOf(
                3f + BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE,
                4f - BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE
            ),
            getNormalBullet(floatArrayOf(0f, 0f, 0f, 0f))
        )
        val chartData5 = ChartData(
            0f,
            floatArrayOf(4f + BULLET_GAP_HALF_WIDTH_IN_PERCENTAGE, 5f),
            getNormalBullet(floatArrayOf(0f, height / 2, height / 2, 0f))
        )

        mDataList.add(chartData1)
        mDataList.add(chartData2)
        mDataList.add(chartData3)
        mDataList.add(chartData4)
        mDataList.add(chartData5)

        graph.data = mDataList
    }

    private fun getDataRectAttribute(
        color: Int,
        height: Float,
        radius: FloatArray
    ): RectAttribute? {
        val builder = RectAttribute.Builder()
        builder.setColor(color)
            .setHeight(height)
            .setCornerRadius(radius)
        return builder.build()
    }
}
