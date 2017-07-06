package com.vtime.qqalbum.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vtime.qqalbum.R;
import com.vtime.qqalbum.adapter.AlbumAdapter;
import com.vtime.qqalbum.bean.AlbumBean;
import com.vtime.qqalbum.view.itemDecoration.EaseItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 纯净版
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;

    private List<AlbumBean> albumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        initData();

        init();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
    }

    private void init() {
        mAdapter = new AlbumAdapter(MainActivity.this, new ArrayList<AlbumBean>());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position, mAdapter.getDatas());
            }
        });
        EaseItemDecoration itemDecoration = new EaseItemDecoration(MainActivity.this, mAdapter.getDatas());
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setData(albumList);
        itemDecoration.setmDatas(mAdapter.getDatas());
    }

    private void initData() {
        int position = 1;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < position; j++) {
                AlbumBean albumBean = new AlbumBean();
                albumBean.setSubId(position + "");
                albumBean.setTitle("第" + position + "个标题");
                albumBean.setUrl(R.mipmap.img);
                albumList.add(albumBean);
            }
            position++;
        }
    }

    private int setSpanSize(int position, List<AlbumBean> listEntities) {
        int count;
        int d;
        if ((position + 1 < listEntities.size()) && position > 0) {
            if (!listEntities.get(position).getSubId().equals(listEntities.get(position + 1).getSubId())) {
                mAdapter.getItem(position + 1).value = 2 - (mAdapter.getItem(position).value + position) % 3 + mAdapter.getItem(position).value;
                d = 2 - (mAdapter.getItem(position).value + position) % 3;
                if (d == 2) {
                    count = 3;
                } else if (d == 1) {
                    count = 2;
                } else {
                    count = 1;
                }
            } else {
                mAdapter.getItem(position + 1).value = mAdapter.getItem(position).value;
                count = 1;
            }
        } else if (position == 0) {
            if (mAdapter.getDatas().size() > 1) {
                if ((!listEntities.get(position).getSubId().equals(listEntities.get(position + 1).getSubId()))) {
                    mAdapter.getItem(1).value = 2;
                    count = 3;
                } else {
                    count = 1;
                    mAdapter.getItem(1).value = 0;
                }
            } else {
                count = 1;
            }
        } else {
            count = 1;
        }
        return count;
    }

}
