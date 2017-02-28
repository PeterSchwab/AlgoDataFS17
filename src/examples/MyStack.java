package examples;

import java.util.Arrays;


public class MyStack<E> implements Stack<E> {

	private E[] mem = (E[])new Object[1];
	private int size;
	
	@Override
	public void push(E o) {
		if (size==mem.length) mem = Arrays.copyOf(mem,size*2);
		mem[size++]=o;
	}

	@Override
	public E pop() {
		if (size == 0) throw new RuntimeException("stack is empty!");
		return mem[--size];
	}

	@Override
	public E top() {
		if (size == 0) throw new RuntimeException("stack is empty!");
		return mem[size-1];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	public static void main(String[] args) {
		Stack<String> s = new MyStack<>();
		s.push("hans");
		s.push("beat");
		String st = (String)s.top();
		System.out.println(s.pop());
		System.out.println(s.pop())		;
		
	}

}
