package zhangman.github.androidkt

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import zhangman.github.androidkt.jsudoku.Board

/**
 * Created by zhangman on 2019/3/29 11:10.
 * Email: zhangman523@126.com
 */
class SudoActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var mSudoKu: Board

    private lateinit var mSudoConfig: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)
        mSudoKu = findViewById(R.id.board)
        mSudoConfig = "005406000000000201007380000062700090050023804704109060823590010490867020576031948"
        mSudoKu.setGameOverCallBack {
            AlertDialog.Builder(this).setTitle("awesome!")
                .setMessage("Congratulations，you solve the Sudoku!")
                .setNegativeButton("exit") { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
                .setPositiveButton("Next") { dialog, which ->
                    dialog.dismiss()

                }
                .create()
                .show()

        }
        mSudoKu.loadMap(mSudoConfig)
    }

    override fun onClick(v: View?) {
        var number: TextView = v as TextView

        mSudoKu.inputText(number.text.toString())

    }
}