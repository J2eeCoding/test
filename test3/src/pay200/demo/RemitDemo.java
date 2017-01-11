package pay200.demo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class RemitDemo {
	private static final String KEY="xQ9LpX6cd0f3XP3964L7sWEN45Lxl361";
	private static final String DES="kaD1SlJ1C487c5BuP0ap8jdK";

	private static final String URL="http://top.pay200.cn/trx/rm/remit";
	public static void main(String[] args) {
		//JSON串请求报文，可使用JSON工具包组
		String jsonStr ="{\"orderAmount\":\"390\","
				+ "\"bankAccountNo\":\""+encode(DES,"6214830123467168")+"\","
				+ "\"orderId\":\"LLR20161230004\","
				+ "\"merNo\":\"B100000171\","
				+ "\"bankAccountName\":\""+encode(DES,"刘军飞")+"\"}";
		System.out.println("请求明文:"+jsonStr);
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
	public static String encode(String key,String data){
		try{
			return toHex(des3Encryption(key.getBytes(), data.getBytes()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	private static byte[] des3Encryption(byte[] key, byte[] data) throws
	    NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
	    BadPaddingException, IllegalBlockSizeException, IllegalStateException {
	  final String Algorithm = "DESede";
	
	  SecretKey deskey = new SecretKeySpec(key, Algorithm);
	
	  Cipher c1 = Cipher.getInstance(Algorithm);
	  c1.init(Cipher.ENCRYPT_MODE, deskey);
	  return c1.doFinal(data);
	}
}
