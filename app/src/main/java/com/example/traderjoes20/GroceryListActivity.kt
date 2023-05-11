package com.example.traderjoes20

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.traderjoes20.databinding.ActivityListgroceryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import android.app.AlertDialog

class GroceryListActivity : AppCompatActivity() {

private lateinit var binding: ActivityListgroceryBinding
private lateinit var itemView: ListView
private lateinit var addBtn: Button
private lateinit var itemEdt: EditText

private lateinit var database: DatabaseReference
private lateinit var databaseUsers: DatabaseReference
private lateinit var databaseGroups: DatabaseReference
private lateinit var groupRef: DatabaseReference
private lateinit var members: DatabaseReference

private lateinit var userId: String
private lateinit var makeGroup: Button
private lateinit var joinGroup: Button
private lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListgroceryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // on below line we are initializing our variables.
        itemView = findViewById(R.id.userlist)
        addBtn = findViewById(R.id.button)
        itemEdt = findViewById(R.id.AddItem)
        makeGroup = findViewById(R.id.makeGroup)
        joinGroup = findViewById(R.id.joinGroup)
        back = findViewById(R.id.backToPantry)

        database = FirebaseDatabase.getInstance().reference
        databaseGroups = FirebaseDatabase.getInstance().reference

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        val itemList = mutableListOf<String>()

        // on below line we are initializing adapter for our list view.
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@GroceryListActivity,
            android.R.layout.simple_list_item_1,
            itemList as List<String?>
        )
        // on below line we are setting adapter for our list view.
        itemView.adapter = adapter


        //--------------------------------------------------------
        //retrieves info from database
        databaseUsers = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)

        //--------------------------------------------------------



        // Retrieve user's group information/ and if they don't have a group too
        retrieveGroceryList(itemList, adapter)
        //----------------------------

        //-----------------

        //REMOVING ITEM FROM FIREBASE
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isInGroup = dataSnapshot.child("groupCode").exists()
                if (isInGroup) {
                    // User is in a group
                    val groupCode = dataSnapshot.child("groupCode").value.toString()
                    val groupGroceryListRef = FirebaseDatabase.getInstance().reference
                        .child("groups")
                        .child(groupCode)
                        .child("grocery-list")

                    itemView.setOnItemClickListener { _, _, position, _ ->
                        val clickedItem = adapter.getItem(position)

                        val builder = AlertDialog.Builder(this@GroceryListActivity)
                        builder.setTitle("Delete Item")
                        builder.setMessage("Are you sure you want to delete this item?")
                        builder.setPositiveButton("Delete") { _, _ ->
                            Toast.makeText(this@GroceryListActivity, "Item deleted!", Toast.LENGTH_SHORT).show()

                            val itemsQuery = groupGroceryListRef.orderByValue().equalTo(clickedItem)
                            itemsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (itemSnapshot in dataSnapshot.children) {
                                        itemSnapshot.ref.removeValue()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.e("Delete Item", "Failed to delete item", databaseError.toException())
                                }
                            })
                        }

                        builder.setNegativeButton("Cancel") { _, _ ->
                            Toast.makeText(this@GroceryListActivity, "Deletion canceled!", Toast.LENGTH_SHORT).show()
                        }

                        builder.create().show()
                    }
                } else {
                    // User is not in a group, use user's grocery list
                    val userGroceryListRef = FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(userId)
                        .child("grocery-list")

                    itemView.setOnItemClickListener { _, _, position, _ ->
                        val clickedItem = adapter.getItem(position)

                        val builder = AlertDialog.Builder(this@GroceryListActivity)
                        builder.setTitle("Delete Item")
                        builder.setMessage("Are you sure you want to delete this item?")
                        builder.setPositiveButton("Delete") { _, _ ->
                            Toast.makeText(this@GroceryListActivity, "Item deleted!", Toast.LENGTH_SHORT).show()

                            val itemsQuery = userGroceryListRef.orderByValue().equalTo(clickedItem)
                            itemsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (itemSnapshot in dataSnapshot.children) {
                                        itemSnapshot.ref.removeValue()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.e("Delete Item", "Failed to delete item", databaseError.toException())
                                }
                            })
                        }

                        builder.setNegativeButton("Cancel") { _, _ ->
                            Toast.makeText(this@GroceryListActivity, "Deletion canceled!", Toast.LENGTH_SHORT).show()
                        }

                        builder.create().show()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "userRef:onCancelled", databaseError.toException())
            }
        })

        //----------------- END OF CLICK ON ITEM TO DELETE

        //BUTTON FUNCTIONS
        // on below line we are adding click listener for our button. - need to fix
        addBtn.setOnClickListener {
            val item = itemEdt.text.toString()
            if (item.isNotEmpty()) {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val isInGroup = dataSnapshot.child("groupCode").exists()
                        if (isInGroup) {
                            // User is in a group, add the item to the group's grocery-list node
                            val groupCode = dataSnapshot.child("groupCode").value.toString()
                            val groupGroceryListRef = FirebaseDatabase.getInstance().reference.child("groups")
                                .child(groupCode)
                                .child("grocery-list")
                            val newItemRef = groupGroceryListRef.push()
                            newItemRef.setValue(item)

                            // Remove "No Grocery Shopping Needed" entry if it exists
                            itemList.remove("No Grocery Shopping Needed")

                        } else {
                            // User is not in a group, add the item to the user's grocery-list node
                            val userGroceryListRef = FirebaseDatabase.getInstance().reference.child("users")
                                .child(userId)
                                .child("grocery-list")
                            val newItemRef = userGroceryListRef.push()
                            newItemRef.setValue(item)

                            // Remove "No Grocery Shopping Needed" entry if it exists
                            itemList.remove("No Grocery Shopping Needed")
                        }

                        // Clear the input field after adding the item
                        itemEdt.text.clear()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TAG", "userRef:onCancelled", databaseError.toException())
                    }
                })
            }
        }
        //END OF ADD TO LIST BUTTON
        //---------------------

        //need to add a button to go back to the pantry list
        back.setOnClickListener{
            val intent = Intent(this, MyPantryActivity::class.java)
            startActivity(intent)
        }

        //end of "share" button

        //------------
        databaseGroups = FirebaseDatabase.getInstance().reference
            .child("groups")
        //------------


        //Making Group BUTTON
        makeGroup.setOnClickListener {
            // Create a dialog to prompt for group name
            val dialogBuilder = AlertDialog.Builder(this)
            val groupNameInput = EditText(this)
            dialogBuilder.setTitle("Create Group")
                .setMessage("Enter group name:")
                .setView(groupNameInput)
                .setPositiveButton("Create") { dialog, _ ->
                    val groupName = groupNameInput.text.toString().trim()
                    if (groupName.isNotEmpty()) {
                        //send groupName to method that sends to firebase
                        createGroup(groupName,itemList,adapter)
                    } else {
                        Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } //end of make group

        //NEED TO FIX
        joinGroup.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val groupCodeInput = EditText(this)
            dialogBuilder.setTitle("Join Group")
                .setMessage("Enter group code:")
                .setView(groupCodeInput)
                .setPositiveButton("Join") { dialog, _ ->
                    val groupCode = groupCodeInput.text.toString().trim()
                    if (groupCode.isNotEmpty()) {
                        // Call a function to join the group using the group code
                        joinGroupWithCode(groupCode, itemList, adapter)
                    } else {
                        Toast.makeText(this, "Please enter a group code", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }//end of On-Create

    //Retrieve Grocery-List


    private fun retrieveGroceryList(itemList: MutableList<String>, adapter: ArrayAdapter<String?>) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isInGroup = dataSnapshot.child("groupCode").exists()

                val groceryListRef = if (isInGroup) {
                    // User is in a group, retrieve grocery list from the group's grocery-list node
                    val groupCode = dataSnapshot.child("groupCode").value.toString()
                    FirebaseDatabase.getInstance().reference.child("groups")
                        .child(groupCode)
                        .child("grocery-list")
                } else {
                    // User is not in a group, retrieve grocery list from the user's grocery-list node
                    FirebaseDatabase.getInstance().reference.child("users")
                        .child(userId)
                        .child("grocery-list")
                }

                groceryListRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val updatedItemList = mutableListOf<String>()

                        for (ds in dataSnapshot.children) {
                            val item = ds.getValue(String::class.java)
                            item?.let {
                                updatedItemList.add(it)
                            }
                        }
                        // Update the UI with the retrieved grocery list
                        itemList.clear()
                        itemList.addAll(updatedItemList)
                        // Check if the grocery list is empty
                        if (itemList.isEmpty()) {
                            itemList.add("No Grocery Shopping Needed")
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TAG", "groceryListRef:onCancelled", databaseError.toException())
                    }
                })

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "userRef:onCancelled", databaseError.toException())
            }
        })
    }

    //end of retrieving grocery-list

    //function to join group

    private fun joinGroupWithCode(groupCode: String, itemList: MutableList<String>, adapter: ArrayAdapter<String?>) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        // Check if the group with the given code exists
        val groupRef = FirebaseDatabase.getInstance().reference.child("groups").child(groupCode)
        groupRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val groupName = dataSnapshot.child("GroupInfo").child("GroupName").getValue(String::class.java)

                    // Save the group information to the user's data in Firebase
                    val userGroupData = hashMapOf<String, Any>(
                        "groupCode" to groupCode,
                        "GroupName" to groupName!!
                    )
                    userRef.updateChildren(userGroupData)
                        .addOnSuccessListener {
                            // User associated with the group successfully
                            Toast.makeText(this@GroceryListActivity, "Joined $groupName group!", Toast.LENGTH_SHORT).show()
                            // Retrieve the updated grocery list
                            retrieveGroceryList(itemList, adapter)
                            // Add user's name to the group's members list
                            //add later FIX
                            // Refresh the ListView with the new group information
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            // Error associating the user with the group
                            Toast.makeText(this@GroceryListActivity, "Failed to join the group: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // The group with the given code does not exist
                    Toast.makeText(this@GroceryListActivity, "Invalid group code", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error occurred while retrieving group data
                Toast.makeText(this@GroceryListActivity, "Failed to join the group: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    //end of function to join group
    //----------------------
    // Function to create a new group
    private fun createGroup(groupName: String, itemList: MutableList<String>, adapter: ArrayAdapter<String?>) {
        // Generate a unique group code or use an existing mechanism to create a group code
        val groupCode = generateGroupCode()

        // Save the group information to Firebase
        val groupData = hashMapOf<String, Any>(
            "groupCode" to groupCode,
            "GroupName" to groupName
        )

        groupRef = databaseGroups.child(groupCode).child("GroupInfo")
        groupRef.setValue(groupData as Map<String, Any>)
            .addOnSuccessListener {
                // Group created successfully, now associate the user with the group
                val userGroupData = hashMapOf<String, Any>(
                    "groupCode" to groupCode,
                    "GroupName" to groupName
                )

                //database_users already has userID
                //sends the group info to the USER SECTION in the database
                val userGroupRef = databaseUsers
                userGroupRef.updateChildren(userGroupData)
                    .addOnSuccessListener {
                        // User associated with the group successfully
                        Toast.makeText(this, "$groupName has been created! Group code: $groupCode", Toast.LENGTH_SHORT).show()
                        // Clear the grocery list and notify the adapter to update the list view
                        itemList.clear()

                        if (itemList.isEmpty()) {
                            itemList.add("No Grocery Shopping Needed")
                        }
                        adapter.notifyDataSetChanged()

                        // Retrieve the updated grocery list
                        retrieveGroceryList(itemList, adapter)

                        //add message thing to send the code?
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"

                        intent.putExtra(Intent.EXTRA_TEXT, "Ask Friends and Family to join\n Use Code: $groupCode")

                        startActivity(Intent.createChooser(intent, "Share grocery list with"))

                    }
                    .addOnFailureListener { exception ->
                        // Error associating the user with the group
                        Toast.makeText(this, "Failed to associate user with the group: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                // Error creating the group
                Toast.makeText(this, "Failed to create group: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        //need to create a members list as well with the user's name saved
        val userRef = databaseUsers
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userName = dataSnapshot.child("UserInfo").child("fullName").getValue(String::class.java)

                if (userName != null) {
                    members = databaseGroups.child(groupCode).child("GroupInfo").child("Members")
                    val memberData = HashMap<String, Any>()
                    memberData["name"] = userName

                    members.updateChildren(memberData)
                        .addOnSuccessListener {
                            // User associated with the group successfully
                            Toast.makeText(this@GroceryListActivity, "$userName has been added to $groupName", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            // Error associating the user with the group
                            Toast.makeText(this@GroceryListActivity, "Failed to associate user with the group: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Handle case where fullName is null
                    Toast.makeText(this@GroceryListActivity, "Full name is not available", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(this@GroceryListActivity, "Failed to retrieve full name: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
            //end of adding members to list
        })



    }
//end of createGroup

    //create a random code
    private fun generateGroupCode(): String {
        val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val numbers = "0123456789"

        val code = mutableListOf<Char>()
        // Add 4 random letters
        repeat(4) {
            val randomIndex = (letters.indices).random()
            code.add(letters[randomIndex])
        }
        // Add 4 random numbers
        repeat(4) {
            val randomIndex = (numbers.indices).random()
            code.add(numbers[randomIndex])
        }
        // Shuffle the code to randomize the order
        code.shuffle()
        // Convert the list of characters to a string and return
        return code.joinToString("")
    }

}

