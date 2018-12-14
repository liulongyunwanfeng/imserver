package com.eplat.utils;

import java.io.*;
import java.util.*;


/**
 * @description 汉字转拼音,支持多音字(需自行扩展多音字词组),暂不支持'\uF900'-'\uFA2D'之间的生僻字 ,及main方法中输出的生僻字
 * @author Luckywb
 * @version 1.0 2013-6-19
 */
public class HanziDecoder {
//	private static final Logger log=Logger.getLogger(HanziDecoder.class);
	protected static final String ENCODE="UTF-8";
	protected static final char GAP='#';
	protected static final char PIN_YIN_GAP=',';
	protected static final char CI_ZU_GAP=' ';
	protected static Map<String,char[]> PYTOCZ;//拼音映射词组
	protected static Map<Character,List<String>> HZTOPY;//汉字映射拼音
	private static String hztopy="/hanzi_to_pinyin.txt";
	private static String pytocz="/pinyin_to_cizu.txt";
	private static final String HANZI_REGEX="\u4E00-\u9FA5\u3007";
	static{
		loadPYTOCZ();
		loadHZTOPY();
	}
	
	protected static void debug(String message){
//		log.debug(message);
		System.out.println(message);
	}
	
	protected static void error(Exception e){
//		log.error(e);
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		PrintStream ps=new PrintStream(bos);
		e.printStackTrace(ps);
		System.err.println(bos.toString());
		try {
			bos.close();
		} catch (IOException e1) {
			
		}
		ps.close();
	}
	
	
	public static boolean isHanzi(char c){
		return c>='\u4E00'&&c<='\u9FA5'||c=='\u3007';
	}
	public static boolean isHanzi(String text){
		if(text==null)return false;
		return text.matches("["+HANZI_REGEX+"]");
	}
	public static String getHanziRegex(){
		return HANZI_REGEX;
	}
	
	protected static List<String> getList(String... pinyins){
		if(pinyins==null)return null;
		List<String> list=new LinkedList<String>();
		for(String py:pinyins){
			list.add(py.toLowerCase());
		}
		return list;
	}
	private static void loadHZTOPY(){
		InputStream is=null;
		try{
			is=HanziDecoder.class.getResourceAsStream(hztopy);
			String[] rs=reloadHZTOPY(is);
//			debug("导入信息如下:\r\n"+rs[1]);
			if(!rs[0].contains("成功")){
				throw new RuntimeException(rs[0]);
			}
		}finally{
			if(is!=null){
				try{
					is.close();
				}catch(IOException e){
					error(e);
				}
			}
		}
	}
	
	protected static String[] reloadHZTOPY(InputStream is){
		BufferedReader br=null;
		String[] rs=new String[2];
		StringBuilder sb=new StringBuilder();
		try{
			br=new BufferedReader(new InputStreamReader(is,ENCODE));
			Map<Character,List<String>> tmp=new TreeMap<Character, List<String>>();
			String line;
			while((line=br.readLine())!=null){
				int index=line.indexOf(GAP);
				if(index!=1){
					sb.append("第二个字符不为'").append(GAP).append("',忽略行->").append(line).append("\r\n");
				}else{
					char c=line.charAt(0);
					if(isHanzi(c)){
						String pinyin=line.substring(index+1).toLowerCase();
						String pyGap=String.valueOf(PIN_YIN_GAP);
						if(pinyin.matches("[a-z]+(?:"+pyGap+"[a-z]+)*")){
							String[] pinyins=pinyin.split(pyGap);
							tmp.put(c,getList(pinyins));
						}else{
							sb.append("拼音格式有误,忽略行->").append(line).append("\r\n");
						}
					}else{
						sb.append("["+c+"]不是一个汉字,忽略行->").append(line).append("\r\n");
					}
				}
			}
			if(tmp.isEmpty()){
				rs[0]="文件中不包含有效的词库信息\r\n请确保导入的词库格式如[重#chong,zhong](不包括中括号)";
			}else{
				if(HZTOPY!=null)HZTOPY.clear();
				HZTOPY=tmp;
				rs[0]="成功导入"+tmp.size()+"条词库信息";
			}
			rs[1]=sb.toString();
		} catch (IOException e) {
			error(e);
			rs[0]="加载汉字词库失败";
			rs[1]=sb.toString();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					error(e);
				}
			}
		}
		return rs;
	}
	private static void loadPYTOCZ(){
		InputStream is=null;
		try{
			is=HanziDecoder.class.getResourceAsStream(pytocz);
			String[] rs=reloadPYTOCZ(is);
//			debug("导入信息如下:\r\n"+rs[1]);
			if(!rs[0].contains("成功")){
				throw new RuntimeException(rs[0]);
			}
		}finally{
			if(is!=null){
				try{
					is.close();
				}catch(IOException e){
					error(e);
				}
			}
		}
	}
	protected static String[] reloadPYTOCZ(InputStream is){
		BufferedReader br=null;
		String[] rs=new String[2];
		StringBuilder sb=new StringBuilder();
		try{
			br=new BufferedReader(new InputStreamReader(is,ENCODE));
			Map<String,char[]> tmp=new TreeMap<String,char[]>(); 
			String line;
			while((line=br.readLine())!=null){
				int index=line.indexOf(GAP);
				if(index<0){
					sb.append("行字串不存在'").append(GAP).append("',忽略行->").append(line).append("\r\n");
				}else{
					String ciyu=line.substring(index+1);
					ciyu=CI_ZU_GAP+ciyu.trim()+CI_ZU_GAP;//保证首尾都有一个空格
					String py=line.substring(0,index);
					if(py.matches("[a-zA-Z]+")){
						tmp.put(py.toLowerCase(),ciyu.toCharArray());
					}else{
						sb.append("["+py+"]不是有效拼音,忽略行->").append(line).append("\r\n");
					}
				}
			}
			if(tmp.isEmpty()){
				rs[0]="文件中不包含有效的词组信息\r\n请确保导入的词组格式如[zhong#重量 重力 重心](不包括中括号)";
			}else{
				if(PYTOCZ!=null)PYTOCZ.clear();
				PYTOCZ=tmp;
				rs[0]="成功导入"+tmp.size()+"行词组信息";
			}
			rs[1]=sb.toString();
		} catch (IOException e) {
			error(e);
			rs[0]="加载多音字词库失败";
			rs[1]=sb.toString();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					error(e);
				}
			}
		}
		return rs;
	}
	
	/*
	 * @description 大小写转换
	 * @param chrs 小写英文字符数组
	 * @param type 1首字母大写,2大写,其它小写
	 */
	private static void change(char[] chrs,int type){
		switch(type){
		case 1:chrs[0]-=32;break;//首字母小写转大写
		case 2:
			int i,len=chrs.length;
			for(i=0;i<len;chrs[i]-=32,++i);
			break;
		default:return;
		}
	}
	
	
	
	/**
	 * @description 将字符串中的汉字转换成小写拼音
	 * @param text 带有汉字的字符串
	 * @return 转换后的字符串
	 */
	public static String getHypy(String text){
		return getHypy(text, 0);
	}
	
	/**
	 * @description 将字符串中的汉字转换成小写拼音
	 * @param text 带有汉字的字符串
	 * @param chr 汉字转成拼音的前后间隔符
	 * @return 转换后的字符串
	 */
	public static String getHypy(String text,char chr){
		return getHypy(text, 0,chr);
	}
	
	private static boolean isLetter(char lastChar){
		return lastChar>='A'&&lastChar<='Z'||lastChar>='a'&&lastChar<='z';
	}
	/**
	 * @description 将字符串中的汉字转换成拼音
	 * @param text 带有汉字的字符串
	 * @param type 0小写,1首字母大写,2大写
	 * @return 转换后的字符串
	 */
	public static String getHypy(String text,int type,char chr){
		if(text==null)return text;
		char[] chrs=text.toCharArray();
		StringBuilder sb=new StringBuilder();
		int i,len=chrs.length;//汉字起始位置
		char lastChar=' ';
		for(i=0;i<len;++i){
			char c=chrs[i];
			if(isHanzi(c)){
				String pinyin;
				List<String> pinyins=HZTOPY.get(c);
				if(pinyins==null){
					continue;
				}else if(pinyins.size()==1){
					pinyin=pinyins.get(0);
				}else{
//					long cur=System.currentTimeMillis();
					pinyin=getPinyin(pinyins, chrs, c, i);
//					log.debug(c+"->"+pinyin+"耗时"+(System.currentTimeMillis()-cur)+"ms");
				}
				char[] crs=pinyin.toCharArray();
//				crs[0]-=32;//首字母小写转大写
				change(crs,type);
				if(isLetter(lastChar)){
					sb.append(chr);
				}
				sb.append(crs);
				if(i+1<len&&isLetter(chrs[i+1])){
					sb.append(chr);
				}else{
					lastChar=crs[crs.length-1];
				}
			}else{
				sb.append(c);
				lastChar=c;
			}
		}
		return sb.toString();
	}
	
	/**
	 * @description 将字符串中的汉字转换成拼音
	 * @param text 带有汉字的字符串
	 * @param type 0小写,1首字母大写,2大写
	 * @return 转换后的字符串
	 */
	public static String getHypy(String text,int type){
		if(text==null)return text;
		char[] chrs=text.toCharArray();
		StringBuilder sb=new StringBuilder();
		int i,len=chrs.length;//汉字起始位置
		for(i=0;i<len;++i){
			char c=chrs[i];
			if(isHanzi(c)){
				String pinyin;
				List<String> pinyins=HZTOPY.get(c);
				if(pinyins==null){
					continue;
				}else if(pinyins.size()==1){
					pinyin=pinyins.get(0);
				}else{
//					long cur=System.currentTimeMillis();
					pinyin=getPinyin(pinyins, chrs, c, i);
//					debug(c+"->"+pinyin+"耗时"+(System.currentTimeMillis()-cur)+"ms");
				}
				char[] crs=pinyin.toCharArray();
//				crs[0]-=32;//首字母小写转大写
				change(crs,type);
				sb.append(crs);
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	/**
	 * 获取拼音的首字母，只取第一个字的首字母
	 * @param text
	 * @return
	 */
	public static String getFirst(String text){
		String rtn=getFirstAll(text);
		if (StringUtils.hasLength(rtn)){
			return rtn.substring(0,1);
		} else {
			return "";
		}
	}
	/**
	 * 获取所有字的首字母
	 * @param text
	 * @return
	 */
	public static String getFirstAll(String text){
		if(text==null)return text;
		char[] chrs=text.toCharArray();
		StringBuilder sb=new StringBuilder();
		int i,len=chrs.length;//汉字起始位置
		for(i=0;i<len;++i){
			char c=chrs[i];
			if(isHanzi(c)){
				String pinyin;
				List<String> pinyins=HZTOPY.get(c);
				if(pinyins==null){
					continue;
				}else if(pinyins.size()==1){
					pinyin=pinyins.get(0);
				}else{
					pinyin=getPinyin(pinyins, chrs, c, i);
				}
			
				sb.append(pinyin.toUpperCase().substring(0,1));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	//连续两个字相同,表示词组匹配上
	//0表示匹配,1表示默认pinyin,-1不匹配
	private static int isMatches(char[] phrase,char[] chrs,char c,int index){
		int plen=phrase.length-1;
		if(plen<1)return -1;
		char b,t;
		//比对头部
		int i,clen=chrs.length,result=-1;
		for(i=1;i<plen;++i){
			if(c==phrase[i]){
				//判断前一个字是否匹配
				if((b=phrase[i-1])!=CI_ZU_GAP&&index>0&&b==chrs[index-1]){
					return 0;
				}else if((t=phrase[i+1])!=CI_ZU_GAP&&index+1<clen&&t==chrs[index+1]){
					return 0;
				}
			}
		}
		return result;
	}
	/**
	 * @description 多音字根据上下文选择对应拼音
	 * @param chrs 需要转换的字符串
	 * @param c 当前字符
	 * @param index 当前字符索引位置
	 * @return
	 */
	protected static String getPinyin(List<String> pinyins,char[] chrs,char c,int index){
		Iterator<String> itr=pinyins.iterator();
		String dpinyin=itr.next();
		String pinyin=dpinyin;
		while(itr.hasNext()){
			pinyin=itr.next();
			char[] phrase=PYTOCZ.get(pinyin);
			if(phrase==null){
				debug(pinyin+"未定义多音字词组");
			}else{
				int r=isMatches(phrase, chrs, c, index);
				if(r==0)return pinyin;
			}
		}
		return dpinyin;
	}
	
}
