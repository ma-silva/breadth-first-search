package net.masilva

import net.masilva.adt.Node
import net.masilva.adt.Tree
import net.masilva.persistence.DB._
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.util.Try

package object persistence {
    def composeTree(id: Int): Try[Tree] = Try{
        val root = Await.result(getNode(id), Duration.Inf).head
        val children = Await.result(getChildren(id), Duration.Inf)
        val tree = Tree(root._1)
        val childrenNodes = (for(c <- children) 
            yield Node(c._1, Some(root._1), Some(root._1), Nil, tree.root.height + 1)).toList
        tree.root.children = childrenNodes
        composeChildren(childrenNodes)
        tree
    }

    private def composeChildren(list: List[Node]):Unit = {
        val queue = mutable.Queue[Node]()
        list.foreach(queue += _)
        while(queue.nonEmpty){
            val node = queue.dequeue
            val children = Await.result(getChildren(node.id), Duration.Inf)
            val childrenNodes = for(c <- children) yield Node(c._1, node.root, Some(node.id), Nil, node.height + 1)
            node.children = childrenNodes.toList
            childrenNodes.foreach(queue += _)
        }
    }

    def updateNode(node: (Int, Option[Int])): Int = {
        upsertNode((node._1, None, node._2))
    }

    def upsertNode(node: (Int, Option[Int], Option[Int])): Int = {
        Await.result(upsert(node), Duration.Inf)
    }
}