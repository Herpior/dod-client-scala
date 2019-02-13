package http

/**
  * An object for setting up the keystore from a cacerts file,
  * or if no external file is found, from the cacerts file bundled in with the jar.

  * @author Qazhax
  */

import java.io.File

import javax.net.ssl.SSLContext
import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.conn.ssl.{SSLConnectionSocketFactory, TrustSelfSignedStrategy}
import org.apache.http.impl.client.{BasicCookieStore, CloseableHttpClient, HttpClients}
import org.apache.http.ssl.SSLContexts


object KeystoreLoader {

  def setUpClient(cacertPath:String, cacertPass:Array[Char], httpCookieStore:BasicCookieStore): CloseableHttpClient = {
    // try to use a cacerts file that is outside the jar,
    // in case the cacerts file needs to be recreated issued to users without a full update
    var sslcontext:SSLContext = null
    try{
      val url = cacertPath.dropWhile(p => p=='/') //drop the first "/" in the path
      val keystore = new File(url)
      sslcontext = SSLContexts.custom()
        .loadTrustMaterial(keystore, cacertPass,
          new TrustSelfSignedStrategy())
        .build();
    } catch {
      case e:Throwable=>
        // it's e:FileNotFoundException usually,
        // but I'll need to retry after any exception so

        val url = getClass.getResource(cacertPath)
        sslcontext = SSLContexts.custom()
          .loadTrustMaterial(url, cacertPass,
            new TrustSelfSignedStrategy())
          .build();
    }


    val sslsf = new SSLConnectionSocketFactory(
      sslcontext,
      Array( "TLSv1" ),
      null,
      SSLConnectionSocketFactory.getDefaultHostnameVerifier)
    val globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()
    val client = HttpClients.custom()
      .setSSLSocketFactory(sslsf)
      .setDefaultRequestConfig(globalConfig)
      .setDefaultCookieStore(httpCookieStore)
      .build()
    client
  }
}



