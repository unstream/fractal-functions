package io.unstream.fractal.images.functions;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;
import io.unstream.fractal.images.entity.Quad;
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

				LOG.info("Starting to compute image ... ");
				LOG.info("Quad: " +  quad.getWidth() + ", " + quad.getHeight());
				ImageInfo imi = new ImageInfo(quad.getWidth(), quad.getHeight(), 8, false); // 8 bits per channel, no alpha
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) imi.getTotalRawBytes());

				PngWriter png = new PngWriter(outputStream, imi);
				png.getMetadata().setDpi(100.0);
				png.getMetadata().setTimeNow(0);
				png.getMetadata().setText(PngChunkTextVar.KEY_Title, "Fractal");
				png.getMetadata().setText("Imagetype", "Mandelbrot Set");
				for (int y = 0; y < imi.rows; y++) {
					ImageLineInt iline = new ImageLineInt(imi);
					for (int x = 0; x < imi.cols; x++) {
						int color = quad.getData()[y][x];
						if (color > 255) {
							color = 255;
						}
						int r = 0;
						int g = 0;
						int b = color;
						ImageLineHelper.setPixelRGB8(iline, x, r, g, b);
					}
					png.writeRow(iline);
				}
				png.end();
				LOG.info("Stopped to compute image ... ");
				return outputStream.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}
