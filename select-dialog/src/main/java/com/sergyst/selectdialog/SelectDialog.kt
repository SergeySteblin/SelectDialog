package com.sergyst.selectdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlin.math.roundToInt


class SelectDialog(
        private val context: Context,
        private val title: String? = null,
        private val itemStyle: ItemStyle = ItemStyle()
) {
    private var dialog: Dialog
    private var content: LinearLayout
    private var scrollView: ScrollView
    private var hasTitle = !TextUtils.isEmpty(title)
    private val display: Display
    private val displaySize: Point = Point()
    private val itemHeight: Int
    private val spaceBetweenGroups: Int

    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay

        display.getSize(displaySize)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_view, null)
        view.minimumWidth = (displaySize.x * 0.9).roundToInt()

        dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(view)

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        scrollView = view.findViewById(R.id.scroll_view) as ScrollView
        content    = view.findViewById(R.id.scroll_view_content) as LinearLayout

        view.findViewById<TextView>(R.id.title).apply {
            if(hasTitle) {
                visibility = View.VISIBLE
                text = title
                textSize = itemStyle.size.toFloat()
                setTextColor(itemStyle.color)
            } else {
                visibility = View.GONE
            }
        }

        dialog.window?.apply {
            setGravity(Gravity.CENTER)
            val lp = attributes
            lp.x = 0
            lp.y = 0
            attributes = lp
        }

        val scale = context.resources.displayMetrics.density
        itemHeight         = (50 * scale + 0.5f).toInt()
        spaceBetweenGroups = (10 * scale + 0.5f).toInt()
    }

    fun show(groupsItems: List<List<Item>>) {
        if (groupsItems.isEmpty()) {
            return
        }
        addToContent(groupsItems)
        dialog.show()
    }

    private fun addToContent(groupsItems: List<List<Item>>) {
        if (groupsItems.isEmpty()) {
            return
        }
        val numAllItems = groupsItems.fold(0, { count, group -> count + group.size })
        if (numAllItems >= 7) {
            val layoutParams = scrollView.layoutParams as LinearLayout.LayoutParams
            layoutParams.height = displaySize.y / 2
            scrollView.layoutParams = layoutParams
        }

        for (groupInd in groupsItems.indices) {
            val groupItems = groupsItems[groupInd]

            for (itemInGroupInd in groupItems.indices) {
                val item = groupItems[itemInGroupInd]

                val itemView  = TextView(context)
                itemView.text = item.title
                itemView.textSize = itemStyle.size.toFloat()
                itemView.setTextColor(itemStyle.color)
                itemView.gravity = Gravity.CENTER

                if (hasTitle) {
                    if (groupInd in 1 until numAllItems) {
                        itemView.setBackgroundResource(R.drawable.item_middle_selector)
                    } else {
                        itemView.setBackgroundResource(R.drawable.item_bottom_selector)
                    }
                } else {
                    when {
                        groupItems.size == 1 -> {
                            // single item in group
                            itemView.setBackgroundResource(R.drawable.item_single_selector)
                        }
                        itemInGroupInd == 0 -> {
                            // first item
                            itemView.setBackgroundResource(R.drawable.item_top_selector)
                        }
                        itemInGroupInd == groupItems.size - 1 -> {
                            // last item
                            itemView.setBackgroundResource(R.drawable.item_bottom_selector)
                        }
                        else -> {
                            // item in the middle
                            itemView.setBackgroundResource(R.drawable.item_middle_selector)
                        }
                    }
                }

                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight)
                if(groupInd > 0 && itemInGroupInd == 0) {
                    // first item in every not first group
                    layoutParams.topMargin = spaceBetweenGroups
                }
                itemView.layoutParams = layoutParams
                itemView.setOnClickListener {
                    item.clickListener.onClick(item.key)
                    dialog.dismiss()
                }
                content.addView(itemView)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(which: String)
    }

    class Item(var title: String, var key: String, var clickListener: OnItemClickListener)

    class ItemStyle(var size: Int = 20, var color: Int = Color.WHITE)
}
