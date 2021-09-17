package com.bintyblackbook.model

class WalletListModel:BaseResponseModel() {

    val data= ArrayList<WalletData>()
}

class WalletData{
    val id:Int=0
}
