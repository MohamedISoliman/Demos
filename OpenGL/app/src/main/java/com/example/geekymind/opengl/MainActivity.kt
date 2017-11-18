package com.example.geekymind.opengl

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

  lateinit var glServiceView: GLSurfaceView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    glServiceView = MyGlsSurfaceView(this)
    setContentView(glServiceView)

  }
}
