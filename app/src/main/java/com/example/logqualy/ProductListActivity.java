package com.example.logqualy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.logqualy.model.Product;
import com.example.logqualy.ui.recyclerview.adapter.ProductAdapter;
import com.example.logqualy.ui.recyclerview.adapter.listener.ItemClickListener;
import com.example.logqualy.ui.recyclerview.helper.ProductItemTouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductListActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_EDIT_PRODUCT = 2;
    public static final String PRODUCTS_COLLECTION = "products";

    public static final String EXTRA_EDIT_PRODUCT = "editProduct";

    public static final String TAG = "SAVE_PRODUCT";
    private FirebaseUser user;
    private FloatingActionButton fabFormProduct;
    private FirebaseFirestore db;
    private List<Product> productList;
    private RecyclerView productRecyclerview;
    private ProductAdapter adapter;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        db = FirebaseFirestore.getInstance();

        productList = new ArrayList<>();

        loadData();

        fabFormProduct = findViewById(R.id.fabFormProduct);

        fabFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, FormProductActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void configRecyclerView() {
        productRecyclerview = findViewById(R.id.recyclerViewProduct);
        productRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList);
        productRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(Product product) {
                Intent intent = new Intent(ProductListActivity.this, FormProductActivity.class);
                intent.putExtra(EXTRA_EDIT_PRODUCT, product);
                startActivityForResult(intent,REQUEST_EDIT_PRODUCT);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ProductItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(productRecyclerview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == REQUEST_CODE && data.hasExtra(FormProductActivity.PRODUCT_SAVE)){
            if (resultCode == Activity.RESULT_OK){
                Product product = (Product) data.getSerializableExtra(FormProductActivity.PRODUCT_SAVE);
                byte[] byteArray1 = data.getByteArrayExtra("imagem");

                String nameFile = UUID.randomUUID().toString();
                product.setPhotoProduct(nameFile);

                db.collection(PRODUCTS_COLLECTION).add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mStorageRef = FirebaseStorage.getInstance().getReference("image/"+ nameFile+".jpg");
                        mStorageRef.putBytes(byteArray1);
                        loadData();
                    }
                });
            }
        }

        if (requestCode == REQUEST_EDIT_PRODUCT &&
                data.hasExtra(FormProductActivity.PRODUCT_EDIT)){
            if (resultCode == Activity.RESULT_OK){
                Product product = (Product)
                        data.getSerializableExtra(FormProductActivity.PRODUCT_EDIT);
                db.collection(PRODUCTS_COLLECTION).document(product.getId()).set(product);
                loadData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_logout:
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToLoginActivity() {
        startActivity(new Intent(
                this,
                MainActivity.class
        ));
        finish();
    }

    void loadData(){
        db.collection(PRODUCTS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productList.clear();
                            //Pegando os dados como lista de produtos
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                product.setId(document.getId());
                                productList.add(product);
                            }

                            //productList = task.getResult().toObjects(Product.class);
                            configRecyclerView();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}