package io

import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import java.io.ObjectInputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.security.SecureRandom

import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

object Crypt {

  def decipherFrom(path:String): String = {
    val ois = new ObjectInputStream(new FileInputStream(path+"2"))
    val ks = new DESKeySpec(ois.readObject().asInstanceOf[Array[Byte]])
    val skf = SecretKeyFactory.getInstance("DES")
    val key = skf.generateSecret(ks)

    val c = Cipher.getInstance("DES/CFB8/NoPadding")
    c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ois.readObject().asInstanceOf[Array[Byte]]))
    val cis = new CipherInputStream(new FileInputStream(path+"1"), c)
    val br = new BufferedReader(new InputStreamReader(cis))
    val res = br.readLine()
    //System.out.println(res+" in crypt")
    res
  }
  
  def encipherTo(ciphering:String,path:String): Unit = {
    val kg = KeyGenerator.getInstance("DES")
    kg.init(new SecureRandom())
    val key = kg.generateKey()
    val skf = SecretKeyFactory.getInstance("DES")
    val spec = Class.forName("javax.crypto.spec.DESKeySpec")
    val ks = skf.getKeySpec(key, spec).asInstanceOf[DESKeySpec]
    val oos = new ObjectOutputStream(new FileOutputStream(path+"2"))
    oos.writeObject(ks.getKey)

    val c = Cipher.getInstance("DES/CFB8/NoPadding")
    c.init(Cipher.ENCRYPT_MODE, key)
    val cos = new CipherOutputStream(new FileOutputStream(path+"1"), c)
    val pw = new PrintWriter(new OutputStreamWriter(cos))
    pw.println(ciphering)
    pw.close()
    oos.writeObject(c.getIV)
    oos.close()
  }
  
}