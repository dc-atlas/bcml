grammar Filtering;


@header {
package com.miravtech.sbgn.filter.evaluator;
import java.util.HashMap;

import com.miravtech.sbgn.FindingType;
import com.miravtech.sbgn.SBGNGlyphType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

}


@lexer::header {package com.miravtech.sbgn.filter.evaluator;


}

@members {
	public FindingType toEval;
	public SBGNGlyphType glyphToEval;

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
    |   e=ID '=' v=VAL {$value = com.miravtech.filterMatcher.Matcher.contains(glyphToEval,toEval, $e.text, $v.text);}   
    |   '(' e1=expr ')' {$value = $e1.value;}
    ;



BOOL :   'true'|'false';
ID  :   ('a'..'z'|'A'..'Z')+ ;
VAL  :   '\''('a'..'z'|'A'..'Z'|' ')+'\'' ;
NEWLINE:'\r'? '\n' ;
WS  :   (' '|'\t')+ {skip();} ;
