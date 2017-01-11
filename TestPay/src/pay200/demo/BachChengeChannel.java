package pay200.demo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BachChengeChannel {
	public static void main(String[] args) {
		String info = "B100000155,YSA10021506;B100000156,YSA10021511;B100000157,YSA10021510;B100000158,YSA10021516;B100000161,YSA10021515;B100000165,YSA10021134;B100000166,YSA10021003;B100000060,YSC10021443;B100000169,YSA10028002;B100000167,YSA10021088;B100000054,YSC10021436;B100000174,YSA10021519;B100000175,YSA10021519;B100000176,MSA10021125;B100000177,YSA10021527;B100000181,YSA10021210;B100000191,YSC10021468;B100000197,YSA10021348;B100000198,YSA10021341";
		String[] infos = info.split(";");
		for(int i=0;i<infos.length;i++){
			String merChennel = infos[i];
			String[] channel = merChennel.split(",");
			String url = "http://114.55.239.114:12937/timeweb/manualTosQsX1sSo9owu/change.html?p=AL&m="+channel[0]+"&c="+channel[1];
			String reg = send(url,"GBK");
			System.out.println(channel[0]+"-"+channel[1]+"-"+reg); 
		}
		
	}
	
	public static String send(String url,String encode) {
		try {
			StringBuffer params = new StringBuffer();
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
}
