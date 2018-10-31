package http

import java.io.File


import javax.net.ssl.SSLContext;
import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.conn.ssl.{SSLConnectionSocketFactory, TrustSelfSignedStrategy}
import org.apache.http.impl.client.{BasicCookieStore, HttpClients}
import org.apache.http.ssl.SSLContexts


object KeystoreLoader {

  def setUpClient(cacertPath:String, cacertPass:Array[Char], httpCookieStore:BasicCookieStore) = {
    //try to use a cacert file that is outside the jar,
    // in case the cacerts file needs to be recreated issued to users without a full update
    var sslcontext:SSLContext = null
    try{
      val url = (cacertPath).dropWhile(p => p=='/') //drop the first "/" in the path
      val keystore = new File(url)
      sslcontext = SSLContexts.custom()
        .loadTrustMaterial(keystore, cacertPass,
          new TrustSelfSignedStrategy())
        .build();
    } catch {
      case e:Throwable=>
        //e:FileNotFoundException usually,
        // but I'll need a working sslcontext in any case, the above is only a fallback

        val url = getClass.getResource(cacertPath)
        //val file = Source.fromInputStream(getClass.getResourceAsStream("/dodcacerts")).mkString
        //val keystore = new File(url.getFile()) // needs to be outside of jar for this to work
        sslcontext = SSLContexts.custom()
          .loadTrustMaterial(url, cacertPass,
            new TrustSelfSignedStrategy())
          .build();
    }


    val sslsf = new SSLConnectionSocketFactory(
      sslcontext,
      Array( "TLSv1" ),
      null,
      SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    val globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    val client = HttpClients.custom()
      .setSSLSocketFactory(sslsf)
      .setDefaultRequestConfig(globalConfig)
      .setDefaultCookieStore(httpCookieStore)
      .build();
    //HttpClientBuilder.create().build();//new DefaultHttpClient .setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);

    client
  }
}



