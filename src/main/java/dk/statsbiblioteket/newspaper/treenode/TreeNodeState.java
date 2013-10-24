package dk.statsbiblioteket.newspaper.treenode;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;

/**
 * Provides functionality for accessing the current state for the current node in the batch structure.
 */
public class TreeNodeState extends DefaultTreeEventHandler {
    private TreeNode currentNode = null;

    public TreeNode getCurrentNode() {
        return currentNode;
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        updateCurrentNode(event);
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        currentNode = currentNode.getParent();
    }

    // Todo This is becoming a complicated. consider switch to state machine pattern.
    private void updateCurrentNode(NodeBeginsParsingEvent event) {
        NodeType nextNodeType = null;
        if (currentNode == null) {
            nextNodeType = NodeType.BATCH;
        }  else if (currentNode.getType().equals(NodeType.BATCH)) {
            if (event.getName().endsWith("WORKSHIFT-ISO-TARGET")) {
                nextNodeType = NodeType.WORKSHIFT_ISO_TARGET;
            } else {
                nextNodeType = NodeType.FILM;
            }
        } else if (currentNode.getType().equals(NodeType.FILM)) {
            if (event.getName().endsWith("FILM-ISO-TARGET")) {
                nextNodeType = NodeType.FILM_ISO_TARGET;
            } else if (event.getName().endsWith("UNMATCHED")) {
                nextNodeType = NodeType.UNMATCHED;
            } else {
                nextNodeType = NodeType.EDITION;
            }
        } else if (currentNode.getType().equals(NodeType.EDITION) ||
                currentNode.getType().equals(NodeType.UNMATCHED)) {
            nextNodeType = NodeType.PAGE;
        } else if (currentNode.getType().equals(NodeType.FILM_ISO_TARGET)) {
            nextNodeType = NodeType.FILM_TARGET;
        } else if (currentNode.getType().equals(NodeType.WORKSHIFT_ISO_TARGET)) {
            nextNodeType = NodeType.WORKSHIFT_TARGET;
        } else if (currentNode.getType().equals(NodeType.PAGE)) {
            nextNodeType = NodeType.PAGE_IMAGE;
        } else if (currentNode.getType().equals(NodeType.FILM_TARGET)) {
            nextNodeType = NodeType.TARGET_IMAGE;
        } else if (currentNode.getType().equals(NodeType.WORKSHIFT_TARGET)) {
            nextNodeType = NodeType.TARGET_IMAGE;
        } else {
            throw new IllegalStateException("Unexpected event: " + event + " for current node: " + currentNode);
        }
        assert (nextNodeType != null);
        currentNode = new TreeNode(event.getName(), nextNodeType, currentNode);
    }
}
