package net.masilva.adt

import scala.util.{Try, Success, Failure}
import scala.collection.mutable
import scala.annotation.tailrec

/**
 * Class Node models the node structure of a Tree.
 */
case class Node(
    id: Int, 
    root: Option[Int], 
    var parent: Option[Int], 
    var children: List[Node], 
    height: Int
)

/**
 * Class Tree models the tree hierarchical structure with a root node.
 */
case class Tree(id: Int){
    val root = Node(id, None, None, Nil, 0)

    /**
    * An algorithm for inserting a node.
    */
    def insert(id: Int): Unit = {
        val parent = findLeafOrBranch(root).getOrElse(throw new Exception(s"Error, cannot insert node $id."))

        parent.children = parent.children match {
            case Nil => List(Node(id, Some(root.id), Some(parent.id), Nil, parent.height + 1))
            case _ => parent.children :+ Node(id, Some(root.id), Some(parent.id), Nil, parent.height + 1)
        }
    }

    /**
    * An algorithm for finding a leaf or branch node.
    */
    def findLeafOrBranch(node: Node): Option[Node] = {
        @tailrec
        def recLevelOrderSearch(queue: mutable.Queue[Node]): Option[Node] = 
            if(queue.nonEmpty){
                val node = queue.dequeue
                node.children match {
                    case Nil => Some(node)
                    case head::Nil => Some(node)
                    case (head::tail) => recLevelOrderSearch(queue += head ++= tail)
                    case _ => recLevelOrderSearch(queue)
                }
            } else None

        recLevelOrderSearch(mutable.Queue.empty += root)
    }

    def findNode(id: Int): Option[Node] = if(id == root.id ) Some(root) else {
        findRelation(id).map(_._1)
    }

    /**
    * An algorithm for finding a binary relation of a node.
    */
    def findRelation(id: Int): Option[(Node, Option[Node])] = if(id == root.id ) Some(root, None) else {
        @tailrec
        def recLevelOrderSearch(queue: mutable.Queue[(Node, Option[Node])]): Option[(Node, Option[Node])] = 
            if(queue.nonEmpty){
                val node = queue.dequeue
                if(id==node._1.id) Some(node) else {
                    val parent = node._1
                    parent.children match {
                        case head::Nil => recLevelOrderSearch(queue += ((head, Some(parent))))
                        case (head::tail) => recLevelOrderSearch(queue += ((head, Some(parent))) ++= tail.map((_,Some(parent))))
                        case _ => recLevelOrderSearch(queue)
                    }
                }
            } else None

        recLevelOrderSearch(mutable.Queue.empty += ((root, None)))
    }

    /**
    * An algorithm for updating a node.
    */
    @throws(classOf[IllegalArgumentException])
    @throws(classOf[NoSuchElementException])
    def updateParentNode(id: Int, futureParentId: Int): Try[Node] = Try{
        if(id == root.id) throw new IllegalArgumentException("Error, cannot update root node.")
        val err = (x: Int) => new NoSuchElementException(s"Error, cannot find node : $x.")

        val (node, parentNode) = findRelation(id).getOrElse(throw err(id))
        val futureParentNode = findNode(futureParentId).getOrElse(throw err(futureParentId))
        val updatedNode = node.copy(parent = Some(futureParentId))

        parentNode.get.children = parentNode.get.children.filterNot(_.id == id)
        futureParentNode.children = futureParentNode.children :+ updatedNode
        updatedNode
    }

    /**
    * Transforms a tree to a list of Nodes.
    */
    def toList(): List[Node] = {
        @tailrec
        def recLevelOrderSearch(queue: mutable.Queue[Node], list: List[Node]): List[Node] = 
            if(queue.nonEmpty){
                val parent = queue.dequeue
                val addNodeToList =  parent :: list
                parent.children match {
                    case head::Nil => recLevelOrderSearch(queue += head, addNodeToList)
                    case (head::tail) => recLevelOrderSearch(queue += head ++= tail, addNodeToList)
                    case _ => recLevelOrderSearch(queue, addNodeToList)
                }
            } else list

        recLevelOrderSearch(mutable.Queue.empty += root, List())
    }
}