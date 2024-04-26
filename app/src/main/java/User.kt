import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class User {
     var username: String = ""
     var password: String = ""
    var points = 0
     fun register() {
         val database = Firebase.database
         val myRef = database.getReference("accounts")
            myRef.child(username).setValue(this)

     }
}