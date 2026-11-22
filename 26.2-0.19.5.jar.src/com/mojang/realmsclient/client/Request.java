/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ public abstract class Request<T extends Request<T>> {
/*     */   protected HttpURLConnection connection;
/*     */   
/*     */   private boolean connected;
/*     */   
/*     */   protected String url;
/*     */   
/*     */   private static final int DEFAULT_READ_TIMEOUT = 60000;
/*     */   
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
/*     */   
/*     */   private static final String NETWORK_PROTOCOL_KEY = "X-NetworkProtocolVersion";
/*     */   
/*     */   private static final String IS_SNAPSHOT_KEY = "Is-Prerelease";
/*     */   
/*     */   private static final String COOKIE_KEY = "Cookie";
/*     */   
/*     */   public Request(String url, int connectTimeout, int readTimeout) {
/*     */     try {
/*  31 */       this.url = url;
/*  32 */       Proxy proxy = RealmsClientConfig.getProxy();
/*  34 */       if (proxy != null) {
/*  35 */         this.connection = (HttpURLConnection)(new URL(url)).openConnection(proxy);
/*     */       } else {
/*  37 */         this.connection = (HttpURLConnection)(new URL(url)).openConnection();
/*     */       } 
/*  40 */       this.connection.setConnectTimeout(connectTimeout);
/*  41 */       this.connection.setReadTimeout(readTimeout);
/*  42 */     } catch (MalformedURLException e) {
/*  43 */       throw new RealmsHttpException(e.getMessage(), e);
/*  44 */     } catch (IOException e) {
/*  45 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cookie(String key, String value) {
/*  50 */     cookie(this.connection, key, value);
/*     */   }
/*     */   
/*     */   public static void cookie(HttpURLConnection connection, String key, String value) {
/*  54 */     String cookie = connection.getRequestProperty("Cookie");
/*  55 */     if (cookie == null) {
/*  56 */       connection.setRequestProperty("Cookie", key + "=" + key);
/*     */     } else {
/*  58 */       connection.setRequestProperty("Cookie", cookie + ";" + cookie + "=" + key);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addVersionHeaders(int networkProtocol, boolean isSnapshot) {
/*  63 */     this.connection.addRequestProperty("X-NetworkProtocolVersion", String.valueOf(networkProtocol));
/*  64 */     this.connection.addRequestProperty("Is-Prerelease", String.valueOf(isSnapshot));
/*     */   }
/*     */   
/*     */   public int getRetryAfterHeader() {
/*  68 */     return getRetryAfterHeader(this.connection);
/*     */   }
/*     */   
/*     */   public static int getRetryAfterHeader(HttpURLConnection connection) {
/*  72 */     String pauseTime = connection.getHeaderField("Retry-After");
/*     */     try {
/*  74 */       return Integer.valueOf(pauseTime).intValue();
/*  75 */     } catch (Exception ignored) {
/*  76 */       return 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int responseCode() {
/*     */     try {
/*  82 */       connect();
/*  83 */       return this.connection.getResponseCode();
/*  84 */     } catch (Exception e) {
/*  85 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String text() {
/*     */     try {
/*     */       String result;
/*  91 */       connect();
/*  94 */       if (responseCode() >= 400) {
/*  95 */         result = read(this.connection.getErrorStream());
/*     */       } else {
/*  97 */         result = read(this.connection.getInputStream());
/*     */       } 
/* 100 */       dispose();
/* 101 */       return result;
/* 102 */     } catch (IOException e) {
/* 103 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String read(InputStream in) throws IOException {
/* 108 */     if (in == null)
/* 109 */       return ""; 
/* 111 */     InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
/* 112 */     StringBuilder sb = new StringBuilder();
/*     */     int x;
/* 113 */     for (x = streamReader.read(); x != -1; x = streamReader.read())
/* 114 */       sb.append((char)x); 
/* 117 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void dispose() {
/* 121 */     byte[] bytes = new byte[1024];
/*     */     try {
/* 123 */       InputStream in = this.connection.getInputStream();
/* 124 */       while (in.read(bytes) > 0);
/* 127 */       in.close();
/* 128 */     } catch (Exception ignore) {
/*     */       try {
/* 130 */         InputStream errorStream = this.connection.getErrorStream();
/* 131 */         if (errorStream == null)
/*     */           return; 
/* 135 */         while (errorStream.read(bytes) > 0);
/* 138 */         errorStream.close();
/* 139 */       } catch (IOException iOException) {}
/*     */     } finally {
/* 142 */       if (this.connection != null)
/* 143 */         this.connection.disconnect(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected T connect() {
/* 150 */     if (this.connected)
/* 151 */       return (T)this; 
/* 153 */     T t = doConnect();
/* 154 */     this.connected = true;
/* 155 */     return t;
/*     */   }
/*     */   
/*     */   protected abstract T doConnect();
/*     */   
/*     */   public static Request<?> get(String url) {
/* 162 */     return new Get(url, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> get(String url, int connectTimeoutMillis, int readTimeoutMillis) {
/* 166 */     return new Get(url, connectTimeoutMillis, readTimeoutMillis);
/*     */   }
/*     */   
/*     */   public static Request<?> post(String uri, String content) {
/* 170 */     return new Post(uri, content, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> post(String uri, String content, int connectTimeoutMillis, int readTimeoutMillis) {
/* 174 */     return new Post(uri, content, connectTimeoutMillis, readTimeoutMillis);
/*     */   }
/*     */   
/*     */   public static Request<?> delete(String url) {
/* 178 */     return new Delete(url, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> put(String url, String content) {
/* 182 */     return new Put(url, content, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> put(String url, String content, int connectTimeoutMillis, int readTimeoutMillis) {
/* 186 */     return new Put(url, content, connectTimeoutMillis, readTimeoutMillis);
/*     */   }
/*     */   
/*     */   public String getHeader(String header) {
/* 190 */     return getHeader(this.connection, header);
/*     */   }
/*     */   
/*     */   public static String getHeader(HttpURLConnection connection, String header) {
/*     */     try {
/* 195 */       return connection.getHeaderField(header);
/* 196 */     } catch (Exception ignored) {
/* 197 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Delete extends Request<Delete> {
/*     */     public Delete(String uri, int connectTimeout, int readTimeout) {
/* 203 */       super(uri, connectTimeout, readTimeout);
/*     */     }
/*     */     
/*     */     public Delete doConnect() {
/*     */       try {
/* 209 */         this.connection.setDoOutput(true);
/* 210 */         this.connection.setRequestMethod("DELETE");
/* 211 */         this.connection.connect();
/* 212 */         return this;
/* 213 */       } catch (Exception e) {
/* 214 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Get extends Request<Get> {
/*     */     public Get(String uri, int connectTimeout, int readTimeout) {
/* 221 */       super(uri, connectTimeout, readTimeout);
/*     */     }
/*     */     
/*     */     public Get doConnect() {
/*     */       try {
/* 227 */         this.connection.setDoInput(true);
/* 228 */         this.connection.setDoOutput(true);
/* 229 */         this.connection.setUseCaches(false);
/* 230 */         this.connection.setRequestMethod("GET");
/* 231 */         return this;
/* 232 */       } catch (Exception e) {
/* 233 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Put extends Request<Put> {
/*     */     private final String content;
/*     */     
/*     */     public Put(String uri, String content, int connectTimeout, int readTimeout) {
/* 242 */       super(uri, connectTimeout, readTimeout);
/* 243 */       this.content = content;
/*     */     }
/*     */     
/*     */     public Put doConnect() {
/*     */       try {
/* 249 */         if (this.content != null)
/* 250 */           this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8"); 
/* 253 */         this.connection.setDoOutput(true);
/* 254 */         this.connection.setDoInput(true);
/* 255 */         this.connection.setRequestMethod("PUT");
/* 256 */         OutputStream out = this.connection.getOutputStream();
/* 257 */         OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 258 */         writer.write(this.content);
/* 259 */         writer.close();
/* 260 */         out.flush();
/* 261 */         return this;
/* 262 */       } catch (Exception e) {
/* 263 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Post extends Request<Post> {
/*     */     private final String content;
/*     */     
/*     */     public Post(String uri, String content, int connectTimeout, int readTimeout) {
/* 272 */       super(uri, connectTimeout, readTimeout);
/* 273 */       this.content = content;
/*     */     }
/*     */     
/*     */     public Post doConnect() {
/*     */       try {
/* 279 */         if (this.content != null)
/* 280 */           this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8"); 
/* 283 */         this.connection.setDoInput(true);
/* 284 */         this.connection.setDoOutput(true);
/* 285 */         this.connection.setUseCaches(false);
/* 286 */         this.connection.setRequestMethod("POST");
/* 287 */         OutputStream out = this.connection.getOutputStream();
/* 288 */         OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 289 */         writer.write(this.content);
/* 290 */         writer.close();
/* 291 */         out.flush();
/* 292 */         return this;
/* 293 */       } catch (Exception e) {
/* 294 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\Request.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */