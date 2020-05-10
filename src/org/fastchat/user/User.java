package org.fastchat.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//User class
@SuppressWarnings("serial")
public class User implements Serializable {
	private String name;
	private String surname;
	private String username;
	private String pass;
	private KeyPair keys;
	private File inbox;
	private File root;
	private File check;
	private boolean isActive = false;

	// Call from REGISTER
	// Constructor for new registered user
	public User(String n, String s, String u, String p) throws Exception, NullPointerException {
		name = n;
		surname = s;
		username = u;
		pass = p;
		root = new File("FastChat/" + username);
		root.mkdir();
		inbox = new File(root + "/" + username + "Inbox"); // Inbox 
		inbox.mkdir();
		check = new File("Checker/" + username + "Check"); // Inbox check
		check.mkdir();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");  //Key pair
		keyPairGenerator.initialize(1024);
		keys = keyPairGenerator.generateKeyPair();

		sendCertRequest();
	}

	public void sendCertRequest() throws IOException {
		FileOutputStream foStream = new FileOutputStream(new File(root + "/" + getUsername() + ".key"));
		FileOutputStream admStream = new FileOutputStream(
				new File("administration/requests" + "/" + getUsername() + ".key"));
		String keypem = "-----BEGIN RSA PRIVATE KEY-----\n"
				+ Base64.getEncoder().encodeToString(keys.getPrivate().getEncoded())
				+ "\n-----END RSA PRIVATE KEY-----\n";
		foStream.write(keypem.getBytes());
		foStream.close();
		admStream.write(keypem.getBytes());
		admStream.close();
	}

	// Get and set functions
	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getUsername() {
		return username;
	}

	public String getPass() {
		return pass;
	}

	//Public Key from Certificate
	public PublicKey getPub() throws FileNotFoundException, CertificateException {
		X509Certificate userCertificate=loadCertificate();
		userCertificate.checkValidity();
		return userCertificate.getPublicKey();
	}

	public PrivateKey getPriv() {
		return keys.getPrivate();
	}

	public Path getRoot() {
		return root.toPath();
	}

	public void setActive(boolean b) {
		isActive = b;
	}

	public boolean getActive() {
		return isActive;
	}

	public Path getInbox() {
		return inbox.toPath();
	}

	public Path getCheck() {
		return check.toPath();
	}

	public File getInboxFile(User sender) {
		File file = new File(getInbox() + "/" + sender.getUsername() + ".txt");
		return file;
	}

	public X509Certificate loadCertificate() throws FileNotFoundException, CertificateException {
    
		FileInputStream fileCertInputStream;
		X509Certificate senderCertificate = null;
		//File from system
		fileCertInputStream = new FileInputStream(
				"/media/halim/DISK/Fakultet/KRIPT/PROJEKTNI/Original/administration/certs/" + getUsername()
						+ ".pem");
		CertificateFactory cFactory = CertificateFactory.getInstance("X.509");
		senderCertificate = (X509Certificate) cFactory.generateCertificate(fileCertInputStream);
		return senderCertificate;
	}

	public SecretKey generateAESkey() throws NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128, random);
		SecretKey aeSecretKey = keyGenerator.generateKey();
		return aeSecretKey;
	}

	// FUNCTIONS FOR ENCRYPTION
	public String encryptMessage(String input, String key) throws Exception {
		byte[] crypted = null;
		SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skey);
		crypted = cipher.doFinal(input.getBytes());
		java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
		return new String(encoder.encodeToString(crypted));
	}

	public String encryptAESKey(SecretKey key, PublicKey pubKey) throws Exception {
		String aesKey1 = Base64.getEncoder().encodeToString(key.getEncoded());
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] aesEnc = cipher.doFinal(aesKey1.getBytes());
		String returnKey = Base64.getEncoder().encodeToString(aesEnc);
		return returnKey;
	}

	public byte[] hashKeyGenerate(String aesKey) throws Exception {
		// Initialize message diggest
		MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
		byte[] keyhash = mDigest.digest(aesKey.getBytes());
		// Encrypt diggest
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getPriv());
		byte[] aesSignature = cipher.doFinal(keyhash);
		return aesSignature;
	}

	public void checkerUpdate(User receiver, String ext) throws IOException {
		BufferedWriter check = new BufferedWriter(new FileWriter(receiver.getCheck() + "/" + getUsername() + ext));
		Random random = new Random();
		Integer num = random.nextInt(100);
		check.write(num);
		check.close();
	}

	public byte[] signatureMessage(User receiver) throws Exception {
		// Initialize
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(getPriv());
		byte[] msgBytes = Files.readAllBytes(Paths.get(receiver.getInbox() + "/" + getUsername() + ".msg"));
		signature.update(msgBytes);
		byte[] digSign = signature.sign();

		return digSign;
	}

	public void sendMessage(String message, User receiver) throws Exception {

		// TODO HASH AES-a i HASH MESSAGEa
		// AES hash pomocu message diggest
		// Message hash with Signature
		File keyFile = new File(receiver.getInbox() + "/" + getUsername() + ".kgn");
		File messageFile = new File(receiver.getInbox() + "/" + getUsername() + ".msg");
		FileOutputStream keyFileSignature = new FileOutputStream(receiver.getInbox() + "/" + getUsername() + ".ksgn");
		FileOutputStream messageSignature = new FileOutputStream(receiver.getInbox() + "/" + getUsername() + ".msgn");

		SecretKey aeSecretKey = generateAESkey();
		String aesString = Base64.getEncoder().encodeToString(aeSecretKey.getEncoded());

		/**
		 * Encrypted Key and Message Will be written in files
		 */
		String encryptedMessage = encryptMessage(message, aesString);
		String encryptedAESkey = encryptAESKey(aeSecretKey, receiver.getPub());

		/**
		 * Files for key and message
		 */
		BufferedWriter key = new BufferedWriter(new FileWriter(keyFile));
		BufferedWriter msg = new BufferedWriter(new FileWriter(messageFile));
		key.write(encryptedAESkey);
		key.close();
		msg.write(encryptedMessage);
		msg.close();

		byte[] hashAESkey = hashKeyGenerate(encryptedAESkey);
		byte[] hasgMessage = signatureMessage(receiver);
		keyFileSignature.write(hashAESkey);
		messageSignature.write(hasgMessage);
		keyFileSignature.close();
		messageSignature.close();
		// Add file to CHECKER, temp operations for WatchInbox service
		checkerUpdate(receiver, ".txt");
	}

	public void sendSteganographyMessage(String message, User receiver, boolean fol) throws IOException {
		File encImg = new File("fc.bmp");
		String inboxUser = receiver.getInbox() + "/" + getUsername();
		Steganography.encode(encImg, inboxUser, message);
		if (fol == false)
			checkerUpdate(receiver, ".stg");
		else {
			checkerUpdate(receiver, ".lst");
		}
	}

	// FUNCTIONS FOR DECRYPTION

	public String readSteganograhyMessage(User sender) {
		File image = new File(getInbox() + "/" + sender.getUsername() + ".bmp");
		return Steganography.decode(image);
	}

	public String decryptAESKey(String aesEnc) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, getPriv());
		byte[] decAes = cipher.doFinal(Base64.getDecoder().decode(aesEnc));
		String aeskey = new String(decAes);
		return aeskey;
	}

	public String decryptMessage(String msg, String aeskey) throws Exception {
		byte[] output = null;
		java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
		SecretKeySpec skey = new SecretKeySpec(aeskey.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skey);
		output = cipher.doFinal(decoder.decode(msg));
		return new String(output);
	}

	public boolean checkKeySignature(User sender) throws Exception {
		// Initialize
		File keyHash = new File(getInbox() + "/" + sender.getUsername() + ".ksgn");
		byte[] hashKey = Files.readAllBytes(keyHash.toPath());

		// Decrypt AES key hash
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, sender.getPub());
		byte[] decryptedHashKey = cipher.doFinal(hashKey);

		// Generate key hash to check
		File keyFile = new File(getInbox() + "/" + sender.getUsername() + ".kgn");
		BufferedReader key = new BufferedReader(new FileReader(keyFile));
		String temp = key.readLine();
		String keyString = "";
		while (temp != null) {
			keyString += temp;
			temp = key.readLine();
		}
		key.close();
		MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
		byte[] hashKeyCheck = mDigest.digest(keyString.getBytes());

		return Arrays.equals(decryptedHashKey, hashKeyCheck);
	}

	public boolean checkMsgSignature(User sender) throws Exception {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(sender.getPub());

		File receivedMsg = new File(getInbox() + "/" + sender.getUsername() + ".msg");
		byte[] digSign = Files.readAllBytes(receivedMsg.toPath());
		signature.update(digSign);

		File receivedSign = new File(getInbox() + "/" + sender.getUsername() + ".msgn");
		byte[] digSignReceived = Files.readAllBytes(receivedSign.toPath());

		return signature.verify(digSignReceived);
	}

	public String readMessage(User sender) throws Exception {
		File keyFile = new File(getInbox() + "/" + sender.getUsername() + ".kgn");
		File messageFile = new File(getInbox() + "/" + sender.getUsername() + ".msg");
		boolean checkKeySignature = checkKeySignature(sender);
		boolean checkMsgSignature = checkMsgSignature(sender);

		if (checkKeySignature == true && checkMsgSignature == true) {
			String temp;
			String keyString = "";
			String msgString = "";
			BufferedReader key = new BufferedReader(new FileReader(keyFile));
			BufferedReader msg = new BufferedReader(new FileReader(messageFile));

			/**
			 * Read encrypted key
			 */
			temp = key.readLine();
			while (temp != null) {
				keyString += temp;
				temp = key.readLine();
			}
			temp = null;
			key.close();

			/**
			 * Read encrypted message
			 */
			temp = msg.readLine();
			while (temp != null) {
				msgString += temp;
				temp = msg.readLine();
			}
			msg.close();

			String decryptedAESString = decryptAESKey(keyString);
			String decryptedMessage = decryptMessage(msgString, decryptedAESString);

			return decryptedMessage;
		} else
			return "Key or message corrupted!";
	}
}
