package net.masilva.service

import cats.effect.IO
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._
import scala.util.{Success, Failure}
import net.masilva.adt._
import net.masilva.adt.Node
import net.masilva.adt.Tree
import net.masilva.persistence.DB._
import net.masilva.persistence._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable

object DataServices{

    /**
     * routes: address:port'/'
     */
    def index: Endpoint[IO, String] = get(pathEmpty) {
        Ok(s"""Services routes :
        |http://localhost:8081/all
        |http://localhost:8081/fetch/2
        |http://localhost:8081/update/3/2
        |http://localhost:8081/insert/4000/1""".stripMargin)
      }
    
    /**
     * routes: address:port'/all'
     */
    def all: Endpoint[IO, Node] = get("all") {
        val tree = composeTree(0)
        tree match {
            case Success(res) => Ok(res.root)
            case Failure(err) => InternalServerError(new Exception(err))
        }
    }

    /**
     * routes: address:port'/fetch'
     */
    def fetch: Endpoint[IO, Node] = get("fetch" :: path[Int]) { id: Int =>
        val tree = composeTree(id)
        tree match{
            case Success(res) => Ok(res.root)
            case Failure(err) => NotFound(new Exception(s"Error, cannot find node $id."))
        }
    }

    /**
     * routes: address:port'/update/:nodeId/:futureParentId'
     */
    def update: Endpoint[IO, Node] = get("update" :: path[Int] :: path[Int]) { (id: Int, futureParent: Int) =>
        upsert(id, futureParent, updateNode((id, Some(futureParent))))
    }

    /**
     * routes: address:port'/insert/:uniqueNodeId/:rootId/:futureParentId'
     */
    def insert: Endpoint[IO, Node] = get("insert" :: path[Int] :: path[Int] :: path[Int]) { (id: Int, root: Int, futureParent: Int) =>
        upsert(id, futureParent, upsertNode((id, Some(root), Some(futureParent))))
    }

    private def upsert(id: Int, futureParent: Int, result: Int)  = {
        val tree = composeTree(futureParent)
            tree match {
                case Success(res) if result == 1 => Ok(res.root)
                case Failure(err) => NotAcceptable(new Exception(s"Error, cannot update node $id."))
            }
    }

    val services = index :+: all :+: update :+: fetch :+: insert
}