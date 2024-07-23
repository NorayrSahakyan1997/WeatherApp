
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_marginTop="4dp"
                android:orientation="horizontal"
                android:weightSum="2"
                >
                <TextView
                    android:id="@+id/map_result_worse"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:singleLine="true"
                    android:text="@string/map_result_worse"
                    android:gravity="start"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintBottom_toBottomOf="parent"
                    android:layout_weight="1"
                  />

                <TextView
                    android:id="@+id/map_result_better"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:singleLine="true"
                    android:text="@string/map_result_better"
                    android:gravity="end"
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    android:layout_weight="1"
                    />

            </LinearLayout>
