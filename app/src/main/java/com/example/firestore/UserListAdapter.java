package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//  This class Applies to RecyclerView
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<UserVariable> userVariableList;

    public UserListAdapter(Context mCtx, List<UserVariable> userVariableList) {
        this.mCtx = mCtx;
        this.userVariableList = userVariableList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(
//              link with list item
                LayoutInflater.from(mCtx).inflate(R.layout.list_item, parent, false)
        );
    }

    //  this call after call above method  / bind data to view object / call one item by one item
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // in this get position and it set to userVariable
        UserVariable userVariable = userVariableList.get(position);

        holder.textViewName.setText(userVariable.getName().toString());
        holder.textViewEmail.setText(""+ userVariable.getEmail());
        holder.textViewPassword.setText(userVariable.getPassword());


    }

//   how many items in RecyclerView
    @Override
    public int getItemCount() {
        return userVariableList.size();
    }

   // link GUI component relevant to RecyclerView
    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName, textViewEmail, textViewPassword;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.nameView);
            textViewEmail = itemView.findViewById(R.id.emailView);
            textViewPassword = itemView.findViewById(R.id.passwordView);

            // What happens when you click an item
            itemView.setOnClickListener(this);
        }

       // What happens when you click item
        @Override
        public void onClick(View v) {
            UserVariable userVariable = userVariableList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx, UpdateUserActivity.class);
            intent.putExtra("Users", userVariable);
            mCtx.startActivity(intent);

        }
    }
}