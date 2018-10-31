package http

import http.HttpHandler.{client, getClass, httpCookieStore}
import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.conn.ssl.{SSLConnectionSocketFactory, TrustSelfSignedStrategy}
import org.apache.http.impl.client.{BasicCookieStore, HttpClients}
import org.apache.http.ssl.SSLContexts


object KeystoreLoader {

  def setUpClient(cacertpath:String, cacertPass:Array[Char], cookiestore:BasicCookieStore) = {

    val url = getClass.getResource(cacertpath)
    //val file = Source.fromInputStream(getClass.getResourceAsStream("/dodcacerts")).mkString
    //val keystore = new File(url.getFile()) // needs to be outside of jar for this to work
    val sslcontext = SSLContexts.custom()
      .loadTrustMaterial(url, cacertPass,
        new TrustSelfSignedStrategy())
      .build();
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



