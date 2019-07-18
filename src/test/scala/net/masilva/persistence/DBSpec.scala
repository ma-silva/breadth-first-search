import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class ConfigSpec extends FreeSpec{
    "A DB" - {
        import net.masilva.persistence.DB._

        "get should return a node equivalent to the  requested parameter." in {
            Await.result(getNode(2), Duration.Inf).head._1 shouldBe 2 
        }        

        "getChildren should return nodes with parent id equivalent to the  requested parameter." in {
            Await.result(getChildren(0), Duration.Inf).size shouldBe 2 
        }

        "upsert should update or insert a node." in {
            Await.result(upsert((0, None, None)), Duration.Inf) shouldBe 1
            Await.result(upsert((4, None, Some(2))), Duration.Inf) shouldBe 1
        }
    }
}

