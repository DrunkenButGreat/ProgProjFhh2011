package de.gruppe12.ki;

/**
 * Die Klasse Node initialisiert einen Baum
 * <p>
 * Copyright: (c) 2011
 * <p>
 * Company: Gruppe 12
 * <p>
 * 
 * @author Markus
 * @version 1.0.0 21.11.2011 Aenderungen: 21.11. - Keine Aenderung
 */

public class Node<T> {

	private Node<T> left;
	private Node<T> right;
	public T info;

	Node(T i) {
		this(i, null, null);
	}

	Node(T i, Node<T> l, Node<T> r) {
		info = i;
		left = l;
		right = r;
	}

	public T getInfo() {
		return info;
	}

	public Node<T> getLeft() {
		return left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setInfo(T i) {
		info = i;
	}

	public void setLeft(Node<T> l) {
		left = l;
	}

	public void setRight(Node<T> r) {
		right = r;
	}
}
