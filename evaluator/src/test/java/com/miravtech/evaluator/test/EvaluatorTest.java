package com.miravtech.evaluator.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.miravtech.sbgn.FindingType;
import com.miravtech.sbgn.OrganismEnum;
import com.miravtech.sbgn.filter.evaluator.FilteringLexer;
import com.miravtech.sbgn.filter.evaluator.FilteringParser;

public class EvaluatorTest {

	@BeforeClass
	public void setup() {

	}

	
	@SuppressWarnings("unchecked")
	public static boolean contains(FindingType f,  String  property, String value ) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		f.getExperimentDesign();
		String prop = property.substring(0, 1).toUpperCase() + property.substring(1);
		String method =  "get"+prop;
		String enumType = "com.miravtech.sbgn."+prop+"Enum";
		Method getter = FindingType.class.getMethod(method);
		Object o = getter.invoke(f);
		List l = (List)o;
		if (l.size() == 0)
			return true;
		Class enumClass = Class.forName(enumType);
		Method getValue = enumClass.getMethod("value");
		for (Object o1: l) {
			String val = (String)getValue.invoke(o1);
			if (val.equalsIgnoreCase(value))
				return true;
		}
		return false;
	}
	
	@Test
	public void evaluateTest() throws Exception {
		
		ByteArrayInputStream bais = new ByteArrayInputStream("organism=\"Homo sapienssss\" or organism=\"Homo sapiens\"".getBytes());
		ANTLRInputStream input = new ANTLRInputStream(bais);
		FilteringLexer lexer = new FilteringLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FilteringParser parser = new FilteringParser(tokens);
		parser.toEval = new FindingType();
		parser.toEval.getOrganism().add(OrganismEnum.HOMO_SAPIENS);
		assert(contains(parser.toEval,"organism", "Homo sapiens"));
		assert(!contains(parser.toEval,"organism", "Homo sapiens1"));
		assert(contains(parser.toEval,"cellType", "test"));
		boolean ret = parser.expr();
		System.out.println("Return is: "+ret);

	}

}
