package com.pramod.chaquopy

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.pramod.chaquopy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mbinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       mbinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val py = Python.getInstance()
        val module = py.getModule("plot")

        mbinding.button.setOnClickListener {
            try {
                val bytes = module.callAttr("plot",
                    mbinding.etX.text.toString(),
                    mbinding.etY.text.toString())
                    .toJava(ByteArray::class.java)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
               mbinding.imageView.setImageBitmap(bitmap)

                currentFocus?.let {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(it.windowToken, 0)
                }
            } catch (e: PyException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}