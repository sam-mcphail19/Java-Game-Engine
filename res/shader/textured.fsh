#version 450 core

in DATA {
    vec4 position;
    vec2 texCoords;
} in_data;

out vec4 out_Color;

uniform sampler2D tex;

void main() {
    //out_Color = vec4(vec3(depth), 1.0);
    //out_Color = vec4(vec3(in_data.position), 1.0);
    out_Color = texture(tex, in_data.texCoords);
}