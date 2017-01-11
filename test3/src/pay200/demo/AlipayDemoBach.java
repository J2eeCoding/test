package pay200.demo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class AlipayDemoBach {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmsssssss");
	private static final String KEY="85c2uJ2WCff4ikMo4pI4u39W2Q7yKFK1";
	private static final String URL="http://one.pay200.cn/trx/alipay/pay";
	public static void main(String[] args) {
		String channelCode = "MSA10021008,MSA10021018,MSA10021025,MSA10021029,MSA10021024,MSA10021026,MSA10021027,MSA10021028,MSA10021030,MSA10021043,MSA10021044,MSA10021045,MSA10021046,MSA10021047,MSA10021048,MSA10021049,MSA10021050,MSA10021051,MSA10021052,MSA10021053,MSA10021054,MSA10021055,MSA10021056,MSA10021057,MSA10021058,MSA10021059,MSA10021060,MSA10021061,MSA10021062,MSA10021063,MSA10021064,MSA10021065,MSA10021066,MSA10021067,MSA10021068,MSA10021069,MSA10021071,MSA10021072,MSA10021073,MSA10021078,MSA10021084,MSA10021085,MSA10021088,MSA10021089,MSA10021090,MSA10021091,MSA10021092,MSA10021093,MSA10021095,MSA10021096,MSA10021097,MSA10021098,MSA10021099,MSA10021100,MSA10021101,MSA10021102,MSA10021103,MSA10021104,MSA10021105,MSA10021106,MSA10021107,MSA10021108,MSA10021109,MSA10021149,MSA10021148,MSA10021147,MSA10021146,MSA10021145,MSA10021144,MSA10021143,MSA10021142,MSA10021141,MSA10021140,MSA10021139,MSA10021138,MSA10021137,MSA10021136,MSA10021135,MSA10021134,MSA10021133,MSA10021132,MSA10021131,MSA10021130,MSA10021129,MSA10021128,MSA10021127,MSA10021126,MSA10021125,MSA10021124,MSA10021123,MSA10021122,MSA10021121,MSA10021120,MSA10021119,MSA10021118,MSA10021117,MSA10021116,MSA10021115,MSA10021114,MSA10021113,MSA10021112,MSA10021111,MSA10021110";
		String[] channels = channelCode.split(",");
		for(int i=0;i<channels.length;i++){
			pay(channels[i]);
		}
	}
	
	public static String pay(String channelCode){
		//切换通道
		String url = "http://114.55.239.114:12937/timeweb/manualTosQsX1sSo9owu/change.html?p=AL&m=B100000171&c="+channelCode;
		String channelCodeRes = send(url,new HashMap<String,String>(),"GBK");
		String ordernuber = "WX"+System.currentTimeMillis();
		//JSON串请求报文，可使用JSON工具包组
		String jsonStr ="{\"orderProduct\":\"支付宝\","
				+ "\"orderAmount\":\"0.01\","
				+ "\"orderId\":\""+ordernuber+"\","
				+ "\"merNo\":\"B100000171\","
				+ "\"urlServ\":\"http://localhost/alipay/callback\"}";
		HashMap<String,String>  req = new HashMap<String, String>();
		String sign = md5(jsonStr+KEY);
		req.put("data", jsonStr);
		req.put("sign", sign);
		String str = send(URL,req,"utf-8");
		JSONObject json = (JSONObject)JSONObject.parse(str);
		JSONObject retData = (JSONObject)JSONObject.parse(json.getString("retData"));
		String qrcode = retData.getString("qrcodeUrl");
		System.out.println(channelCode+"-"+channelCodeRes+"-"+qrcode);
		return qrcode;
	}
	
	
	
	public static String send(String url, Map<String, String> map,String encode) {
		try {
			StringBuffer params = new StringBuffer();
			for (Map.Entry<String, String> p : map.entrySet()) {
				params.append(p.getKey());
				params.append("=");
				params.append(p.getValue());
				params.append("&");
			}
			if (params.length() > 0) {
				params.deleteCharAt(params.length() - 1);
			}
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000 * 5);
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",String.valueOf(params.length()));

			OutputStream outStream = conn.getOutputStream();
			outStream.write(params.toString().getBytes(encode));
			outStream.flush();
			outStream.close();
			return new String(readInputStream(conn.getInputStream()), encode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}
	
	public static String md5(String str) {
		try {
			byte[] data = str.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			return toHex(md.digest(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static String toHex(byte input[]){
        if(input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for(int i = 0; i < input.length; i++){
            int current = input[i] & 0xff;
            if(current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }
        return output.toString();
    }
}
