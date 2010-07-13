package com.miravtech.annotator;
import java.awt.Color;

import junit.framework.TestCase;


public class TestColorManager extends TestCase {
	public void testGreenBgContrast() {
		 Color  c = ColorManager.getMostContrastantColor(Color.GREEN, Color.BLACK,  Color.WHITE );
		 assertTrue(c == Color.WHITE);
	}
}
