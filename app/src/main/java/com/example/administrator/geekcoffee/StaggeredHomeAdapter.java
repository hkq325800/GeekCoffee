package com.example.administrator.geekcoffee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class StaggeredHomeAdapter extends
		RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder>
{
    private Context now;//this
	private List<String> mDatas;//每一组商品的名称
    private List<Integer> mSum;//将每一组的个数记录下来
	private LayoutInflater mInflater;//layout
    private Consumption mConsumption;
	private List<Integer> mHeights;
    private int[][] mdetail;//用来存储每项商品的预定详情
    private int mAmount=0;//pos是标明商品种类 mAmount是当前已定的商品总数
    //也为Consumption类中数组的position

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);

        void onNumAddClick(View view, int position);

        void onNumCutClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener )
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public StaggeredHomeAdapter(Context context, List<String> datas)
	{
        now = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
        mConsumption=new Consumption();
        mSum = new ArrayList<Integer>();
		mHeights = new ArrayList<Integer>();
        mdetail=new int[getItemCount()][10];
		for (int i = 0; i < mDatas.size(); i++)
		{
            mSum.add(0);
			mHeights.add( (int) (350 + Math.random() * 300));
            for(int j=0;j<10;j++){
                mdetail[i][j]=0;
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
	public void onBindViewHolder(final MyViewHolder holder, final int position)
	{
		LayoutParams lp = holder.custom_cross.getLayoutParams();
		lp.height = mHeights.get(position);
		holder.tv_item.setLayoutParams(lp);
		holder.tv_item.setText(mDatas.get(position));
        if(mSum.get(position)==0){
            holder.tv_temp.setVisibility(View.INVISIBLE);
            holder.tv_sum.setVisibility(View.INVISIBLE);
            holder.btn_cut.setVisibility(View.INVISIBLE);
            holder.tv_sum.setText("");
            holder.tv_temp.setText("");
        }else{
            holder.tv_temp.setVisibility(View.VISIBLE);
            holder.tv_sum.setVisibility(View.VISIBLE);
            holder.btn_cut.setVisibility(View.VISIBLE);
            switch (mdetail[position][mSum.get(position)-1]){//恢复上一次选择的选项
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
            holder.tv_sum.setText("共 "+mSum.get(position)+" 个");
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

                    if(mSum.get(pos)==1){
                        holder.btn_cut.setVisibility(View.INVISIBLE);
                        holder.tv_sum.setVisibility(View.INVISIBLE);
                        holder.tv_temp.setVisibility(View.INVISIBLE);
                        holder.tv_temp.setText("");
                    }else{
                        switch (mdetail[pos][mSum.get(pos)-2]){//恢复上一次选择的选项
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
                    mdetail[pos][mSum.get(pos)-1]=0;
                    mAmount--;
                    mSum.set(pos,mSum.get(pos)-1);//减少定的个数
                    if(mSum.get(pos)==0){
                        holder.tv_sum.setText("");
                    }else{
                        holder.tv_sum.setText("共 "+mSum.get(pos)+" 个");
                    }
                }
            });

            holder.btn_add.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    final int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onNumAddClick(holder.itemView, pos);

                    if(mSum.get(pos)==0){
                        holder.btn_cut.setVisibility(View.VISIBLE);
                        holder.tv_sum.setVisibility(View.VISIBLE);
                        holder.tv_temp.setVisibility(View.VISIBLE);
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(now);
                    dialog.setTitle("选择冷热");
                    dialog.setCancelable(true);
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                        @Override
                        public void onCancel(DialogInterface dialog){
                            if(mSum.get(pos)==0){
                                holder.btn_cut.setVisibility(View.INVISIBLE);
                                holder.tv_sum.setVisibility(View.INVISIBLE);
                                holder.tv_temp.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    dialog.setPositiveButton("热", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mdetail[pos][mSum.get(pos)]=2;
                            mSum.set(pos,mSum.get(pos)+1);//增加定的个数
                            mAmount++;
                            holder.tv_temp.setText("热");
                            holder.tv_sum.setText("共 "+mSum.get(pos)+" 个");
                        }
                    });
                    dialog.setNegativeButton("冷", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mdetail[pos][mSum.get(pos)]=1;
                            mSum.set(pos,mSum.get(pos)+1);//增加定的个数
                            mAmount++;
                            holder.tv_temp.setText("冷");
                            holder.tv_sum.setText("共 "+mSum.get(pos)+" 个");
                        }
                    });
                    dialog.show();
                }
            });
		}
	}

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}

	public void addData(int position)
	{
		mDatas.add(position, "Insert One");
		mHeights.add( (int) (350 + Math.random() * 300));//3:100+Math.random()*300
        mSum.add(0);
        int[][] temp = new int[getItemCount()][10];
        System.arraycopy(mdetail,0,temp,0,mdetail.length);//数组扩充
        mdetail=temp;
		notifyItemInserted(position);
	}

	public void removeData(int position)
	{
		mDatas.remove(position);
        mHeights.remove(position);
        mSum.remove(position);
        for(int i=position;i<getItemCount();i++){
            mdetail[i]=mdetail[i+1];
        }
		notifyItemRemoved(position);
	}

    public ArrayList<String> getResult(){
        ArrayList<String> result = new ArrayList();
        for(int i=0;i<mDatas.size();i++){
            if(mSum.get(i)!=0){
                 result.add(mDatas.get(i)+"/"+mSum.get(i));
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
        private int[] id;//对应外部pos
        private int[] sum;

        public void setAll(int id, int sum){
            if(mAmount==0){
                this.id=new int[30];
                this.sum=new int[30];
            }

            this.id[mAmount] = id;
            this.sum[mAmount] = sum;
        }

        public int getId(int position) {
            return id[position];
        }

        public void setId(int position, int id) {
            this.id[position] = id;
        }

        public int[] getSum() {
            return sum;
        }

        public void setSum(int[] sum) {
            this.sum = sum;
        }
    }

}