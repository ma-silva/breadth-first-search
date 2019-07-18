import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class TreeSpec extends FreeSpec{
    "A Tree" - {
        import net.masilva.adt._        
        val tree = Tree(0)
        tree.insert(1)
        tree.insert(2)
        tree.insert(3)

        "instance should return a root." in {
            tree.root.id shouldBe 0
        }

        "insert should able to insert a node." in {
            tree.root.children.size shouldBe 2
        }

        "findLeafOrBranch should able to find a leaf or branch node." in {
            tree.findLeafOrBranch(tree.root).get.id shouldBe 1
        }

        "findNode should able to find a node." in {
            tree.findNode(2).get.id shouldBe 2
        }

        "findRelation should return a node and a parent node." in {
            tree.findRelation(1).map(x=> (x._1.id, x._2.get.id)) shouldBe Some((1,0))
        }

        "updateParentNode should a node's parent." in {
            tree.updateParentNode(3,2).get.parent shouldBe Some((2))
        }

        "toList should return all the nodes in a list." in {
            tree.toList().size shouldBe 4
        }
    }
}