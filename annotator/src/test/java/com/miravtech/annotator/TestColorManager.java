package com.miravtech.annotator;
import java.awt.Color;

import junit.framework.TestCase;


public class TestColorManager extends TestCase {
	public void testGreenBgContrast() {
		// on green background, it is better to paint black, therefore green dimension counts double towards black.
		 Color  c = ColorManager.getMostContrastantColor(Color.GREEN, Color.BLACK,  Color.WHITE );
		 assertTrue(c == Color.BLACK);
	}
}
