package com.efortunetech.lau.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.adapter.ProductAdapter;
import com.efortunetech.lau.demo.bean.ProductBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yq06 on 2018/7/13.
 */

public class ProductFragment extends Fragment {
    private TextView title;
    private RecyclerView productRecyclerView;
    private Activity mActivity;
    private List<ProductBean> productBeansList;

    public static final ProductFragment newInstance(int position) {
        ProductFragment nf = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        nf.setArguments(bundle);
        return nf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, null);
        title = view.findViewById(R.id.title);
        productRecyclerView = view.findViewById(R.id.productList);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
            title.setText("fragment " + position);
        }
        productRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        productBeansList = new ArrayList<>();

        int[] imgs = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6, R.drawable.pic5, R.drawable.pic4, R.drawable.pic3, R.drawable.pic2, R.drawable.pic1};
        String[] names = {"举杯呵呵喝2 第二季：张靓颖畅聊心事",
                "这！就是灌篮 第一季：王俊凯版“流川枫”上线 3分球一投一个准",
                "德云社岳云鹏相声专场北美巡演纽约站 2018：岳云鹏与刘昊然 雷佳音比脑袋大小",
                "法国国庆频现“乌龙”: 摩托车相撞 飞机喷出俄罗斯国旗",
                "再获重大进展! 俄叙即将发起总攻, 关键时刻以色列不再支持叛军",
                "这！就是世界波 2018：梅西C罗出局王自健落泪",
                "再获重大进展! 俄叙即将发起总攻, 关键时刻以色列不再支持叛军",
                "法国国庆频现“乌龙”: 摩托车相撞 飞机喷出俄罗斯国旗",
                "德云社岳云鹏相声专场北美巡演纽约站 2018：岳云鹏与刘昊然 雷佳音比脑袋大小",
                "这！就是灌篮 第一季：王俊凯版“流川枫”上线 3分球一投一个准",
                "举杯呵呵喝2 第二季：张靓颖畅聊心事"
        };
        String[] descs = {"《举杯呵呵喝》第二季强势归来！海泉、杨迪、沈南和本季新晋主MC大左组成的新\"胡喝帮\"在第一期招待了华语乐坛超级唱将张靓颖，各位嘉宾大展模仿才能，轮番爆料入行以来的各种趣事趣料！张靓颖更是大谈和母亲的另类相处模式，走心寄语自己和粉丝！更多精彩内容，敬请收看《举杯呵呵喝》第二季！",
                "《这！就是灌篮》是由优酷、天猫出品，联手日月星光制作的运动偶像燃魂真人秀。节目嘉宾除华裔球员林书豪和篮球传奇巨星马布里之外，演艺圈的篮球高手周杰伦也将跨界惊喜加盟。节目网罗热血篮球高手，通过层层考验，最终打造一支最青春热血的灌篮天团。",
                "《德云社岳云鹏相声专场北美巡演纽约站 》是德云社当红相声演员岳云鹏在纽约的相声专场演出。节目内容丰富，颇具“德云特色”。岳云鹏联合德云社多位相声艺人倾力轮番上阵，一起将爆笑进行到底。",
                "法国国庆频现“乌龙”: 摩托车相撞 飞机喷出俄罗斯国旗。",
                "再获重大进展! 俄叙即将发起总攻, 关键时刻以色列不再支持叛军。",
                "《这！就是世界波》是优酷出品、优制娱乐承制的一档世界杯主题直播节目。赛前一小时，明星陪你趣玩世界杯！节目每期以一个世界杯热点话题为主题，通过畅聊足球话题和球迷趣味互动，达到普及世界杯知识、传播足球文化的目的，打造一档集专业性、话题性和趣味性于一体的世界杯主题直播节目。",
                "再获重大进展! 俄叙即将发起总攻, 关键时刻以色列不再支持叛军。",
                "法国国庆频现“乌龙”: 摩托车相撞 飞机喷出俄罗斯国旗。",
                "《德云社岳云鹏相声专场北美巡演纽约站 》是德云社当红相声演员岳云鹏在纽约的相声专场演出。节目内容丰富，颇具“德云特色”。岳云鹏联合德云社多位相声艺人倾力轮番上阵，一起将爆笑进行到底。",
                "《这！就是灌篮》是由优酷、天猫出品，联手日月星光制作的运动偶像燃魂真人秀。节目嘉宾除华裔球员林书豪和篮球传奇巨星马布里之外，演艺圈的篮球高手周杰伦也将跨界惊喜加盟。节目网罗热血篮球高手，通过层层考验，最终打造一支最青春热血的灌篮天团。",
                "《举杯呵呵喝》第二季强势归来！海泉、杨迪、沈南和本季新晋主MC大左组成的新\"胡喝帮\"在第一期招待了华语乐坛超级唱将张靓颖，各位嘉宾大展模仿才能，轮番爆料入行以来的各种趣事趣料！张靓颖更是大谈和母亲的另类相处模式，走心寄语自己和粉丝！更多精彩内容，敬请收看《举杯呵呵喝》第二季！"
        };

        for (int i = 0; i < imgs.length; i++) {
            ProductBean bean = new ProductBean();
            bean.productImg = imgs[i];
            bean.productName = names[i];
            bean.productDesc = descs[i];
            productBeansList.add(bean);
        }
        ProductAdapter adapter = new ProductAdapter(mActivity, productBeansList);
        productRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(mActivity, "點擊了position=" + position, Toast.LENGTH_SHORT).show();
                ProductFragmentListener pfl = (ProductFragmentListener) mActivity;
                if (pfl != null) {
//                    pfl.showProductDetail(position);
                    pfl.showProductDetail_static(productBeansList.get(position).productName, productBeansList.get(position).productDesc);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    public interface ProductFragmentListener {
        void showProductDetail(int productId);

        void showProductDetail_static(String title, String desc);
    }
}
