package com.example.logqualy.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logqualy.ProductListActivity;
import com.example.logqualy.R;
import com.example.logqualy.model.Product;
import com.example.logqualy.ui.recyclerview.adapter.listener.ItemClickListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private ItemClickListener onItemClickListener;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.mergeViewData(product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextProduct;
        private TextView mTextDescription;
        private TextView mTextDate;
        private ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextProduct =  itemView.findViewById(R.id.itemtextViewProduct);
            mTextDescription =  itemView.findViewById(R.id.itemTextViewDescription);
            mTextDate =  itemView.findViewById(R.id.itemTextViewData);
            mImageView = itemView.findViewById(R.id.itemImageViewProduct);
        }

        void mergeViewData(Product product){
            mTextProduct.setText(product.getNameProduct());
            mTextDescription.setText(product.getDescriptionProduct());
            mTextDate.setText(product.getDate());
        }
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void removeProduct(int adapterPosition) {
        Product product = productList.get(adapterPosition);
        FirebaseFirestore.getInstance()
                .collection(ProductListActivity.PRODUCTS_COLLECTION)
                .document(product.getId())
                .delete();
        productList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
}
