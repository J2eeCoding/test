package pay200.demo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class WeiXinDemo {
	private static final String KEY="83GEaSU2g3TzRW3M4A324wpmqY167221";
	private static final String URL="http://top.pay200.cn/trx/wx/pay";
	public static void main(String[] args) {
		//JSON串请求报文，可使用JSON工具包组
		String jsonStr ="{\"orderProduct\":\"游戏充值\","
				+ "\"orderAmount\":\"1.00\","
				+ "\"orderId\":\"LW2017010902\","
				+ "\"merNo\":\"B100000171\","
				+ "\"urlServ\":\"http://localhost/alipay/callback\"}";
		System.out.println("请求明文:"+jsonStr);
		HashMap<String,String>  req = new HashMap<String, String>();
		String sign = md5(jsonStr+KEY);
		req.put("data", jsonStr);
		req.put("sign", sign);
		System.out.println("请求报文:"+req);
		String str = send(URL,req);
		System.out.println("响应报文:"+str);

	}
	
	public static void mockServerNotify(String url){
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("retCode", "S");
		param.put("retMesg", "交易成功");
		JSONObject json = new JSONObject();
		json.put("merNo", "B10001");
		json.put("orderId", "12324560001");
		json.put("orderAmount", "1");
		json.put("orderDate", "20160930 12:12:12");
		json.put("payDate", "20160930 12:12:20");
		json.put("orderStatus", "1");
		String data = json.toJSONString();
		param.put("retData", data);
		param.put("retSign", md5(data+KEY));
		String result = send(url, param);
		if("success".equalsIgnoreCase(result)){
			System.out.println("通知商户成功");
		}
	}
	
	public static void query(){
		String jsonStr ="{\"orderProduct\":\"支付宝\","
		+ "\"orderAmount\":\"1\","
		+ "\"orderId\":\"20160920W0004\","
		+ "\"merNo\":\"1234567\","
		+ "\"orderDate\":\"yyyy-MM-dd HH:mm:ss\","
		+ "\"urlServ\":\"http://localhost/alipay/callback\"}";
		System.out.println("请求明文:"+jsonStr);
		String url = "http://one.pay200.cn/trx/alipay/query";
		HashMap<String,String>  req = new HashMap<String, String>();
		String sign = md5(jsonStr+KEY);
		req.put("data", jsonStr);
		req.put("sign", sign);
		System.out.println("请求报文:"+req);
		String str = send(URL,req);
		System.out.println("响应报文:"+str);
	}
	
	public static String send(String url, Map<String, String> map) {
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
			HttpURLConnection conn = (HttpURLConnection) urlObj
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000 * 5);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					String.valueOf(params.length()));

			OutputStream outStream = conn.getOutputStream();
			outStream.write(params.toString().getBytes("utf-8"));
			outStream.flush();
			outStream.close();
			return new String(readInputStream(conn.getInputStream()), "utf-8");
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
