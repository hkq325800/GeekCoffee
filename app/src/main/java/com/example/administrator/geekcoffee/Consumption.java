package com.example.administrator.geekcoffee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/1 0001.
 */
public class Consumption {//订单类一个订单对应一个实体商品
    private List<Integer> mSum;//将每一组的个数记录下来
    private int mAmount = 0;//pos是标明商品种类 mAmount是当前已定的商品总数
    private List<Integer> hotNum;
    private List<Integer> coldNum;

    public Consumption(int count) {
        mSum = new ArrayList<Integer>();
        hotNum = new ArrayList<Integer>();
        coldNum = new ArrayList<Integer>();
    }

    public void initmSum() {
        mSum.add(0);
    }

    public void removemSum(int pos) {
        mSum.remove(pos);
    }

    public void removeAllmSum() {
        mSum.removeAll(mSum);
    }

    public Integer getmSum(int pos) {
        return mSum.get(pos);
    }

    public void setmSum(int pos, int mSum) {
        this.mSum.set(pos, mSum);
    }

    public int getmAmount() {
        return mAmount;
    }

    public void setmAmount() {
        mAmount = 0;
        for (int i = 0; i < mSum.size(); i++) {
            if (getmSum(i) != 0) {
                mAmount += getmSum(i);
            }
        }
    }

    public void inithotNum() {
        hotNum.add(0);
    }

    public int gethotNum(int pos) {
        return hotNum.get(pos);
    }

    public void removehotNum(int pos) {
        hotNum.remove(pos);
    }

    public void removeAllhotNum() {
        hotNum.removeAll(hotNum);
    }

    public void setHotNum(int pos, int value) {
        hotNum.set(pos, value);
    }

    public void initcoldNum() {
        coldNum.add(0);
    }

    public int getcoldNum(int pos) {
        return coldNum.get(pos);
    }

    public void removecoldNum(int pos) {
        coldNum.remove(pos);
    }

    public void removeAllcoldNum() {
        coldNum.removeAll(coldNum);
    }

    public void setColdNum(int pos, int value) {
        coldNum.set(pos, value);
    }

}