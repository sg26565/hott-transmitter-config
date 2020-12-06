import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*

fun main() {
    launch<TreeViewTestApp>()
}

class TreeViewTestApp : App(TreeViewTestView::class)

class TreeViewTestView : View() {
    private val treeView = TreeView<String>()
    private val rootNode = TreeItem("root node")

    override val root = treeView

    init {
        treeView.root = rootNode

        for (i in 1..3) {
            rootNode.children.add(TreeItem("Item $i"))
        }

        rootNode.children.forEachIndexed { i, node ->
            for (j in 1..3) {
                node.children.add(TreeItem("Item ${i + 1}.$j"))
            }
        }

        rootNode.addEventHandler(eventType) { evt ->
            val item = evt.treeItem
            println("${item.value} was expanded")
        }
    }

    companion object {
        val eventType = TreeItem.branchExpandedEvent<String>()
    }
}
