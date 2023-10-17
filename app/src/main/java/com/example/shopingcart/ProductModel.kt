package com.example.shopingcart

class ProductModel {

    var itemName : String =""
    var price : Double = 0.0
    var  description : String =""
    var imageurl : String =""



    constructor(itemName: String, price: Double, description: String, imageurl: String) {
        this.itemName = itemName
        this.price = price
        this.description = description
        this.imageurl = imageurl
    }

    constructor()

}