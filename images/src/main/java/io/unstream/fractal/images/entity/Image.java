package io.unstream.fractal.images.entity;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Image {
    private int height;
    private int width;
    private byte[] data;
}
