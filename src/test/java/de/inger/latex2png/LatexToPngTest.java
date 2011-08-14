package de.inger.latex2png;

import java.io.IOException;

import junit.framework.TestCase;

public class LatexToPngTest extends TestCase {

	public void testConvertString() {
		LatexToPng latexToPng = new LatexToPng();
		latexToPng.setTightImageSize(true);
		latexToPng.setTransparentBackground(false);
		byte[] picture = null;
		try {
			picture = latexToPng.convertString("--- Не хотите ли подбавить рому? --- сказал я моему собеседнику. --- У меня есть белый из {\\it Тифлиса}. Теперь холодно.");
			
		} catch (IOException e) {
			fail(e.getMessage());
		}		
		assertNotNull(picture);
		assertTrue(picture.length > 0);
	}
	
}
