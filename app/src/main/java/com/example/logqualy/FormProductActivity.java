package com.example.logqualy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.logqualy.model.Product;

public class FormProductActivity extends AppCompatActivity {

    public static final String PRODUCT_SAVE = "PRODUCT_SAVE";
    public static final String PRODUCT_EDIT = "PRODUCT_EDIT";
    private EditText mEditTextNameProduct;
    private EditText mEditTextDescriptionProduct;
    private EditText mEditTextDate;
    private ImageView mImageViewPhoto;
    private Button mButtonSave;
    private Intent intent;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        //adicionando o botão para voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadViews();
        clickButtons();

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
    }

    private void goToProductListActivity(String saveOrEditExtra) {
        Intent intent = new Intent(FormProductActivity.this,
                        ProductListActivity.class);
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

            product = new Product(nameProduct, descriptionProduct, date, photoProduct);
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
}