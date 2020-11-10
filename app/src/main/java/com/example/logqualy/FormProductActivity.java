package com.example.logqualy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.logqualy.model.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class FormProductActivity extends AppCompatActivity {

    public static final String PRODUCT_SAVE = "PRODUCT_SAVE";
    public static final String PRODUCT_EDIT = "PRODUCT_EDIT";
    public static final String EXTRA_IMAGE_PATH = "image";

    private EditText mEditTextNameProduct;
    private EditText mEditTextDescriptionProduct;
    private EditText mEditTextDate;
    private ImageView mImageViewPhoto;
    private Button mButtonSave;
    private Intent intent;
    private Product product;
    private byte[] imageInByte;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        //adicionando o botão para voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadViews();
        clickButtons();
        mStorage = FirebaseStorage.getInstance();
        intent = getIntent();

        if (intent.hasExtra(ProductListActivity.EXTRA_EDIT_PRODUCT)){
            getSupportActionBar().setTitle("Editar Sinistros");
            product = (Product) intent.getSerializableExtra(ProductListActivity.EXTRA_EDIT_PRODUCT);
            loadForm();
        }
    }

    private void loadForm() {
        mEditTextNameProduct.setText(product.getNameProduct());
        mEditTextDescriptionProduct.setText(product.getDescriptionProduct());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickButtons() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.hasExtra(ProductListActivity.EXTRA_EDIT_PRODUCT)){
                    updateProductFromForm();
                    goToProductListActivity(PRODUCT_EDIT);
                }else{
                    getProductFromForm();
                    goToProductListActivity(PRODUCT_SAVE);
                }
            }
        });

        mImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "asdf",
                        Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
            }
        });
    }

    private void goToProductListActivity(String saveOrEditExtra) {
        Intent intent = new Intent(FormProductActivity.this,
                        ProductListActivity.class);
        //Esse extra envia a imagem para tela de lista de produtos
        intent.putExtra(EXTRA_IMAGE_PATH,imageInByte);

        //Esse extra envia um objeto Product para tela de lista de produtos
        intent.putExtra(saveOrEditExtra, product);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private void updateProductFromForm() {
        String nameProduct = mEditTextNameProduct.getText().toString();
        String descriptionProduct = mEditTextDescriptionProduct.getText().toString();
        product.setNameProduct(nameProduct);
        product.setDescriptionProduct(descriptionProduct);
    }

    private void getProductFromForm() {
        if(validateForm()) {
            String nameProduct = mEditTextNameProduct.getText().toString();
            String descriptionProduct = mEditTextDescriptionProduct.getText().toString();
            String date = mEditTextDate.getText().toString();
            String photoProduct = "adress image";

            Drawable drawable = mImageViewPhoto.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            product = new Product(nameProduct, descriptionProduct);
        }
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(mEditTextNameProduct.getText())){
            mEditTextNameProduct.setError("Informe o nome do produto");
            mEditTextNameProduct.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mEditTextDescriptionProduct.getText())){
            mEditTextDescriptionProduct.setError("Informe a descrição do produto");
            mEditTextNameProduct.requestFocus();
            return false;
        }

        return true;
    }

    private void loadViews() {
        mEditTextNameProduct =  findViewById(R.id.editTextProductName);
        mEditTextDescriptionProduct =  findViewById(R.id.editTextProductDescreption);
        mEditTextDate =  findViewById(R.id.editTextDate);
        mImageViewPhoto =  findViewById(R.id.imageViewPhotoProduct);
        mButtonSave =  findViewById(R.id.btnSaveProduct);
    }

    //Código de requisição da camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //Esse metodo criar e starta uma intent para abrir o App de camera do celular
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    //Quando o usuário finalizar a camera, o App voltará para o metodo abaixo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Pegando o extra para posterior manipulação
            Bundle extras = data.getExtras();

            //Pegando a imagem que vei da intent
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Carregando a imagem na ImageView
            mImageViewPhoto.setImageBitmap(imageBitmap);

            //Transformando a imagem em um arquivo JPGE
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //Transforando a imagem em um arrandy de bites
            imageInByte = stream.toByteArray();

        }
    }
}