package com.aist.common.rapidQuery.scriptParse;

public enum NodeEnum {
	textNode("text",TextNode.class),ifNode("if",IfNode.class);
	String tagName;
	Class nodeClass;
	
	private NodeEnum(String code,Class nodeClass){
		this.tagName = code;
		this.nodeClass = nodeClass;
    }

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
