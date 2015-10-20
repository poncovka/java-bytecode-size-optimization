package jbyco;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class Analyzer {

	public static void main(String[] args) {
 		
		JavaClass clazz;
		
		try {
			
			//lookupClassFile(String class_name)
			
			// find analyzed class
			clazz = Repository.lookupClass("input.Test003");
			
			// print informations
			System.out.println(clazz);
			
			// get size of class in bytes
			int size = clazz.getBytes().length;
			System.out.println("The size of class in bytes: " + size);
			
			// get constant pool
			ConstantPool pool = clazz.getConstantPool();
			System.out.println(pool);
			
			// get code
			Method[] methods = clazz.getMethods();
			for (Method m : methods){
				System.out.println("Method:" + m);
				System.out.println("Constant Pool:\n" + m.getConstantPool());
				System.out.println("Local Variable Table:\n" + m.getLocalVariableTable());
				System.out.println("Exception Table:\n" + m.getExceptionTable());
				System.out.println("Code:\n" + m.getCode());
				System.out.println("\n" + "------------------------------------------------------------------" + "\n");
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}

}
