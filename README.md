val textView: TextView = findViewById(R.id.textView)
        textView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = textView.compoundDrawablesRelative[2] // Right drawable
                drawableEnd?.let {
                    val bounds = drawableEnd.bounds
                    val x = event.rawX.toInt()
                    val y = event.rawY.toInt()
                    val right = textView.right - textView.paddingRight

                    if (x >= right - bounds.width() && x <= right && y >= textView.top && y <= textView.bottom) {
                        // Drawable end icon clicked
                        onDrawableEndClick()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    private fun onDrawableEndClick() {
        // Handle the drawable end click event here
        Toast.makeText(this, "Drawable end icon clicked!", Toast.LENGTH_SHORT).show()
    }
