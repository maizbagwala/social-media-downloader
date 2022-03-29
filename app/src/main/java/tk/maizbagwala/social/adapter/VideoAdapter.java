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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.internal.service.Common;

import java.util.List;

import tk.maizbagwala.social.R;
import tk.maizbagwala.social.databinding.ItemsWhatsappViewBinding;
import tk.maizbagwala.social.model.Status;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ItemViewHolder> {

    private final List<Status> videoList;
    private Context context;
    private final RelativeLayout container;
    private LayoutInflater layoutInflater;

    public VideoAdapter(List<Status> videoList, RelativeLayout container) {
        this.videoList = videoList;
        this.container = container;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        return new ItemViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.items_whatsapp_view, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        final Status status = videoList.get(position);
        holder.binding.ivPlay.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
            return;
        }

        holder.binding.tvDownload.setOnClickListener(v -> copyFile(status, context, container));

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemsWhatsappViewBinding binding;

        public ItemViewHolder(ItemsWhatsappViewBinding mbinding) {
            super(mbinding.getRoot());
            this.binding = mbinding;
        }
    }
}
