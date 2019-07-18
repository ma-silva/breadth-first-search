import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import com.twitter.finagle.http.Status
import io.finch._

class DataServicesSpec extends FreeSpec{
    "A DataServices" - {
        import net.masilva.service.DataServices._

        "index should return the list of services." in {
            assert(index(Input.get("/")).awaitValueUnsafe() == Some(s"""Services routes :
            |http://localhost:8081/all
            |http://localhost:8081/fetch/2
            |http://localhost:8081/update/3/2
            |http://localhost:8081/insert/4000/1""".stripMargin))
        }

        "all should return a root node." in {
            all(Input.get("/all")).awaitValueUnsafe().head.height shouldBe 0
        }

        "fetch should return a node equivalent to the requested parameter." in {
            fetch(Input.get("/fetch/2")).awaitValueUnsafe().head.id shouldBe 2
        }

        "update should return the new parent node." in {
            update(Input.get("/update/3/2")).awaitValueUnsafe().head.id shouldBe 2
        }
    }
}