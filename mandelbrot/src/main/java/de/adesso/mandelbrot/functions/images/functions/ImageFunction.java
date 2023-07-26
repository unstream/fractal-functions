package de.adesso.mandelbrot.functions.images.functions;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;

import de.adesso.mandelbrot.functions.images.entity.Image;
import de.adesso.mandelbrot.functions.images.entity.ImageData;
import de.adesso.mandelbrot.functions.images.entity.Color;
import de.adesso.mandelbrot.functions.images.control.Gradient;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayOutputStream;
import java.util.function.Function;

@Component
public class ImageFunction {

	private final static Logger LOG = LoggerFactory.getLogger(ImageFunction.class);

	@Autowired
	private Function<ImageData, Image> colorizeImageDate;

	public Function<ImageData, byte[]> obsoleteCreateAndColorizeImage(){

		return imageData -> {
			try {
				long now = System.currentTimeMillis();

				//Gradient gradient = new Gradient(new Color(255,255,255), new Color( 0, 0, 0), imageData.getMaxValue());
				Gradient gradient = new Gradient(imageData.getMaxValue());

				ImageInfo imageInfo = new ImageInfo(imageData.getWidth(), imageData.getHeight(), 8, false); // 8 bits per channel, no alpha
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) imageInfo.getTotalRawBytes());

				PngWriter png = new PngWriter(outputStream, imageInfo);

				for (int y = 0; y < imageInfo.rows; y++) {
					ImageLineInt line = new ImageLineInt(imageInfo);
					for (int x = 0; x < imageInfo.cols; x++) {
						Color color = gradient.lookupColor(imageData.getData()[y][x]);
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

	@RouterOperations(value = {
			@RouterOperation(method = RequestMethod.POST, operation	= @Operation(
					description = "Generate a colorized PNG image for the given image data.",
					tags = "images")),
			@RouterOperation(method = RequestMethod.GET, operation = @Operation(hidden = true))
	})
	@Bean
	public Function<Image, byte[]> createImage(){
		return image -> {
			try {
				long now = System.currentTimeMillis();

				ImageInfo imageInfo = new ImageInfo(image.getWidth(), image.getHeight(), 8, false); // 8 bits per channel, no alpha
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) imageInfo.getTotalRawBytes());

				PngWriter png = new PngWriter(outputStream, imageInfo);

				for (int y = 0; y < imageInfo.rows; y++) {
					ImageLineInt line = new ImageLineInt(imageInfo);
					for (int x = 0; x < imageInfo.cols; x++) {
						Color color = image.getData()[y][x];
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
