package com.example.shopingcart

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shopingcart.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class MainActivity : AppCompatActivity() {
    var productmodel = ProductModel()
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                binding.mainLayout.setBackgroundColor(Color.LTGRAY)
                binding.mainLayout.visibility = View.GONE
                binding.spinKit.visibility = View.VISIBLE
                val fileUri = data?.data!!
                Firebase.storage.reference.child("ProductImage${UUID.randomUUID()}")
                    .putFile(fileUri).addOnCompleteListener()
                {
                    if (it.isSuccessful) {
                        it.result.storage.downloadUrl.addOnSuccessListener {
                            productmodel.imageurl = it.toString()
                            binding.productImage.setImageURI(fileUri)
                            binding.mainLayout.visibility = View.VISIBLE
                            binding.spinKit.visibility = View.GONE
                        }
                    }
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        binding.productImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.button.setOnClickListener {
            if (binding.itemDetails.text.isEmpty()) {
                binding.itemDetails.setError("Product name shouldn't be empty")
            }
            if (binding.priceDetails.text.isNullOrEmpty()) {
                binding.priceDetails.setError("Price shouldn't be empty")
            }
            if (binding.itemDescription.text.isNullOrEmpty()) {
                binding.itemDescription.setError("Description shouldn't be empty")
            }
            if (productmodel.imageurl.isEmpty()) {
                Toast.makeText(this, "Image not selected", Toast.LENGTH_LONG).show()
            } else {
                productmodel.itemName = binding.itemDetails.text.toString()
                productmodel.price = binding.priceDetails.text.toString().toDouble()
                productmodel.description = binding.itemDescription.text.toString()
                Toast.makeText(this, "Item Saved Sucessfully", Toast.LENGTH_LONG).show()
            }


        }

    }


}