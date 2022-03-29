package tk.maizbagwala.social.adapter;

import static tk.maizbagwala.social.util.Utils.copyFile;
import static tk.maizbagwala.social.util.Utils.copyFileFromUri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import tk.maizbagwala.social.R;
import tk.maizbagwala.social.model.Status;


public class ImageAdapter extends RecyclerView.Adapter<VideoAdapter.ItemViewHolder> {

    private final List<Status> imagesList;
    private Context context;
    private final RelativeLayout container;
    private LayoutInflater layoutInflater;


    public ImageAdapter(List<Status> imagesList, RelativeLayout container) {
        this.imagesList = imagesList;
        this.container = container;
    }

    @NonNull
    @Override
    public VideoAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        context = parent.getContext();
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        return new VideoAdapter.ItemViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.items_whatsapp_view, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull final VideoAdapter.ItemViewHolder holder, int position) {

        final Status status = imagesList.get(position);

//        holder.imageView.setOnClickListener(v -> {
//
//            final AlertDialog.Builder alertD = new AlertDialog.Builder(context);
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.view_image_full_screen, null);
//            alertD.setView(view);
//
//            ImageView imageView = view.findViewById(R.id.img);
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                Picasso.get().load(status.getFileUri()).into(imageView);
//            else
//                Picasso.get().load(status.getFile()).into(imageView);
//
//            AlertDialog alert = alertD.create();
//            alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
//            alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            alert.show();
//
//        });
//
//        holder.share.setOnClickListener(v -> {
//
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//
//            shareIntent.setType("image/jpg");
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                shareIntent.putExtra(Intent.EXTRA_STREAM, status.getFileUri());
//            else
//                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
//            context.startActivity(Intent.createChooser(shareIntent, "Share image"));
//
//        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            Glide.with(context)
                    .asBitmap()
                    .load(status.getFileUri())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.binding.pcw.setImageBitmap(resource);
                            holder.binding.tvDownload.setOnClickListener(v -> copyFileFromUri(status, context, container, resource));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

//            Picasso.get().load(status.getFileUri()).into(holder.imageView);

//            Picasso.get().load(status.getFileUri()).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
////                    holder.imageView.setImageBitmap(bitmap);
//                    holder.save.setOnClickListener(v -> Common.copyFileFromUri(status, context, container, bitmap));
//                }
//
//                @Override
//                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });

            return;
        }

//        Picasso.get().load(status.getFile()).into(holder.imageView);
        holder.binding.tvDownload.setOnClickListener(v -> copyFile(status, context, container));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
