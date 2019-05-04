package com.anikmohammad.tryoutflickr;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null) {
            Resources resources = getResources();
            TextView title = findViewById(R.id.photo_title);
            TextView author = findViewById(R.id.author);
            TextView tags = findViewById(R.id.photo_tags);
            ImageView photoImage = findViewById(R.id.photo);
            title.setText(resources.getString(R.string.photo_title_text, photo.getTitle()));
            author.setText(resources.getString(R.string.photo_author_text, photo.getAuthor()));
            tags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));
            Picasso.get().load(photo.getLink())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}
