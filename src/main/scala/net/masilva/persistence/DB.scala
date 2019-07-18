package net.masilva.persistence

import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob
import org.reactivestreams.Publisher
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds
import scala.util.{Failure, Success}
import slick.basic.DatabasePublisher
import slick.jdbc.H2Profile.api._
import net.masilva.adt._

object DBConfig{
    val db = Database.forConfig("h2mem")
    val nodes = TableQuery[Nodes]

    class Nodes(tag: Tag) extends Table[(Int, Option[Int], Option[Int])](tag, "nodes") {
      def id = column[Int]("id", O.PrimaryKey)
      def rootId = column[Option[Int]]("root_id")
      def parentId = column[Option[Int]]("parent_id")
      def * = (id, rootId, parentId)
    }

    //Setup initial data.
    def apply(): Unit = {
      val tree = Tree(0)
      for(i <- 1 to 3000){
        tree.insert(i)
      }
      val tree2 = Tree(3001)
      for(i <- 3002 to 3100){
        tree2.insert(i)
      }
      val mapNodes = (node: Node) => (node.id, node.root, node.parent)
      Await.result(db.run(DBIO.seq((nodes.schema).create,
        nodes ++= tree.toList.map(mapNodes) 
          ::: tree2.toList.map(mapNodes)
        // Seq(
        //   (0,None),
        //   (1,Some(0)),(2,Some(0)),
        //   (3,Some(1)),(4,Some(1)),(5,Some(2)),(6,Some(2)),
        //   (7,Some(3)),(8,Some(3)),(9,Some(4)),(10,Some(4))
        // )
      )),  Duration.Inf)
    }
}

object DB{
  import DBConfig._
  apply() // Initialize h2 db and default data.

  def getNode(id: Int) = db.run(nodes.filter(_.id === id).result)

  def getChildren(parentId: Int) = db.run(nodes.filter(_.parentId === parentId).result)

  def upsert(node: (Int, Option[Int], Option[Int])) = db.run(nodes.insertOrUpdate(node))
}
