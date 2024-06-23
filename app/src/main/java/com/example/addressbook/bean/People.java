package com.example.addressbook.bean;


import com.example.addressbook.utils.PinYinStringHelper;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.*;

public class People extends LitePalSupport implements Serializable{
	@Column(unique = true)
	private long id;
	private String name;
	private String g;//分组
	private String telephone;
	private String word;
	private String information;//备注
	private int picturePath;//图片

	private String pinyin;
	private String jianpin;

	public String getPinyin() {
		return pinyin;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getJianpin() {
		return jianpin;
	}

	public void setJianpin(String jianpin) {
		this.jianpin = jianpin;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public People(){
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public People(long id, String name, String telephone, String information, int picturePath){
		this.id = id;
		this.name = name;
		this.telephone = telephone;
		this.information = information;
		this.picturePath = picturePath;

		this.pinyin= PinYinStringHelper.getPingYin(name);//全拼
		this.word = PinYinStringHelper.getAlpha(name);//大写首字母或特殊字符
		this.jianpin=PinYinStringHelper.getPinYinHeadChar(name);//简拼
	}
	public void setName(String name){
		this.name = name;
	}
	public void setTelephone(String telephone){
		this.telephone = telephone;
	}
	public void setInformation(String information){
		this.information = information;
	}
	public void setPicturePath(int picturePath){
		this.picturePath = picturePath;
	}

	public String getName(){
		return this.name;
	}
	public String getTelephone(){
		return this.telephone;
	}
	public String getInformation(){
		return this.information;
	}
	public int getPicturePath(){
		return this.picturePath;
	}

}