package io.unstream.fractal.images.functions;
import ar.com.hjg.pngj.ImageInfo;

import ar.com.hjg.pngj.ImageLineByte;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.PngWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class ImageCombiner {

	public static byte[] combineImages(Function<Integer, byte[]> imageProvider, int imageCount, int imageWidth, int imageHeight) throws IOException {
		// Determine the size of the combined image
		int maxWidth = imageWidth;
		int totalHeight = imageHeight * imageCount;

		// Create the output image
		ImageInfo imageInfo = new ImageInfo(maxWidth, totalHeight, 8, false);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PngWriter pngWriter = new PngWriter(outputStream, imageInfo);

		// Write the combined image

		ImageLineInt imageLine = new ImageLineByte(imageInfo);
		int[] scanline = imageLine.getScanline();

		for (int i = 0; i < imageCount; i++) {
			byte[] imageBytes = imageProvider.apply(i);

			for (int y = 0; y < imageHeight; y++) {
				int lineOffset = y * imageWidth;

				for (int x = 0; x < imageWidth; x++) {
					int pixelOffset = (lineOffset + x) * 4;
					int red = imageBytes[pixelOffset] & 0xFF;
					int green = imageBytes[pixelOffset + 1] & 0xFF;
					int blue = imageBytes[pixelOffset + 2] & 0xFF;
					int alpha = imageBytes[pixelOffset + 3] & 0xFF;

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

		return outputBytes;
	}

	public static void main(String[] args) {
		try {
			// Example usage
			Function<Integer, byte[]> imageProvider = (index) -> {
				// Load image bytes for the given index from source
				// Return the image bytes
			};
			int imageCount = /* Set number of images */;
			int imageWidth = /* Set image width */;
			int imageHeight = /* Set image height */;

			byte[] combinedImageBytes = combineImages(imageProvider, imageCount, imageWidth, imageHeight);

			// Save or process the combined image byte array as desired

			System.out.println("Images combined successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
