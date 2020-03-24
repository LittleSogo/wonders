package coral.widget.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import wfc.service.util.OrderedHashSet;

/**
 * 供前台Ext使用的树状数据结构的节点
 * 
 * @author 龚云
 * 
 * @param <T>
 */
public class TreeNode<T> {

	private OrderedHashSet<TreeNode<?>> children = new OrderedHashSet<TreeNode<?>>();
	private T data;
	private String id;
	private String text;
	private TreeNode<?> parent;
	private boolean leaf;

	/**
	 * 
	 * @param id
	 *            节点id
	 * @param text
	 *            节点名称
	 * @param data
	 *            节点数据
	 * @param leaf
	 *            是否是叶子节点
	 */
	public TreeNode(String id, String text, T data, boolean leaf) {
		this.id = id;
		this.text = text;
		this.data = data;
		this.leaf = leaf;
	}

	/**
	 * 向当前节点添加子节点
	 * 
	 * @param node
	 *            子节点
	 */
	public void addChild(TreeNode<?> node) {
		if (node == null)
			return;
		if (node.parent != this) {
			node.parent = this;
			if (!children.contains(node)) {
				children.add(node);
			}
		}
	}

	/**
	 * 获取所有孩子节点
	 * 
	 * @return
	 */
	public OrderedHashSet<TreeNode<?>> getChildren() {
		return this.children;
	}

	/**
	 * 节点数据
	 * 
	 * @return
	 */
	public T getData() {
		return data;
	}

	/**
	 * 节点ID
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 节点名称
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * 是否是叶子节点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return this.leaf;
	}

	/**
	 * 删除孩子节点
	 * 
	 * @param node
	 *            子节点
	 */
	public void removeChild(TreeNode<?> node) {
		if (node == null)
			return;
		if (children.contains(node))
			children.remove(node);
		node.parent = null;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
