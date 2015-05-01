package com.example.administrator.geekcoffee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/1 0001.
 */
public class Consumption {//订单类一个订单对应一个实体商品
    private List<Integer> mSum;//将每一组的个数记录下来
    //private List<Integer> mPrice;
    private int[][] mDetail;//用来存储每项商品的预定详情
    private int mAmount=0;//pos是标明商品种类 mAmount是当前已定的商品总数
    private List<Integer> hotNum;
    private List<Integer> coldNum;
    //也为Consumption类中数组的pos
    public Consumption(int count){
        mSum = new ArrayList<Integer>();
        mDetail = new int[count][10];
        hotNum = new ArrayList<Integer>();
        coldNum = new ArrayList<Integer>();
    }

    public void initmSum(){
        mSum.add(0);
    }

    public void removemSum(int pos){
        mSum.remove(pos);
    }

    public Integer getmSum(int pos) {
        return mSum.get(pos);
    }

    public void addmSum(int pos) {
        mSum.set(pos,mSum.get(pos)+1);
    }

    public void cutmSum(int pos) {
        mSum.set(pos,mSum.get(pos)-1);
    }

    public int[][] getmDetail() {
        return mDetail;
    }

    public int getmDetailChild(int i, int j){
        return mDetail[i][j];
    }

    public void setmDetail(int[][] mDetail) {
        this.mDetail = mDetail;
    }

    public void setmDetailChild(int i, int j, int result){
        mDetail[i][j]=result;
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

    public void cuthotNum(int pos){
        hotNum.set(pos,hotNum.get(pos)-1);
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

    public void cutcoldNum(int pos){
        coldNum.set(pos,coldNum.get(pos)-1);
    }
}