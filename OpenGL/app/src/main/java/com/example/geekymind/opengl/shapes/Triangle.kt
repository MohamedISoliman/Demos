package com.example.geekymind.opengl.shapes

import java.nio.FloatBuffer
import java.nio.ByteOrder.nativeOrder
import java.nio.ByteBuffer
import android.opengl.GLES20
import com.example.geekymind.opengl.MyRenderer
import kotlin.properties.Delegates

class Triangle : Shape {

  private var vertexBuffer: FloatBuffer

  private var program: Int = 0

  private val fragmentShaderCode = (
      "precision mediump float;" +
          "uniform vec4 vColor;" +
          "void main() {" +
          "  gl_FragColor = vColor;" +
          "}")

  private val vertexShaderCode = "uniform mat4 uMVPMatrix;" +
      "attribute vec4 vPosition;" +
      "void main() {" +
      // the matrix must be included as a modifier of gl_Position
      // Note that the uMVPMatrix factor *must be first* in order
      // for the matrix multiplication product to be correct.
      "  gl_Position = uMVPMatrix * vPosition;" +
      "}"

  // Use to access and set the view transformation
  private val mMVPMatrixHandle: Int = 0

  /**
   * Delegate
   */
  // Use to access and set the view transformation
  private var mvpMatrixHandle by Delegates.notNull<Int>()

  // number of coordinates per vertex in this array
  val COORDS_PER_VERTEX = 3
  private var triangleCoords = floatArrayOf(// in counterclockwise order:
      0.0f, 0.622008459f, 0.0f, // top
      -0.5f, -0.311004243f, 0.0f, // bottom left
      0.5f, -0.311004243f, 0.0f  // bottom right
  )

  // Set color with red, green, blue and alpha (opacity) values
  var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

  init {
    // initialize vertex byte buffer for shape coordinates
    val bb = ByteBuffer.allocateDirect(
        // (number of coordinate values * 4 bytes per float)
        triangleCoords.size * 4)
    // use the device hardware's native byte order
    bb.order(nativeOrder())

    // create a floating point buffer from the ByteBuffer
    vertexBuffer = bb.asFloatBuffer()
    // add the coordinates to the FloatBuffer
    vertexBuffer.put(triangleCoords)
    // set the buffer to read the first coordinate
    vertexBuffer.position(0)

    val vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
        vertexShaderCode)
    val fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
        fragmentShaderCode)

    // create empty OpenGL ES Program
    program = GLES20.glCreateProgram()

    // add the vertex shader to program
    GLES20.glAttachShader(program, vertexShader)

    // add the fragment shader to program
    GLES20.glAttachShader(program, fragmentShader)

    // creates OpenGL ES program executables
    GLES20.glLinkProgram(program)
  }

  private var mPositionHandle: Int = 0
  private var mColorHandle: Int = 0

  private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
  private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

  override fun draw(mvpMatrix: FloatArray) {
    // Add program to OpenGL ES environment
    GLES20.glUseProgram(program)

    // get handle to vertex shader's vPosition member
    mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition")

    // Enable a handle to the triangle vertices
    GLES20.glEnableVertexAttribArray(mPositionHandle)

    // Prepare the triangle coordinate data
    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
        GLES20.GL_FLOAT, false,
        vertexStride, vertexBuffer)

    // get handle to fragment shader's vColor member
    mColorHandle = GLES20.glGetUniformLocation(program, "vColor")

    // Set color for drawing the triangle
    GLES20.glUniform4fv(mColorHandle, 1, color, 0)

    // Draw the triangle
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

    // Disable vertex array
    GLES20.glDisableVertexAttribArray(mPositionHandle)

    // get handle to shape's transformation matrix
    mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

    // Pass the projection and view transformation to the shader
    GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

    // Draw the triangle
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

    // Disable vertex array
    GLES20.glDisableVertexAttribArray(mPositionHandle);

  }

}