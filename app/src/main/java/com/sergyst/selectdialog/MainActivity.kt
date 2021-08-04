package com.sergyst.selectdialog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.sergyst.selectdialog.SelectDialog.OnItemClickListener
import com.sergyst.selectdialog.SelectDialog.ItemStyle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.button1).apply {
            setOnClickListener {
                SelectDialog(this@MainActivity).apply {
                    val groups = mutableListOf<List<SelectDialog.Item>>().apply {
                            add(mutableListOf<SelectDialog.Item>().apply {
                                add(SelectDialog.Item(title = "Limit", key = "Limit", clickListener = generalListener))
                                add(SelectDialog.Item(title = "Stop", key = "Stop", clickListener = generalListener))
                            })
                            add(mutableListOf<SelectDialog.Item>().apply {
                                add(SelectDialog.Item(title = "OCO", key = "OCO", clickListener = generalListener))
                                add(SelectDialog.Item(title = "If Done", key = "IfDone", clickListener = generalListener))
                                add(SelectDialog.Item(title = "If Done Loop", key = "IfDoneLoop", clickListener = generalListener))
                            })
                            add(mutableListOf<SelectDialog.Item>().apply {
                                add(SelectDialog.Item(title = "TWAP", key = "TWAP", clickListener = generalListener))
                            })
                    }
                    show(groups)
                }
            }
        }

        findViewById<Button>(R.id.button2).apply {
            setOnClickListener {
                SelectDialog(this@MainActivity, /*title = "Select", */itemStyle = ItemStyle(size = 18)).apply {
                    val groups = mutableListOf<List<SelectDialog.Item>>().apply {
                        add(mutableListOf<SelectDialog.Item>().apply {
                            for (i in 1..12) {
                                add(SelectDialog.Item(title = "Item $i", key = "Item $i", clickListener = generalListener))
                            }
                        })
                    }
                    show(groups)
                }
            }
        }
    }

    private val generalListener = object : OnItemClickListener {
        override fun onClick(which: String) {
            Toast.makeText(this@MainActivity, which, Toast.LENGTH_SHORT).show()
        }
    }
}
