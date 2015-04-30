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

        void onNumAddClick(View view, int pos);

        void onNumCutClick(View view, int pos);
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
        mResult=result;
        mDatas=datas;
        mCon=new Consumption();
        /*mSum = new ArrayList<Integer>();
        mdetail=new int[getItemCount()][10];*/
		for (int i = 0; i < getItemCount(); i++)
		{
            mHeights.add( (int) (350 + Math.random() * 300));
            mCon.initcoldNum();
            mCon.inithotNum();
            mCon.addmSum();
            for(int j=0;j<10;j++){
                mCon.getmdetail()[i][j]=0;
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
        if(mCon.getmSum(pos)==0){
            holder.tv_temp.setVisibility(View.INVISIBLE);
            holder.tv_sum.setVisibility(View.INVISIBLE);
            holder.btn_cut.setVisibility(View.INVISIBLE);
            holder.tv_sum.setText("");
            holder.tv_temp.setText("");
        }else{
            holder.tv_temp.setVisibility(View.VISIBLE);
            holder.tv_sum.setVisibility(View.VISIBLE);
            holder.btn_cut.setVisibility(View.VISIBLE);
            switch (mCon.getmdetail()[pos][mCon.getmSum(pos)-1]){//恢复上一次选择的选项
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
            holder.tv_sum.setText("共 "+mCon.getmSum(pos)+" 个");
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
					mOnItemClickLitener.onItemClick(holder.itemView, pos);
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
                    mOnItemClickLitener.onNumCutClick(holder.itemView, pos);

                    if(mCon.getmSum(pos)==1){
                        holder.btn_cut.setVisibility(View.INVISIBLE);
                        holder.tv_sum.setVisibility(View.INVISIBLE);
                        holder.tv_temp.setVisibility(View.INVISIBLE);
                        holder.tv_temp.setText("");
                    }else{
                        switch (mCon.getmdetail()[pos][mCon.getmSum(pos)-2]){//恢复上一次选择的选项
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
                    }
                    mCon.getmdetail()[pos][mCon.getmSum(pos)-1]=0;
                    mCon.cutmAmount();
                    mCon.setmSum(pos, mCon.getmSum(pos) - 1);//减少定的个数
                    if(mCon.getmSum(pos)==0){
                        holder.tv_sum.setText("");
                    }else{
                        holder.tv_sum.setText("共 "+mCon.getmSum(pos)+" 个");
                    }
                }
            });

            holder.btn_add.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    final int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onNumAddClick(holder.itemView, pos);

                    Boolean type = mResult.get(pos).getBoolean("type");
                    if(mCon.getmSum(pos)==0){
                        holder.btn_cut.setVisibility(View.VISIBLE);
                        holder.tv_sum.setVisibility(View.VISIBLE);
                        holder.tv_temp.setVisibility(View.VISIBLE);
                    }
                    if(type){//若为饮料弹出选择
                        AlertDialog.Builder dialog = new AlertDialog.Builder(now);
                        dialog.setTitle("选择冷热");
                        dialog.setCancelable(true);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if (mCon.getmSum(pos) == 0) {
                                    holder.btn_cut.setVisibility(View.INVISIBLE);
                                    holder.tv_sum.setVisibility(View.INVISIBLE);
                                    holder.tv_temp.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                        dialog.setPositiveButton("热", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.tv_temp.setText("热");
                                mCon.getmdetail()[pos][mCon.getmSum(pos) - 1] = 2;
                                holder.tv_sum.setText("共 " + mCon.getmSum(pos) + " 个");
                                mCon.addhotNum(pos);
                            }
                        });
                        dialog.setNegativeButton("冷", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.tv_temp.setText("冷");
                                mCon.getmdetail()[pos][mCon.getmSum(pos) - 1] = 1;
                                holder.tv_sum.setText("共 " + mCon.getmSum(pos) + " 个");
                                mCon.addcoldNum(pos);
                            }
                        });
                        dialog.show();
                    }else{//若为蛋糕
                        holder.tv_sum.setText("共 "+(mCon.getmSum(pos)+1)+" 个");
                    }
                    mCon.setmSum(pos,mCon.getmSum(pos)+1);
                    mCon.addmAmount();

                }
            });
		}
	}

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}

	public void addData(int pos)//弹出一窗口供填写表单
	{
		mDatas.add(pos, "One");
		mHeights.add( (int) (350 + Math.random() * 300));//3:100+Math.random()*300
        mCon.addmSum();
        mCon.inithotNum();
        mCon.initcoldNum();
        int[][] temp = new int[getItemCount()][10];
        System.arraycopy(mCon.getmdetail(),0,temp,0,mCon.getmdetail().length);//数组扩充
        mCon.setmdetail(temp);

        AVObject Menu = new AVObject("Menu");
        int price = (int) (Math.random()*10+5);
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
        for(int i=0;i<getItemCount();i++){
            temp[i]=i<pos?mCon.getmdetail()[i]:mCon.getmdetail()[i+1];
        }
        mCon.setmdetail(temp);
        AVObject del = mResult.get(pos);
        del.deleteInBackground();
        mResult.remove(pos);
		notifyItemRemoved(pos);
	}

    public ArrayList<String> getResult(){
        ArrayList<String> result = new ArrayList();
        for(int i=0;i<getItemCount();i++){
            if(mCon.getmSum(i)!=0){//区分是否有预定
                 if(mResult.get(i).getBoolean("type")){//drink
                     String hot;
                     String cold;
                     hot=mCon.gethotNum(i)!=0?"热："+mCon.gethotNum(i):"";
                     cold=mCon.getcoldNum(i)!=0?"冷："+mCon.getcoldNum(i):"";
                     result.add(mDatas.get(i)+hot+cold);
                 }else{
                     result.add(mDatas.get(i)+"总数："+mCon.getmSum(i));
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
    class Consumption {//订单类一个订单对应一个实体商品
        private List<Integer> mSum;//将每一组的个数记录下来
        private int[][] mdetail;//用来存储每项商品的预定详情
        private int mAmount=0;//pos是标明商品种类 mAmount是当前已定的商品总数
        private List<Integer> hotNum;
        private List<Integer> coldNum;
        //也为Consumption类中数组的pos
        public Consumption(){
            mSum = new ArrayList<Integer>();
            mdetail = new int[getItemCount()][10];
            hotNum = new ArrayList<Integer>();
            coldNum = new ArrayList<Integer>();
        }

        public void addmSum(){
            mSum.add(0);
        }

        public void removemSum(int pos){
            mSum.remove(pos);
        }

        public Integer getmSum(int pos) {
            return mSum.get(pos);
        }

        public void setmSum(int pos,int i) {
            this.mSum.set(pos,i);
        }

        public int[][] getmdetail() {
            return mdetail;
        }

        public void setmdetail(int[][] mdetail) {
            this.mdetail = mdetail;
        }

        public int getmAmount(){
            return mAmount;
        }

        public void cutmAmount() {
            mAmount--;
        }

        public void addmAmount() {
            mAmount++;
        }

        public void inithotNum(){
            hotNum.add(0);
        }
        
        public int gethotNum(int pos){
            return hotNum.get(pos);
        }

        public void removehotNum(int pos){
            hotNum.remove(pos);
        }

        public void addhotNum(int pos){
            hotNum.set(pos,hotNum.get(pos)+1);
        }

        public void initcoldNum(){
            coldNum.add(0);
        }

        public int getcoldNum(int pos){
            return coldNum.get(pos);
        }

        public void removecoldNum(int pos){
            coldNum.remove(pos);
        }

        public void addcoldNum(int pos){
            coldNum.set(pos,coldNum.get(pos)+1);
        }
    }

}