#version 450 core

in vec2 pass_texCoords;

out vec4 out_Color;

uniform sampler2D tex;

void main() {
    out_Color = texture(tex, pass_texCoords);
}