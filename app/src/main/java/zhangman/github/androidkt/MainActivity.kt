package zhangman.github.androidkt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import zhangman.github.androidkt.constrainlayout.ConstraintLayoutStateTest
import zhangman.github.androidkt.notification.NotificationOTest

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val dataset: Array<DemosAdapter.Demo> = arrayOf(
        DemosAdapter.Demo(
            "ConstrainLayout ConstraintLayoutStates ", " Basic ConstraintLayoutStates example ",
            ConstraintLayoutStateTest::class.java
        ),
        DemosAdapter.Demo("Sudoku", "Sudoku java model", SudoActivity::class.java),
        DemosAdapter.Demo(
            "notification test",
            "android o notification compat",
            NotificationOTest::class.java
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewManager = LinearLayoutManager(this)
        viewAdapter = DemosAdapter(dataset)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun start(activity: Class<*>, layoutFileId: Int) {
        val intent = Intent(this, activity).apply {
            putExtra("layout_file_id", layoutFileId)
        }
        startActivity(intent)
    }
}
