package com.example.geekymind.opengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.geekymind.opengl.shapes.Square
import com.example.geekymind.opengl.shapes.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.os.SystemClock

class MyRenderer : GLSurfaceView.Renderer {

  var angle : Float = 0f


  companion object {

    fun loadShader(type: Int, shaderCode: String): Int {
      // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
      // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
      val shader = GLES20.glCreateShader(type)
      // add the source code to the shader and compile it
      GLES20.glShaderSource(shader, shaderCode)
      GLES20.glCompileShader(shader)
      return shader
    }

  }

  /**
   * lazy is a lazy initialization for immutable properties
   */
  private val triangle by lazy { Triangle() }
  val square by lazy { Square() }

  // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
  private val MVPMatrix = FloatArray(16)
  private val projectionMatrix = FloatArray(16)
  private val viewMatrix = FloatArray(16)

  private val mRotationMatrix = FloatArray(16)




  override fun onDrawFrame(gl: GL10?) {

    val scratch = FloatArray(16)

    // Draw background color
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

    // Set the camera position (View matrix)
    Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

    // Calculate the projection and view transformation
    Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

    // Draw square
    square.draw(MVPMatrix)

    // Create a rotation for the triangle

    // Use the following code to generate constant rotation.
    // Leave this code out when using TouchEvents.
    // long time = SystemClock.uptimeMillis() % 4000L;
    // float angle = 0.090f * ((int) time);


    Matrix.setRotateM(mRotationMatrix, 0, angle, 0f, 0f, 1.0f)

    // Combine the rotation matrix with the projection and camera view
    // Note that the mMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    Matrix.multiplyMM(scratch, 0, MVPMatrix, 0, mRotationMatrix, 0)

    // Draw triangle
    triangle.draw(scratch)

  }

  override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    GLES20.glViewport(0, 0, width, height)

    val ratio = width.toFloat() / height

    // this projection matrix is applied to object coordinates
    // in the onDrawFrame() method
    Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

  }

  override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    // Set the background frame color
    GLES20.glClearColor(0.10f, 0.0f, 0.40f, 1.0f);

  }
}