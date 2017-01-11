package pay200.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class BuDan extends Thread{
	private String host = "http://114.55.236.3:18002/keenyboss/orderquery/syncAlipayOrder";
	private String tempString;
	
	public BuDan(String tempString){
		super();
		this.tempString = tempString;
	}
	
	@Override
	public void run() {
		String[] strarray = tempString.split(",");
		String merNo = strarray[0];
    	String orderId = strarray[1];
    	String orderDate =strarray[2];
    	String param = "merNo="+merNo+"&orderId="+orderId+"&orderDate="+orderDate;
		try {
			sendPostHttps(host, param, "utf-8");
  		} catch (Exception e) {
  			System.out.println(param+"Exception:"+e.getMessage());   
		}
	}
	
	public static HashMap<String, String> sendPostHttps(String url, String params, String encode) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		HashMap<String, String> retMap = new HashMap<String, String>();
		SSLContext ctx = SSLContext.getInstance("SSL");
		ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
		SSLContext.setDefault(ctx);
		URL urlObj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(1000 * 20);
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("Accept-Charset", "utf-8");
		conn.setRequestProperty("contentType", "utf-8");
		conn.setRequestProperty("Content-Length",String.valueOf(params.length()));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(params.toString().getBytes(encode));
		outStream.flush();
		if (conn.getResponseCode() != 200) {
			System.out.println(params+"访问失败");
			return retMap;
		}
		// 读取数据
		InputStream dataIn = conn.getInputStream();
		ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = dataIn.read(buffer)) != -1) {
			dataOut.write(buffer, 0, len);
		}
		byte[] data = dataOut.toByteArray();
		try {
			dataIn.close();
			dataOut.close();
			outStream.close();
		} catch (Exception e) {
		}
		String res = new String(data, encode);
		System.out.println(params+"==="+res);
		return retMap;
	}
	
	 private static class DefaultTrustManager implements X509TrustManager {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		}
	
}
