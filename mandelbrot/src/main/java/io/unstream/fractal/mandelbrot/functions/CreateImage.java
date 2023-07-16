package io.unstream.fractal.mandelbrot.functions;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;

import io.unstream.fractal.mandelbrot.entity.Gradient;
import io.unstream.fractal.mandelbrot.entity.Color;
import io.unstream.fractal.mandelbrot.entity.Quad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.function.Function;

@Component
public class CreateImage {

	private final static Logger LOG = LoggerFactory.getLogger(CreateImage.class);

	@Bean
	public Function<Quad, byte[]> computePng(){
		return quad -> {
			try {
				long now = System.currentTimeMillis();

				Gradient gradient = new Gradient(new Color(255,255,255), new Color( 0, 0, 0), quad.getMaxValue());

				ImageInfo imageInfo = new ImageInfo(quad.getWidth(), quad.getHeight(), 8, false); // 8 bits per channel, no alpha
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) imageInfo.getTotalRawBytes());

				PngWriter png = new PngWriter(outputStream, imageInfo);
				png.getMetadata().setDpi(100.0);
				png.getMetadata().setTimeNow(0);
				png.getMetadata().setText(PngChunkTextVar.KEY_Title, "Fractal");
				png.getMetadata().setText("Imagetype", "Mandelbrot Set");

				for (int y = 0; y < imageInfo.rows; y++) {
					ImageLineInt line = new ImageLineInt(imageInfo);
					for (int x = 0; x < imageInfo.cols; x++) {
						Color color = gradient.getColor(quad.getData()[y][x]);
						ImageLineHelper.setPixelRGB8(line, x,color.getR(), color.getG(), color.getB());
					}
					png.writeRow(line);
				}
				png.end();
				LOG.info("Image created in " + (System.currentTimeMillis() -now) + " ms.");
				return outputStream.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}
