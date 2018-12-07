
import java.util.*;
import java.util.List;

/**
 * 
 * @author w w w. j a v a g i s t s . c o m
 *
 */
public class Node<T> {

	private T data = null;

	private List<Node<T>> children = new ArrayList<>();

	private Node<T> parent = null;
	
	private String nodeType;
	private String mostUsedLabel;

	public Node(T data, String nodeType) {
		this.data = data;
		this. nodeType = nodeType;
	}

	public Node(T data) {
		this.data = data;
	}
	
	public Node<T> addChild(Node<T> child) {
		child.setParent(this);
		this.children.add(child);
		return child;
	}

	public Node<T> addChild(Node<T> child,String nodeType) {
		child.setParent(this);
		child.setNodeType(nodeType);
		this.children.add(child);
		return child;
	}
	public void addChildren(List<Node<T>> children) {
		children.forEach(each -> each.setParent(this));
		this.children.addAll(children);
	}

	public List<Node<T>> getChildren() {
		return children;
	}
	
	// returns the child node where the childname matches
	public Node<T> getChild(String childName) {
		Iterator<Node<T>> iter = this.getChildren().iterator();
		Node<T> childNode = null;

		Node<T> node = null;
		while(iter.hasNext())
		{
			node = iter.next();
				if(node.getData().equals(childName))
						{
							childNode = node;
							break;
						}
		}
		return childNode;
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	private void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Node<T> getParent() {
		return parent;
	}
	
	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public void setMostUsedLabel(String mostUsedLabel)
	{
		this.mostUsedLabel=mostUsedLabel;
	}
	public String getMostUsedLabel()
	{
		return this.mostUsedLabel;
	}
	
	public boolean hasChild(Node<T> child) {
		{
			List <Node<T>> children = this.getChildren();
			//List it = children.listIterator();
			while (children.iterator().hasNext())
			{
				if (children.iterator().next().equals(child))
					return true;
				
			}
			return false;
		}
	}
}