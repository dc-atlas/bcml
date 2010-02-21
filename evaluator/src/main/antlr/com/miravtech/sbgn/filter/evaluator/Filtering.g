grammar Filtering;


@header {
package com.miravtech.sbgn.filter.evaluator;
import java.util.HashMap;

import com.miravtech.sbgn.FindingType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

}


@lexer::header {package com.miravtech.sbgn.filter.evaluator;}

@members {
	public FindingType toEval;

    	@Override
    	public void recoverFromMismatchedToken(IntStream arg0,
    			RecognitionException arg1, int arg2, BitSet arg3)
    			throws RecognitionException {
    	    throw new MismatchedTokenException(arg2, arg0);
    	}
    	
    	@Override
    	public void reportError(RecognitionException arg0) {
    		super.reportError(arg0);
    		throw new RuntimeException(arg0);
    	}
	
	@SuppressWarnings("unchecked")
	public static boolean contains(FindingType f,  String  property, String val1 )  {
	    String value = val1.substring(1,val1.length() - 1);
		try{
		    //System.out.println("Evaluating: " + property+"="+value);
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
		} catch (Exception e ){
			throw new RuntimeException(e);
		}
	}
}

prog:   expr EOF ;

expr returns [boolean value]
    :   e=atom1 {$value = $e.value;}
        (   'and' e=atom1 {$value = $value && $e.value;}
        |   'or'  e=atom1 {$value = $value || $e.value;}
        |   '=='  e=atom1 {$value = ($value == $e.value);}
        )*
    ;

atom1 returns [boolean value] :
a=atom {$value = $a.value;}
|  'not' a1=atom {$value = !( $a1.value);}
;

 
    
atom returns [boolean value]
    :   BOOL {$value = Boolean.parseBoolean($BOOL.text);}
    |   e=ID '=' v=VAL {$value = contains(toEval, $e.text, $v.text);}   
    |   '(' e1=expr ')' {$value = $e1.value;}
    ;



BOOL :   'true'|'false';
ID  :   ('a'..'z'|'A'..'Z')+ ;
VAL  :   '\''('a'..'z'|'A'..'Z'|' ')+'\'' ;
NEWLINE:'\r'? '\n' ;
WS  :   (' '|'\t')+ {skip();} ;
