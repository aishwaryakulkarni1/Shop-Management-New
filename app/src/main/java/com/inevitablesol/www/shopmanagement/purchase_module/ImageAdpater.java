package com.inevitablesol.www.shopmanagement.purchase_module;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.billing_module.Adapter.BHArrayAdapter;
import com.inevitablesol.www.shopmanagement.expenses.GetExpenseImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.provider.Settings.Global.AIRPLANE_MODE_ON;

/**
 * Created by Pritam on 02-04-2018.
 */

public class ImageAdpater  extends RecyclerView.Adapter<ImageAdpater.ViewHolder>
{

private ArrayList<String> listData;
private GetPurchaseImages getPurchaseImages;
    private static final String TAG = "ImageAdpater";
    private Context context;
  private  GetExpenseImages getExpenseImages;
    public ImageAdpater(Context applicationContext, GetPurchaseImages getPurchaseImages, ArrayList<String> imageArray)
    {
        this.context=applicationContext;
        Log.d(TAG, "ImageAdpater: ");
        this.getPurchaseImages=getPurchaseImages;
        this.listData=imageArray;
    }

    public ImageAdpater(Context applicationContext, GetExpenseImages getExpenseImages, ArrayList<String> imageArray)
    {
        this.context=applicationContext;
        this.getExpenseImages=getExpenseImages;
        this.listData=imageArray;

    }

    @Override
    public ImageAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_imageholder, parent, false);
          //  ViewHolder myViewHolder = new ViewHolder(view);
            return new ImageAdpater.ViewHolder(view);


    }
//    @Override
//    public PurchaseViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.v_list, viewGroup, false);
//        return new PurchaseViewAdapter.ViewHolder(view);
//    }

    @Override
    public void onBindViewHolder(final  ImageAdpater.ViewHolder holder, int position)
    {
        try {
            final  String data=listData.get(position);
            Log.d(TAG, "onBindViewHolder: Data"+data);
            ImageView imageView=holder.imageView;
            TextView textView=holder.textImage;
         //   holder.textImage.setText(data);
            Log.d(TAG, "onBindViewHolder: "+position);
            Log.d(TAG, "onBindViewHolder: List"+listData.get(position));

               // Picasso.with(context).load(listData.get(position)).resize(120, 60).into(imageView);
               Picasso.with(context).load(listData.get(position)).fit().centerInside().into(imageView);
//
//                Log.d(TAG, "onBindViewHolder: Else");
//                Log.d(TAG, "onBindViewHolder: "+listData.get(position));

        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     private ImageView imageView; TextView textImage;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
             //textImage = (TextView) itemView.findViewById(R.id.tv_imgText);
            imageView = (ImageView) itemView.findViewById(R.id.purchase_imageId);

        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() );
        }
    }

    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
    }
}
