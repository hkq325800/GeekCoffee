package com.example.administrator.geekcoffee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.List;

class StaggeredHomeAdapter extends
		RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder>
{
    private Context now;//this
    private LayoutInflater mInflater;//layout
    private List<Integer> mHeights;
    private List<AVObject> mResult;//一次查询后的缓存
    private Consumption mCon;
    private List<String> mDatas;//每一组商品的名称

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int pos);

		void onItemLongClick(View view, int pos);

        void onNumAddClick(View view, int pos, Consumption mCon);

        void onNumCutClick(View view, int pos, Consumption mCon);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener )
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public StaggeredHomeAdapter(Context context, List<String> datas, List<AVObject> result)
	{
        now = context;
        mInflater = LayoutInflater.from(context);
        mHeights = new ArrayList<Integer>();
        mResult = result;
        mDatas = datas;
        mCon = new Consumption(getItemCount());
		for (int i = 0; i < getItemCount(); i++)
		{
            mHeights.add((int) (350 + Math.random() * 300));
            mCon.initcoldNum();
            mCon.inithotNum();
            mCon.setmSum();
            for(int j = 0; j < 10; j++){
                mCon.setmDetailChild(i,j,0);
            }
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_staggered_home, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int pos)//每次重现调用
	{
		LayoutParams lp = holder.custom_cross.getLayoutParams();
		lp.height = mHeights.get(pos);
		holder.tv_item.setLayoutParams(lp);

		holder.tv_item.setText(mDatas.get(pos));
        if (mCon.getmSum(pos) == 0){//若未预定恢复初始态
            setType1(holder);
        } else {//有预定填充数据
            setType2(holder);
            fill(pos,holder);
        }

		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null)
		{
			holder.itemView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, pos);//view,position
				}
			});

			holder.itemView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
					removeData(pos);
					return false;
				}
			});

            holder.btn_cut.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onNumCutClick(holder.itemView, pos, mCon);//数据处理
                    if (mCon.getmSum(pos) == 0) {//恢复初始态
                        setType1(holder);
                    } else {//恢复上一次选择的选项
                        fill(pos,holder);
                    }
                }
            });

            holder.btn_add.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(now);
                    final int pos = holder.getLayoutPosition();
                    Boolean type = mResult.get(pos).getBoolean("type");//isDrink
                    if (mCon.getmSum(pos) == 0) {//设定为预定态
                        setType2(holder);
                    }
                    if (type) {//若为饮料弹出选择
                        dialogBuild(holder,pos,dialog);
                    } else {//若为蛋糕
                        holder.tv_sum.setText("共 " + (mCon.getmSum(pos) + 1) + " 个");
                    }
                    mOnItemClickLitener.onNumAddClick(holder.itemView, pos, mCon);
                }
            });
		}
	}

    private void dialogBuild(final MyViewHolder holder, final int pos, AlertDialog.Builder dialog) {
        dialog.setTitle("选择冷热");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCon.getmSum(pos) == 1) {
                    setType1(holder);
                }

                mCon.cutmSum(pos);
                mCon.cutmAmount();
            }
        });
        dialog.setPositiveButton("热", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.tv_temp.setText("热");
                holder.tv_sum.setText("共 " + mCon.getmSum(pos) + " 个");

                mCon.setmDetailChild(pos,mCon.getmSum(pos) - 1,2);
                mCon.addhotNum(pos);
            }
        });
        dialog.setNegativeButton("冷", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.tv_temp.setText("冷");
                holder.tv_sum.setText("共 " + mCon.getmSum(pos) + " 个");

                mCon.setmDetailChild(pos, mCon.getmSum(pos) - 1, 1);
                mCon.addcoldNum(pos);
            }
        });
        dialog.show();
    }

    private void fill(int pos, MyViewHolder holder) {
        switch (mCon.getmDetailChild(pos,mCon.getmSum(pos) - 1)){//恢复上一次选择的选项
            case 1:
                holder.tv_temp.setText("冷");
                break;
            case 2:
                holder.tv_temp.setText("热");
                break;
            case 0:
                break;
            default:
                break;
        }
        holder.tv_sum.setText("共 " + mCon.getmSum(pos) + " 个");
    }

    private void setType1(MyViewHolder holder){
        holder.btn_cut.setVisibility(View.INVISIBLE);
        holder.tv_sum.setVisibility(View.INVISIBLE);
        holder.tv_temp.setVisibility(View.INVISIBLE);
        holder.tv_temp.setText("");
        holder.tv_sum.setText("");
    }

    private void setType2(MyViewHolder holder){
        holder.btn_cut.setVisibility(View.VISIBLE);
        holder.tv_sum.setVisibility(View.VISIBLE);
        holder.tv_temp.setVisibility(View.VISIBLE);
    }

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}

	public void addData(int pos)//弹出一窗口供填写表单
	{
		mDatas.add(pos, "One");
		mHeights.add((int) (350 + Math.random() * 300));//3:100+Math.random()*300
        mCon.setmSum();
        mCon.inithotNum();
        mCon.initcoldNum();
        int[][] temp = new int[getItemCount()][10];
        System.arraycopy(mCon.getmDetail(), 0, temp, 0, mCon.getmDetail().length);//数组扩充
        mCon.setmDetail(temp);

        AVObject Menu = new AVObject("Menu");
        int price = (int) (Math.random() * 10 + 5);
        Menu.put("name", "One");
        Menu.put("price", price);
        Menu.put("type", false);
        try {
            Menu.save();//saveInBackground()后台保存
        } catch (AVException e) {
            Log.e("avosave", e.getMessage()); //捕获的异常信息
        } finally {
            mResult.add(Menu);//同步至缓存
        }

		notifyItemInserted(pos);
	}

	public void removeData(int pos)
	{
        mDatas.remove(pos);
        mHeights.remove(pos);
        mCon.removemSum(pos);
        mCon.removecoldNum(pos);
        mCon.removehotNum(pos);
        int temp[][]=new int[getItemCount()][10];
        for (int i = 0; i < getItemCount() ; i++){
            temp[i] = i < pos ? mCon.getmDetail()[i] : mCon.getmDetail()[i+1];
        }
        mCon.setmDetail(temp);
        AVObject del = mResult.get(pos);
        del.deleteInBackground();
        mResult.remove(pos);
		notifyItemRemoved(pos);
	}

    public ArrayList<String> getResult(){
        ArrayList<String> result = new ArrayList();
        for (int i = 0; i < getItemCount(); i++){
            if (mCon.getmSum(i) != 0){//区分是否有预定
                 if (mResult.get(i).getBoolean("type")){//drink
                     String hot;
                     String cold;
                     hot = mCon.gethotNum(i) != 0 ? "热：" + mCon.gethotNum(i) : "";
                     cold = mCon.getcoldNum(i) != 0 ? "冷：" + mCon.getcoldNum(i) : "";
                     result.add(mDatas.get(i) + hot + cold);
                 } else {
                     result.add(mDatas.get(i) + "总数：" + mCon.getmSum(i));
                 }
            }
        }
        return result;
    }



	class MyViewHolder extends ViewHolder
	{
        RelativeLayout custom_cross;
		TextView tv_item;
        TextView tv_sum;
        TextView tv_temp;
        Button btn_cut;
        Button btn_add;

		public MyViewHolder(View view)
		{
			super(view);
            custom_cross = (RelativeLayout) view.findViewById(R.id.cross);
            tv_item = (TextView) view.findViewById(R.id.tv_item);
            tv_sum = (TextView) view.findViewById(R.id.tv_sum);
            tv_temp = (TextView) view.findViewById(R.id.tv_temp);
            btn_cut = (Button) view.findViewById(R.id.btn_cut);
            btn_add = (Button) view.findViewById(R.id.btn_add);
            
		}
	}


}