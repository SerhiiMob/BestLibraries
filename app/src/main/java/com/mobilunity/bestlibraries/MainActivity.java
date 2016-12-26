package com.mobilunity.bestlibraries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView (R.id.text_view) TextView textView;
    @BindView(R.id.button) Button button;
    @BindView(R.id.grid_view_1) GridView gridView1;
    @BindView(R.id.grid_view_2) GridView gridView2;
    @BindString (R.string.text) String text;

    private ImageAdapter imageAdapter1;
    private ImageAdapter imageAdapter2;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        imageAdapter1 = new ImageAdapter(this);
        imageAdapter2 = new ImageAdapter(this, getEmojiUrls(realm.where(Emoji.class).findAll()));

        gridView1.setAdapter(imageAdapter1);
        gridView2.setAdapter(imageAdapter2);

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addEmojiToList(imageAdapter1.getItem(i).toString());
            }
        });

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeEmojiFromList(imageAdapter2.getItem(i).toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }

    @OnClick (R.id.button)
    public void onButtonClick() {
        textView.setText(text);

        RestClient restClient = ServiceGenerator.createService(RestClient.class);

        Call<Map<String, String>> request = restClient.getEmojis();
        request.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                imageAdapter1.setData(new ArrayList<>(response.body().values()));
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private List<String> getEmojiUrls(final RealmResults<Emoji> emojis) {
        List<String> result = new ArrayList<>();

        if (emojis.size() > 0) {
            for (Emoji item : emojis){
                result.add(item.getImageUrl());
            }
        }

        return result;
    }

    private void addEmojiToList(String imageUrl) {
        final RealmResults<Emoji> existEmojis = realm.where(Emoji.class).equalTo("imageUrl", imageUrl).findAll();
        if (existEmojis.size() > 0) return;

        realm.beginTransaction();
        Emoji emoji = realm.createObject(Emoji.class);
        emoji.setImageUrl(imageUrl);
        realm.commitTransaction();

        imageAdapter2.addItem(imageUrl);
    }

    private void removeEmojiFromList(String imageUrl) {
        imageAdapter2.deleteItem(imageUrl);

        realm.beginTransaction();
        final RealmResults<Emoji> existEmojis = realm.where(Emoji.class).equalTo("imageUrl", imageUrl).findAll();
        if (existEmojis.size() == 0) return;
        existEmojis.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
