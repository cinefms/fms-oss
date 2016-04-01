package com.openfms.utils.common.reflection;

import java.util.List;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

public class ReflectionSupports {
	
	
	public Class getSupported(Class who) {
		TypeResolver tr = new TypeResolver();
		ResolvedType rt = tr.resolve(who);
		List<ResolvedType> tt = rt.getParentClass().getTypeParameters();
		if(tt.size()==1) {
			return tt.get(0).getErasedType();
		}
		return null;
	}
	
	public static boolean supports(Class who, Class what) {
		TypeResolver tr = new TypeResolver();
		ResolvedType rt = tr.resolve(who);
		List<ResolvedType> tt = rt.getParentClass().getTypeParameters();
		
		if(tt.size()==1) {
			if(tt.get(0).getErasedType()==what) {
				return true;
			}
		}
		return false;
	}

}
