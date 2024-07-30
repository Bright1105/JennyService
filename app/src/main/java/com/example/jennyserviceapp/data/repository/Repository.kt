package com.example.jennyserviceapp.data.repository

import android.net.Uri
import com.example.jennyserviceapp.data.model.Checkout
import com.example.jennyserviceapp.data.model.CheckoutCancel
import com.example.jennyserviceapp.data.model.Feeds
import com.example.jennyserviceapp.data.model.JennyAccountInfo
import com.example.jennyserviceapp.data.model.Products
import com.example.jennyserviceapp.data.model.Promotions
import com.example.jennyserviceapp.data.model.SendNotify
import com.example.jennyserviceapp.data.model.UserAddress
import com.example.jennyserviceapp.data.model.UserInformation
import com.example.jennyserviceapp.data.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

interface Repository {

    suspend fun addProducts(title: String, price: Int, brand: String, description: String, itemType: String, itemAvailable: Int)

    suspend fun addImageToFirebaseStorageAndFireStore(
        imageUri: List<Uri>
    )


    suspend fun addFeed()

    suspend fun addVideoToFirebaseStorage(
        videoUri: Uri
    )
    suspend fun addPromotions(promotions: Promotions)
    suspend fun saveImageToStorage(imageUri: Uri?)
    fun getPromotions(): Flow<List<Promotions>>
    suspend fun deletePromotions(id: String)

    suspend fun sendNotify(sendNotify: SendNotify)

    val getCheckoutList: Flow<List<Checkout>>

    suspend fun getUserName(userId: String): UserInformation?
    suspend fun getUserAddress(userId: String): UserAddress?
    suspend fun getCheckoutById(id: String): Checkout?
    suspend fun deleteCheckout(checkout: Checkout)
    suspend fun addToUserCancelCheckout(checkoutCancel: CheckoutCancel)
    suspend fun updatePending(checkout: Checkout)
    suspend fun updateDelivered(checkout: Checkout)

    suspend fun createBankInfo(jennyAccountInfo: JennyAccountInfo)
    val getBankInfo: Flow<JennyAccountInfo?>
}


class RepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val accountService: AccountService
) : Repository {

    private var downloadUri: Uri? = null
    private var downloadUri1: Uri? = null
    private var promotionImage: Uri? = null

    private var videoUris: Uri? = null

    override suspend fun createBankInfo(jennyAccountInfo: JennyAccountInfo) {
        Firebase.firestore
            .collection(ACCOUNTINFO)
            .document("bank_infor_mat_ion@bank")
            .set(jennyAccountInfo)
            .await()
    }

    override val getBankInfo: Flow<JennyAccountInfo?> =
        Firebase.firestore
            .collection(ACCOUNTINFO)
            .document("bank_infor_mat_ion@bank")
            .dataObjects()

    override suspend fun getCheckoutById(id: String): Checkout? {
        return withContext(Dispatchers.IO) {
            val source = Source.SERVER
            Firebase.firestore
                .collection(CHECKOUT)
                .document(id)
                .get(source)
                .await()
                .toObject()
        }
    }

    override suspend fun deleteCheckout(checkout: Checkout) {
        return withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(CHECKOUT)
                .document(checkout.id)
                .delete()
                .await()
        }
    }

    override suspend fun addToUserCancelCheckout(checkoutCancel: CheckoutCancel) {
        return withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(CHECKOUTCANCELED)
                .add(checkoutCancel)
                .await()
        }
    }

    override suspend fun updatePending(checkout: Checkout) {
        return withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(CHECKOUT)
                .document(checkout.id)
                .set(checkout)
                .await()
        }
    }

    override suspend fun updateDelivered(checkout: Checkout) {
        return withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(CHECKOUT)
                .document(checkout.id)
                .set(checkout)
                .await()
        }
    }

    override suspend fun getUserName(userId: String): UserInformation? {
        return withContext(Dispatchers.IO) {
            val source = Source.SERVER
             Firebase.firestore
                .collection(USERINFORMATION)
                .document(userId)
                .get(source)
                .await()
                .toObject()
        }
    }

    override suspend fun getUserAddress(userId: String): UserAddress? {
        return withContext(Dispatchers.IO) {
            val source = Source.SERVER
            Firebase.firestore
                .collection(USERADDRESS)
                .document(userId)
                .get(source)
                .await()
                .toObject()
        }
    }

    override val getCheckoutList: Flow<List<Checkout>> =
        Firebase.firestore
            .collection(CHECKOUT)
            .dataObjects()

    override suspend fun sendNotify(sendNotify: SendNotify) {
        Firebase.firestore
            .collection(NOTIFY)
            .add(sendNotify)
            .await()
    }

    override suspend fun addProducts(
        title: String,
        price: Int,
        brand: String,
        description: String,
        itemType: String,
        itemAvailable: Int
    ) {
        val products = Products(
            name = title,
            brand = brand,
            price = price,
            description = description,
            itemType = itemType,
            itemAvailable = itemAvailable,
            imageUri = listOf(downloadUri, downloadUri1),
            dateCreated = Timestamp.now()
        )

        firestore.collection("products")
            .add(products).await()
    }

    override suspend fun addImageToFirebaseStorageAndFireStore(imageUri: List<Uri>) {

        withContext(Dispatchers.IO) {
            val storageRef = firebaseStorage.reference.child("products/images").child(UUID.randomUUID().toString())
            val uploadTask = storageRef.putFile(imageUri[0])

            val download = uploadTask.continueWithTask {
                if (!it.isSuccessful) {
                    throw it.exception!!
                }
                it.result.storage.downloadUrl
            }.await()

            downloadUri = download

            val storageRef1 = firebaseStorage.reference.child("products/images").child(UUID.randomUUID().toString())
            val uploadTask1 = storageRef1.putFile(imageUri[1])

            val download1 = uploadTask1.continueWithTask {
                if (!it.isSuccessful) {
                    throw it.exception!!
                }
                it.result.storage.downloadUrl
            }.await()

            downloadUri1 = download1
        }
    }

    override suspend fun saveImageToStorage(imageUri: Uri?) {

        withContext(Dispatchers.IO) {
            val storageRef = Firebase.storage.reference.child("promotions/image").child(UUID.randomUUID().toString())
            val uploadTask = imageUri?.let { storageRef.putFile(it) }

            val download = uploadTask?.continueWithTask {
                if (!it.isSuccessful) {
                    throw  it.exception!!
                }
                it.result.storage.downloadUrl
            }?.await()

            promotionImage = download
        }
    }

    override suspend fun addFeed() {
        val feed = Feeds(
            videoUri = videoUris
        )

        firestore.collection("feedUri").add(feed).await()
    }

    override suspend fun addPromotions(promotions: Promotions) {
        return withContext(Dispatchers.IO) {
            val promotion = promotions.copy(image = promotionImage.toString())
            Firebase.firestore
                .collection(PROMOTIONS)
                .add(promotion).await()

        }
    }

    override suspend fun addVideoToFirebaseStorage(videoUri: Uri) {

        withContext(Dispatchers.IO) {
            val storageRef = firebaseStorage.reference.child("videos").child(UUID.randomUUID().toString())
            val uploadVideoTask = storageRef.putFile(videoUri)

            val downloadVideo = uploadVideoTask.continueWithTask {
                if (!it.isSuccessful) {
                    throw it.exception!!
                }
                it.result.storage.downloadUrl
            }.await()

            videoUris = downloadVideo

        }
    }

    override fun getPromotions(): Flow<List<Promotions>> {
        return Firebase.firestore
            .collection(PROMOTIONS)
            .dataObjects()
    }

    override suspend fun deletePromotions(id: String) {
        Firebase.firestore
            .collection(PROMOTIONS)
            .document(id)
            .delete()
    }

    companion object {
        private const val PROMOTIONS = "promotions"
        private const val NOTIFY = "notification"
        private const  val CHECKOUT = "checkout"
        private const val USERINFORMATION = "userInformation"
        private const val USERADDRESS = "userAddress"
        private const val CHECKOUTCANCELED = "checkoutCanceled"
        private const val ACCOUNTINFO = "accountInfo"
    }
}