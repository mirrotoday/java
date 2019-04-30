package com.fx.test;
/**
 * 1.泛型的好处是在编译的时候检查类型安全，并且所有的强制转换都是自动和隐式的提高了代码的重用率
 * 2.泛型的本质事参数化类型也就是所操作的数据类型被指定为一个参数（泛型的类型只能是类类型）
 * 3.可用于类，接口，方法的创建---泛型类（由成员变量和成员方法构成），泛型接口，泛型方法
 * @author 一场追逐，不曾停歇
 * ---泛型的概念和泛型类的声明
 * @param <T>
 */
class Gen<T>{
	private T ob;
	public Gen(T ob) {
		this.ob = ob;
	}
	public T getOb() {
		return ob;		
	}
	public void setOb(T ob) {
		this.ob = ob;	
	}
	public void showType() {
		System.out.println("T的实际类型是：" + ob.getClass().getName());//使用系统方法
	}
	
}

public class GenDemo {
public static void main(String[]args) {
	//定义泛型类Gen的一个integer版本
	Gen<Integer> intob = new Gen<Integer>(88);
		intob.showType();//使用泛型中的方法
		int i = intob.getOb();
	System.out.println("value=" + i);
	System.out.println("--------------------------------");
	//定义泛型类Gen的一个String版本
	Gen<String> strOb = new Gen<String>("Hello Gen");
		strOb.showType();
		String s = strOb.getOb();
	System.out.println("value=" + s);
    }
}
