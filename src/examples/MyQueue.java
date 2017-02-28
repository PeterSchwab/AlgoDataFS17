package examples;

import java.util.Arrays;

public class MyQueue<E> implements Queue<E> {

	private E[] mem = (E[]) new Object[3]; 
	private int head=0,size=0,tail=0;
	
	@Override
	public void enqueue(E o) {
		if (size==mem.length) {
			// expand
			System.out.println("expanding");
			E[] tmp = mem;
			// copy back
			mem = (E[])new Object[size*2];
			tail=0;
			while (tail<size) {
				if (head==size) head=0;
				mem[tail++]=tmp[head++];
			}
			head=0;
		}
		else if (tail==mem.length) tail = 0;
		mem[tail++]=o;
		size++;
	}

	@Override
	public E dequeue() {
		if (size==0) throw new RuntimeException("Queue is empty!");
		if (head==mem.length) head=0;
		size--;
		return (mem[head++]);
	}

	@Override
	public E head() {
		if (size==0) throw new RuntimeException("Queue is empty!");
		// TODO Auto-generated method stub
		if (head==mem.length) head=0;
		return mem[head];
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
		Queue<Integer> q = new MyQueue<>();
		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		q.enqueue(4);
		q.enqueue(5);
		q.enqueue(6);
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());		
		System.out.println(q.dequeue());		
	}

}
