uniform mat4 u_MVP;
attribute vec4 v_Position;

void main() {
   gl_Position = u_MVP * v_Position;
}
