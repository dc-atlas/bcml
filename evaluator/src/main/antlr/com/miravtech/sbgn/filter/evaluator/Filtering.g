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

	@SuppressWarnings("unchecked")
	public static boolean contains(FindingType f,  String  property, String value )  {
		try{
		    System.out.println("Evaluating: " + property+"="+value);
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

              
expr returns [boolean value]
    :   e=multExpr {$value = $e.value;}
        ( 'and' e=multExpr {$value = $value && $e.value;}
        | 'or' e=multExpr {$value=  $value || $e.value;}
        )*
              
    ;

multExpr returns [boolean value]
    :   e=atom {$value = $e.value;}
    ; 

atom returns [boolean value]
    :   BOOL {$value = Boolean.parseBoolean($BOOL.text);}
    |   '(' expr ')' {$value = $expr.value;}
    |   ID'=''"'VAL'"' {$value = contains(toEval, $ID.text, $VAL.text);} 
    ;

ID:   'organism'|'organismPart' ;
VAL:   ('a'..'z'|'A'..'Z'|' ')+ ;
BOOL :   'true'|'false' ;
NEWLINE:'\r'? '\n' ;
WS  :   (' '|'\t')+ {skip();} ;
