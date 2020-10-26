package com.smartpc.chiyun.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @ClassName: GCMD5Util
 * @Description:TODO(完成密码MD5的加密)
 * @author: Chivalrous
 * @date: 2012-11-23 下午3:57:08
 */
public class GCMD5Util {

	private static final Logger log = LoggerFactory.getLogger(GCMD5Util.class);
	private static final String HEX_NUMS_STR = "0123456789ABCDEF";
	private static final Integer SALT_LENGTH = 12;

	/**
	 * 将16进制字符串转换成字节数组
	 *
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] hexChars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
					.indexOf(hexChars[pos + 1]));
		}
		return result;
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 验证口令是否合法
	 *
	 * @param password
	 * @param passwordInDb
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean validPassword(String password, String passwordInDb) {
		try {
			// 将16进制字符串格式口令转换成字节数组
			byte[] pwdInDb = hexStringToByte(passwordInDb);
			// 声明盐变量
			byte[] salt = new byte[SALT_LENGTH];
			// 将盐从数据库中保存的口令字节数组中提取出来
			System.arraycopy(pwdInDb, 0, salt, 0, SALT_LENGTH);
			// 创建消息摘要对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 将盐数据传入消息摘要对象
			md.update(salt);
			// 将口令的数据传给消息摘要对象
			md.update(password.getBytes("UTF-8"));
			// 生成输入口令的消息摘要
			byte[] digest = md.digest();
			// 声明一个保存数据库中口令消息摘要的变量
			byte[] digestInDb = new byte[pwdInDb.length - SALT_LENGTH];
			// 取得数据库中口令的消息摘要
			System.arraycopy(pwdInDb, SALT_LENGTH, digestInDb, 0,
					digestInDb.length);
			// 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
			if (Arrays.equals(digest, digestInDb)) {
				// 口令正确返回口令匹配消息
				return true;
			} else {
				// 口令不正确返回口令不匹配消息
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.error("对比数据中的MD5摘要和传入校验的进行匹配失败！");
			return false;
		}
	}

	/**
	 * 获得加密后的16进制形式口令
	 *
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncryptedPwd(String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// 声明加密后的口令数组变量
		byte[] pwd = null;
		// 随机数生成器
		SecureRandom random = new SecureRandom();
		// 声明盐数组变量
		byte[] salt = new byte[SALT_LENGTH];
		// 将随机数放入盐变量中
		random.nextBytes(salt);

		// 声明消息摘要对象
		MessageDigest md = null;
		// 创建消息摘要
		md = MessageDigest.getInstance("MD5");
		// 将盐数据传入消息摘要对象
		md.update(salt);
		// 将口令的数据传给消息摘要对象
		md.update(password.getBytes("UTF-8"));
		// 获得消息摘要的字节数组
		byte[] digest = md.digest();

		// 因为要在口令的字节数组中存放盐，所以加上盐的字节长度
		pwd = new byte[digest.length + SALT_LENGTH];
		// 将盐的字节拷贝到生成的加密口令字节数组的前12个字节，以便在验证口令时取出盐
		System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
		// 将消息摘要拷贝到加密口令字节数组从第13个字节开始的字节
		System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
		// 将字节数组格式加密后的口令转化为16进制字符串格式的口令
		return byteToHexString(pwd);
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < src.length() / 2; i++) {
			ret[i] = uniteBytes(tmp[(i * 2)], tmp[(i * 2 + 1)]);
		}
		return ret;
	}

	public static String byte2MD5(byte[] src) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		byte[] md5Bytes = md5.digest(src);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = md5Bytes[i] & 0xFF;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 */
	public static String getMD5(String encStr) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(encStr.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = md5Bytes[i] & 0xFF;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
