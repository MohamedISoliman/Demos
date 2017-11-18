package com.example.geekymind.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

public class MyGlsSurfaceView(context: Context) : GLSurfaceView(context) {

  private val renderer: GLSurfaceView.Renderer

  private val TOUCH_SCALE_FACTOR = 180.0f / 320f
  private var mPreviousX: Float = 0.toFloat()
  private var mPreviousY: Float = 0.toFloat()

  /**
   * init : is a common block that called inside the first constructor,
   * if there is another a constructor, first constructor plus init block will be called,
   * the second constructor
   */
  init {
    setEGLContextClientVersion(2);
    renderer = MyRenderer()
    setRenderer(renderer)
    renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY;
  }

  override fun onTouchEvent(e: MotionEvent): Boolean {
    // MotionEvent reports input details from the touch screen
    // and other input controls. In this case, you are only
    // interested in events where the touch position changed.

    val x = e.x
    val y = e.y

    when (e.action) {
      MotionEvent.ACTION_MOVE -> {

        var dx = x - mPreviousX
        var dy = y - mPreviousY

        // reverse direction of rotation above the mid-line
        if (y > height / 2) {
          dx *= -1
        }

        // reverse direction of rotation to left of the mid-line
        if (x < width / 2) {
          dy *= -1
        }

        (renderer as MyRenderer).apply {
          angle += ((dx + dy) * TOUCH_SCALE_FACTOR)
        }
        requestRender();
      }
    }

    mPreviousX = x
    mPreviousY = y
    return true
  }
}