package org.wit.hillfort.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.helpers.readImage
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel


class HillfortActivity : AppCompatActivity(), AnkoLogger{

  var hillfort = HillfortModel()
  lateinit var app: MainApp
  val IMAGE_REQUEST1 = 1
  val IMAGE_REQUEST2 = 2
  val LOCATION_REQUEST = 3

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    toolbarAdd.title = title
    setSupportActionBar(toolbarAdd)
    info("Hillfort Activity started..")

    app = application as MainApp

    var edit = false

    if (intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      hillfortTitle.setText(hillfort.title)
      description.setText(hillfort.description)
      btnAdd.setText(R.string.save_hillfort)

      hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.image1))
      hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.image2))

      if (hillfort.image1 != null) {
        chooseImage1.setText(R.string.change_hillfort_image)
      }
      if (hillfort.image2 != null) {
        chooseImage2.setText(R.string.change_hillfort_image)
      }
    }

    btnAdd.setOnClickListener() {
      hillfort.title = hillfortTitle.text.toString()
      hillfort.description = description.text.toString()
      if (hillfort.title.isEmpty()) {
        toast(R.string.enter_hillfort_title)
      } else {
        if (edit) {
          app.hillforts.update(hillfort.copy())
        } else {
          app.hillforts.create(hillfort.copy())
        }
      }
      info("add Button Pressed: $hillfortTitle")
      setResult(AppCompatActivity.RESULT_OK)
      finish()
    }

    chooseImage1.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST1)
    }

    chooseImage2.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST2)
    }

    hillfortLocation.setOnClickListener {
      val location = Location(52.245696, -7.139102, 15f)
      if (hillfort.zoom != 0f) {
        location.lat =  hillfort.lat
        location.lng = hillfort.lng
        location.zoom = hillfort.zoom
      }
      startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        finish()
      }
      R.id.item_delete -> {
        app.hillforts.delete(hillfort.copy())
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      IMAGE_REQUEST1 -> {
        if (data != null) {
          hillfort.image1 = data.getData().toString()
          hillfortImage1.setImageBitmap(readImage(this, resultCode, data))
          chooseImage1.setText(R.string.change_hillfort_image)
        }
      }
      IMAGE_REQUEST2 -> {
        if (data != null) {
          hillfort.image2 = data.getData().toString()
          hillfortImage2.setImageBitmap(readImage(this, resultCode, data))
          chooseImage2.setText(R.string.change_hillfort_image)
        }
      }
      LOCATION_REQUEST -> {
        if (data != null) {
          val location = data.extras?.getParcelable<Location>("location")!!
          hillfort.lat = location.lat
          hillfort.lng = location.lng
          hillfort.zoom = location.zoom
        }
      }
    }
  }
}