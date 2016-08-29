package danxx.library.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mateng on 13-12-13.
 */
public class AppUtils {

	private static AtomicInteger counter = new AtomicInteger();
	
	private static Context context;

	public static String historyRecommendJsonUrl = "";
	public static String favoriteRecommendJsonUrl = "";
	public static String userCenterUrl = "";

	public static IPaymentCallback paymentCallback = null;

	public static void setContext(Context c) {
		context = c;
	}
	

	public static int getAtomicInt() {
		return counter.getAndAdd(1);
	}

	/**
	 * 检查网络连接。
	 * 
	 * @param context
	 * @return true 已经接；false 未连接；
	 */
	public static boolean checkNetwork(Context context) {

		boolean isConnect = false;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {

			NetworkInfo[] infoList = manager.getAllNetworkInfo();
			if (infoList != null) {
				int length = infoList.length;
				if (length > 0) {
					for (int i = 0; i < length; i++) {

						final NetworkInfo info = infoList[i];
						if (NetworkInfo.State.CONNECTED == info.getState()) {

							isConnect = true;
							break;
						}
					}
				}
			}
		}
		context = null;
		return isConnect;
	}
	
	public static boolean isEmpty(String str) {
		boolean result = false;
		
		if(null == str || "".equals(str.trim())) {
			result = true;
		}
		
		return result;
	}
	
	public static boolean isScreen1080P(Context mContext) {
		boolean result = false;
		
		if(DeviceUtils.getScreenSize(mContext).y == 1080 || DeviceUtils.getScreenSize(mContext).x == 1920) {
			result = true;
		}
		
		return result;
	}
	
	public static int getProRata(Context mContext, int digit) {
		if(DeviceUtils.getScreenSize(mContext).y == 1080 || DeviceUtils.getScreenSize(mContext).x == 1920) {
			digit *= 1.5;
		}
		
		return digit;
	}
	
	public static int getProRataW(Context context, int width) {
		return (int) DeviceUtils.getAdapterWidth(context, width);
	}
	
	public static int getProRataH(Context context, int height) {
		return (int) DeviceUtils.getAdapterHeight(context, height);
	}

	private static int ID_OFFSET = 0x100;

	public static int createId() {
		return ID_OFFSET++;
	}
	
	public static AtomicInteger uniqueId = new AtomicInteger(100000);
	
	public static int nextId() {
		return uniqueId.getAndIncrement();
	}
	

	private static int showUpdateDialogCount = -1;
	

	private static long showUpdateDialogTime = -1;
	

//	public static void showClientVersion(Context context) {
//		Toast.makeText(context, context.getString(R.string.current_client_version, getClientVersion(context)), Toast.LENGTH_SHORT).show();
//	}

	public static String getClientVersion(Context context) {
		String version = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (NameNotFoundException e) {
			// ignore
		}
		return version;
	}

	public static int getVersionCode(Context context)
	{
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	


	public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
 
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
 
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
 
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
 
        // Set the component to be explicit
        explicitIntent.setComponent(component);
 
        return explicitIntent;
    }
	
	

	
	public static void freeImageView(ImageView img) {
		Drawable drawable = img.getDrawable();
		if (drawable != null) {
			if (drawable instanceof BitmapDrawable) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				Bitmap bitmap = bitmapDrawable.getBitmap();
				if (bitmap != null && !bitmap.isRecycled())
					bitmap.recycle();
			}
		}
	}
	
	public static void freeView(View view) {
		if(view == null)
			return;
		if(view instanceof ImageView) {
			freeImageView((ImageView) view);
		}
		else if(view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup)view;
			for(int i = 0;i < viewGroup.getChildCount();i++) {
				freeView(viewGroup.getChildAt(i));
//				freeView(view);
			}
		}
	}
	
	public static boolean isNetworkOk(Context context) {
    	boolean res = false;
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectMgr.getActiveNetworkInfo();
		if(networkinfo != null && networkinfo.isAvailable()) {
			res = true;
		}
		
		return res;
    }

    public static void removeCahce(Context context,String folder) {
        File cacheDir = context.getCacheDir();
        if(cacheDir != null) {
            String path = cacheDir.getAbsolutePath() + File.separator + folder;
            File[] files = (new File(path)).listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

	public static boolean isNumeric(String str){

		if(TextUtils.isEmpty(str)) return false;

		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}


	public static String getUrlParameter(String url, String key) {
		String val = "";

		if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
			String[] arrSplit = null;
			url = url.trim();
			arrSplit = url.split("[?]");

			String strUrlParam = null;
			if(url.length() > 1) {
				if(arrSplit.length > 1) {
					if(arrSplit[1] != null) {
						strUrlParam = arrSplit[1];
					}
				}
			}

			if(!TextUtils.isEmpty(strUrlParam)) {
				arrSplit = strUrlParam.split("&");
				if(arrSplit != null) {
					for(String strSplit: arrSplit) {
						String[] arrSplitEqual=null;
						arrSplitEqual = strSplit.split("=");
						if(arrSplitEqual.length > 1 && key.equals(arrSplitEqual[0])) {
							val = arrSplitEqual[1];
							break;
						}
					}
				}
			}
		}

		return val;
	}



	public static String encodeUTF8(String str) {
		try {
			if (!TextUtils.isEmpty(str)) {
				return URLEncoder.encode(str, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static interface IPaymentCallback {
		public abstract void onPay(boolean paid, int code);
	}

	public static void copyPlayerJar(Context context) {
		Log.d("com.wasu.wasutvcs", "copyPlayerJar");
		String fileName = "com.mediatek.mmp.jar";

		String dstFilePath = context.getFilesDir() + "/" + fileName;

		File file = new File(dstFilePath);
		if(file.exists()) {
			file.delete();
		}

		try {
			InputStream myInput;
			OutputStream myOutput = new FileOutputStream(dstFilePath);
			myInput = context.getAssets().open(fileName);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while(length > 0)
			{
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}

			myOutput.flush();
			myInput.close();
			myOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get4kUrl(Context context,String orginUrl) {
		final PackageManager pm = context.getPackageManager();
		final boolean is4k = pm.hasSystemFeature("com.sony.dtv.hardware.panel.qfhd");
		Uri.Builder builder = Uri.parse(orginUrl).buildUpon();
		builder.appendQueryParameter("is4K", String.valueOf(is4k));
		return builder.toString();
	}

	public static String getSdkUrl(String orginUrl) {
		Uri.Builder builder = Uri.parse(orginUrl).buildUpon();
		builder.appendQueryParameter("SDKVersion", String.valueOf(Build.VERSION.SDK_INT));
		return builder.toString();
	}

	public static int compareVersion(String version1, String version2) throws Exception {
		if (version1 == null || version2 == null) {
			throw new Exception("illegal params.");
		}
		String[] versionArray1 = version1.split("\\.");
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);
		int diff = 0;
		while (idx < minLength
				&& (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
			++idx;
		}

		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}
}
