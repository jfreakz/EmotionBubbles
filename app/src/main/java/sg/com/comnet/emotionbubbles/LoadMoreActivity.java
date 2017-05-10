package sg.com.comnet.emotionbubbles;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadMoreActivity extends AppCompatActivity implements LoadMoreListView.OnLoadMoreListener {
    CustomAdapter adapter;
    private LoadMoreListView mListView;
    private List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);
        View mHeaderView = LayoutInflater.from(this).inflate(R.layout.head_view, null);
        TextView tab1 = (TextView) mHeaderView.findViewById(R.id.tab1);
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.clear();
                init();
            }
        });
        TextView tab2 = (TextView) mHeaderView.findViewById(R.id.tab2);
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.clear();
                init();
            }
        });
        mListView = (LoadMoreListView) findViewById(R.id.listview);
        mListView.addHeaderView(mHeaderView);
        init();

    }

    private void init() {

        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        mData.add("jojipoojg");
        adapter = new CustomAdapter(getApplicationContext(), mData);//new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mData);
        mListView.setAdapter(adapter);
        mListView.setOnLoadMoreListener(this);
//        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onloadMore() {
//                loadMore();
//            }
//        });
    }

    private void loadMore() {
        final Random random = new Random();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mData.size() > 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                        mListView.setLoadCompleted();
                            mListView.onLoadMoreComplete();
                        }
                    });
                    return;
                }
                mData.add("atate" + random.nextInt(500) + 50);
                mData.add("afasdf");
                mData.add("wertr");
                mData.add("zvxc");
                mData.add("42wrt");
                mData.add("jhklkj");

                if (mData.size() == 99)
                    mData.add("jhklkjadfasdfaadfad");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
//                        mListView.setLoadCompleted();
                        mListView.onLoadMoreComplete();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }


    public class CustomAdapter extends BaseAdapter {
        Context context;
        List<String> mData;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, List<String> mData) {
            this.context = applicationContext;
            this.mData = mData;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return mData.size() % 3 == 0 ? mData.size() / 3 : mData.size() / 3 + 1;
        }

        @Override
        public Object getItem(int i) {
            if (i < mData.size())
                return mData.get(i);
            else
                return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = inflter.inflate(R.layout.row_item, null);
                viewHolder = new ViewHolder();
                viewHolder.icon = (TextView) view.findViewById(R.id.icon);
                viewHolder.icon2 = (TextView) view.findViewById(R.id.icon2);
                viewHolder.icon3 = (TextView) view.findViewById(R.id.icon3);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.icon.setText((String) getItem(i * 3));
//            viewHolder.icon.setText(mData.get(i*3));
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            String s = (String) getItem(i * 3 + 1);
            if (s != null) {
                viewHolder.icon2.setText(s);
//            viewHolder.icon2.setText(mData.get(i*3+1));
                viewHolder.icon2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            s = (String) getItem(i * 3 + 2);
            if (s != null) {
                viewHolder.icon3.setText(s);
//            viewHolder.icon3.setText(mData.get(i*3+2));
                viewHolder.icon3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return view;
        }


        class ViewHolder {
            TextView icon, icon2, icon3;
        }
    }
}