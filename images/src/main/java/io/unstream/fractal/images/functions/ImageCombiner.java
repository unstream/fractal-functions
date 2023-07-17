package io.unstream.fractal.images.functions;
import ar.com.hjg.pngj.*;
import io.unstream.fractal.images.entity.Image;
import io.unstream.fractal.images.entity.Quad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Component
public class ImageCombiner {
	private final static Logger LOG = LoggerFactory.getLogger(ImageCombiner.class);

	@Bean
	public Function<List<Image>, Image> combineImages() {
		return images -> {
			try {
				// Determine the size of the combined image

				int cols = (int) Math.sqrt(images.size());
				int imageWidth = images.get(0).getWidth();
				int imageHeight = images.get(0).getHeight();
				int maxWidth = imageWidth * cols;
				int totalHeight = imageHeight * cols;

				// Create the output image
				ImageInfo imageInfo = new ImageInfo(maxWidth, totalHeight, 8, false);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				PngWriter pngWriter = new PngWriter(outputStream, imageInfo);

				// Write the combined image
				ImageLineInt imageLine = new ImageLineInt(imageInfo);
				int[] scanline = imageLine.getScanline();

				for (int i = 0; i < images.size(); i++) {

					for (int y = 0; y < imageHeight; y++) {
						int lineOffset = y * imageWidth;

						for (int x = 0; x < imageWidth; x++) {
							int pixelOffset = (lineOffset + x) * 4;
							int red = images.get(i).getData()[pixelOffset] & 0xFF;
							int green = images.get(i).getData()[pixelOffset + 1] & 0xFF;
							int blue = images.get(i).getData()[pixelOffset + 2] & 0xFF;
							int alpha = images.get(i).getData()[pixelOffset + 3] & 0xFF;

							int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
							scanline[x] = rgb;
						}

						pngWriter.writeRow(imageLine, y);
					}
				}

				// Finish writing and close the output image
				pngWriter.end();

				// Convert output stream to byte array
				byte[] outputBytes = outputStream.toByteArray();
				outputStream.close();
				Image resultImage = Image.builder()
						.width(imageWidth)
						.height(imageHeight)
						.data(outputBytes)
						.build();
				return resultImage;
			} catch (IOException ioException) {
				throw new RuntimeException(ioException);
			}
		};
	}

}
