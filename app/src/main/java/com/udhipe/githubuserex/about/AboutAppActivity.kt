package com.udhipe.githubuserex.about

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.databinding.ActivityAboutAppBinding


class AboutAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutAppBinding
    private lateinit var subject: String
    private lateinit var target: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.about)

        subject = getString(R.string.app_name)
        target = getString(R.string.my_email)


        binding.btnContactUs.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(target)) // recipients
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }

            val title = resources.getString(R.string.chooser_title)
            val chooser = Intent.createChooser(emailIntent, title)

            // Verify the intent will resolve to at least one activity
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(chooser)
            } else {
                val clipboard: ClipboardManager =
                    getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", target)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, getString(R.string.copy) + " $target", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}