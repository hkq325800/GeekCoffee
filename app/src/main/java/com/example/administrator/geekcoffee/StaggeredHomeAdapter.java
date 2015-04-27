package com.example.administrator.geekcoffee;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        void onColdClick(View view, int position);

        void onHotClick(View view, int position);

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
        //mdetail=new int[getItemCount()][10];
		for (int i = 0; i < mDatas.size(); i++)
		{
            mSum.add(0);
			mHeights.add( (int) (100 + Math.random() * 300));
            /*for(int j=0;j<10;j++){
                mdetail[i][j]=0;
            }*/
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
		LayoutParams lp = holder.tv_num.getLayoutParams();
		lp.height = mHeights.get(position);
		
		holder.tv_num.setLayoutParams(lp);
		holder.tv_num.setText(mDatas.get(position));
        holder.num_cut.setVisibility(View.INVISIBLE);
        holder.cold.setVisibility(View.INVISIBLE);
        holder.hot.setVisibility(View.INVISIBLE);

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

            holder.num_cut.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onNumCutClick(holder.itemView, pos);
                    if(mSum.get(pos)==1){
                        holder.cold.setVisibility(View.INVISIBLE);
                        holder.hot.setVisibility(View.INVISIBLE);
                        holder.num_cut.setVisibility(View.INVISIBLE);
                    }else{
                        /*switch (mdetail[pos][mSum.get(pos)-2]){//恢复上一次选择的选项
                            case 1:
                                holder.cold.setBackgroundColor(now.getResources().getColor(R.color.color_item_normal));
                                break;
                            case 2:
                                holder.hot.setBackgroundColor(now.getResources().getColor(R.color.color_item_normal));
                                break;
                            case 0:
                                break;
                            default:
                                break;
                        }*/
                    }
                    //mdetail[pos][mSum.get(pos)-1]=0;
                    mAmount--;
                    mSum.set(pos,mSum.get(pos)-1);//减少定的个数
                    /*if(mSum.get(pos)==1){
                        holder.cold.setVisibility(View.INVISIBLE);
                        holder.hot.setVisibility(View.INVISIBLE);
                        holder.num_cut.setVisibility(View.INVISIBLE);
                    }else{
                        switch (mConsumption.getIsHot(mSum.get(pos)-1)){
                            case 0:
                                holder.cold.setBackgroundColor(now.getResources().getColor(R.color.color_item_normal));
                                break;
                            case 1:
                                holder.hot.setBackgroundColor(now.getResources().getColor(R.color.color_item_normal));
                                break;
                            default:
                                break;
                        }

                    }
                    mConsumption.remove(mSum.get(pos));
                    mAmount--;
                    mSum.set(pos,mSum.get(pos)-1);*/
                }
            });

            holder.num_add.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onNumAddClick(holder.itemView, pos);
                    if(mSum.get(pos)==0){
                        holder.cold.setVisibility(View.VISIBLE);
                        holder.hot.setVisibility(View.VISIBLE);
                        holder.num_cut.setVisibility(View.VISIBLE);
                        /*mSum.set(pos,mSum.get(pos)+1);//增加定的个数
                        mAmount++;*/
                    }else{
                        /*if(mdetail[pos][mSum.get(pos)-1]==0){
                            Toast.makeText(now,"",Toast.LENGTH_SHORT).show();//提示
                        }else{
                            holder.cold.setBackgroundColor(now.getResources().getColor(R.color.transparent));
                            holder.hot.setBackgroundColor(now.getResources().getColor(R.color.transparent));

                        }*/
                    }
                    mSum.set(pos,mSum.get(pos)+1);//增加定的个数
                    mAmount++;
                }
            });

            holder.cold.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onColdClick(holder.itemView, pos);
                    //mdetail[pos][mSum.get(pos)]=1;
                    holder.hot.setBackgroundColor(now.getResources().getColor(R.color.transparent));
                    holder.cold.setBackgroundColor(now.getResources().getColor(R.color.color_item_normal));
                    /*if(mdetail[pos][mSum.get(pos)]==0){//未设置冷热

                    }*/
                }
            });

            holder.hot.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onHotClick(holder.itemView, pos);
                    //mdetail[pos][mSum.get(pos)]=2;
                    holder.cold.setBackgroundColor(now.getResources().getColor(R.color.transparent));
                    holder.hot.setBackgroundColor(now.getResources().getColor(R.color.color_item_normal));
                    /*if(mdetail[pos][mSum.get(pos)]==0){//未设置冷热

                    }*/
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
		mHeights.add( (int) (100 + Math.random() * 300));
		notifyItemInserted(position);
	}

	public void removeData(int position)
	{
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

    public void confirmOne(int pos){
        mAmount++;
        mSum.set(pos,mSum.get(pos)+1);
        Toast.makeText(now,"已定下该项"+mSum.get(pos)+"个",Toast.LENGTH_SHORT).show();
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

		TextView tv_num;
        //TextView tv_sum;
        Button num_cut;
        Button num_add;
        Button cold;
        Button hot;

		public MyViewHolder(View view)
		{
			super(view);
            tv_num = (TextView) view.findViewById(R.id.id_num);
            //tv_sum = (TextView) view.findViewById(R.id.id_sum);
            num_cut = (Button) view.findViewById(R.id.num_cut);
            num_add = (Button) view.findViewById(R.id.num_add);
            cold = (Button) view.findViewById(R.id.cold);
            hot = (Button) view.findViewById(R.id.hot);
		}
	}
    class Consumption {//订单类一个订单对应一个实体商品
        private int[] id;//对应外部pos
        private int[] sum;
        /*private int[] coldNum;
        private int[] hotNum;*/
        //pos,mDatas.get(pos),2
        public void setAll(int id, int sum){
            if(mAmount==0){
                this.id=new int[30];
                this.sum=new int[30];
                /*this.coldNum=new int[30];
                this.hotNum=new int[30];*/
            }
            /*if(isHot){
                this.hotNum[]++;
            }else{
                this.coldNum[]++;
            }*/
            this.id[mAmount] = id;
            this.sum[mAmount] = sum;
            /*this.coldNum[mAmount] = coldNum;
            this.hotNum[mAmount] = hotNum;*/
        }

        public int getId(int position) {
            return id[position];
        }

        public void setId(int position, int id) {
            this.id[position] = id;
        }

       /* public int[] getColdNum() {
            return coldNum;
        }

        public void setColdNum(int[] coldNum) {
            this.coldNum = coldNum;
        }

        public int[] getHotNum() {
            return hotNum;
        }

        public void setHotNum(int[] hotNum) {
            this.hotNum = hotNum;
        }

        public void remove(){
            id[mAmount]=-1;
            coldNum[mAmount]--;
            hotNum[mAmount]--;
        }

        public void removeAll(){
            for(int i=0;i<id.length;i++){
                id[i]=-1;
                coldNum[i]--;
                hotNum[i]--;
            }
        }
        //返回某一类型的商品的订单数
        public int sum(int pos){
            return coldNum[pos]+hotNum[pos];
        }*/

        public int[] getSum() {
            return sum;
        }

        public void setSum(int[] sum) {
            this.sum = sum;
        }
    }

}