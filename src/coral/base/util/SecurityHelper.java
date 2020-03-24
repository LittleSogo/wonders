package coral.base.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;

import wfc.service.exception.InnerException;

public class SecurityHelper {

	protected SecurityHelper() {
		try {
			lock.lock();

			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchPaddingException ex) {
			throw new InnerException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new InnerException(ex);
		} finally {
			lock.unlock();
		}
	}

	public SecurityHelper(SecretKey key) {
		this();
		this.key = key;
	}

	public SecurityHelper(String keyHex) {
		this(getKey(keyHex));
	}

	/**
	 * DES 解密
	 * 
	 * @param dest
	 *            已加密的目标数据
	 * @return 解密后的源数据
	 */
	public byte[] decode(byte[] dest) {
		try {
			return decrypt(dest);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * DES 解密
	 * 
	 * @param dest
	 *            已加密的目标数据
	 * @return 解密后的源数据
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public byte[] decrypt(byte[] dest) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		try {
			lock.lock();

			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(dest);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * DES 加密
	 * 
	 * @param source
	 *            源数据
	 * @return 加密后的目标数据
	 */
	public byte[] encode(byte[] source) {
		try {
			return encrypt(source);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * DES 加密
	 * 
	 * @param source
	 *            源数据
	 * @return 加密后的目标数据
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public byte[] encrypt(byte[] source) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		try {
			lock.lock();

			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(source);
		} finally {
			lock.unlock();
		}
	}

	public SecretKey getKey() {
		return key;
	}

	/**
	 * 将字节流转成十六进制数字字符串
	 * 
	 * @param source
	 *            字节流
	 * @return 十六进制数字字符串
	 */
	public static String byteToHexStr(byte[] source) {
		//return new String(Hex.encodeHex(source));
		return null;
	}

	/**
	 * 根据种子字符串生成密钥
	 * 
	 * @param seed
	 *            种子字符串
	 * @param seedEnc
	 *            种子字符编码
	 * @return 密钥
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static SecretKey generateKey(String seed, String seedEnc)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
		generator.init(new SecureRandom(seed.getBytes(seedEnc)));
		return generator.generateKey();
	}

	protected static SecretKey getKey(String keyHex) {
		return new SecretKeySpec(hexStrToByte(keyHex), ALGORITHM);
	}

	/**
	 * 将十六进制数字字符串转成字节流
	 * 
	 * @param hexStr
	 *            十六进制数字字符串
	 * @return 字节流
	 */
	public static byte[] hexStrToByte(String hexStr) {
		return null;
		/*try {
			return Hex.decodeHex(hexStr.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}*/
	}

	private Cipher cipher;

	private SecretKey key;

	private ReentrantLock lock = new ReentrantLock();

	public static final String ALGORITHM = "DES";

}