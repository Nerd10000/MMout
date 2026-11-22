/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.FilterMask;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ServerTextFilter implements AutoCloseable {
/*  39 */   protected static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  41 */   private static final AtomicInteger WORKER_COUNT = new AtomicInteger(1);
/*     */   
/*     */   private static final ThreadFactory THREAD_FACTORY;
/*     */   
/*     */   private final URL chatEndpoint;
/*     */   
/*     */   private final MessageEncoder chatEncoder;
/*     */   
/*     */   private final IgnoreStrategy chatIgnoreStrategy;
/*     */   
/*     */   private final ExecutorService workerPool;
/*     */   
/*     */   static {
/*  42 */     THREAD_FACTORY = (runnable -> new Thread(runnable, "Chat-Filter-Worker-" + WORKER_COUNT.getAndIncrement()));
/*     */   }
/*     */   
/*     */   protected static ExecutorService createWorkerPool(int maxConcurrentRequests) {
/*  45 */     return Executors.newFixedThreadPool(maxConcurrentRequests, THREAD_FACTORY);
/*     */   }
/*     */   
/*     */   protected ServerTextFilter(URL chatEndpoint, MessageEncoder chatEncoder, IgnoreStrategy chatIgnoreStrategy, ExecutorService workerPool) {
/*  62 */     this.chatIgnoreStrategy = chatIgnoreStrategy;
/*  64 */     this.workerPool = workerPool;
/*  65 */     this.chatEndpoint = chatEndpoint;
/*  66 */     this.chatEncoder = chatEncoder;
/*     */   }
/*     */   
/*     */   protected static URL getEndpoint(URI host, JsonObject source, String id, String def) throws MalformedURLException {
/*  70 */     String endpointConfig = getEndpointFromConfig(source, id, def);
/*  71 */     return host.resolve("/" + endpointConfig).toURL();
/*     */   }
/*     */   
/*     */   protected static String getEndpointFromConfig(JsonObject source, String id, String def) {
/*  75 */     return (source != null) ? GsonHelper.getAsString(source, id, def) : def;
/*     */   }
/*     */   
/*     */   public static ServerTextFilter createFromConfig(DedicatedServerProperties config) {
/*  79 */     String textFilteringConfig = config.textFilteringConfig;
/*  80 */     if (StringUtil.isBlank(textFilteringConfig))
/*  81 */       return null; 
/*  83 */     switch (config.textFilteringVersion) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */     } 
/*  87 */     LOGGER.warn("Could not create text filter - unsupported text filtering version used");
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   protected CompletableFuture<FilteredText> requestMessageProcessing(GameProfile sender, String message, IgnoreStrategy ignoreStrategy, Executor executor) {
/*  94 */     if (message.isEmpty())
/*  95 */       return CompletableFuture.completedFuture(FilteredText.EMPTY); 
/*  97 */     return CompletableFuture.supplyAsync(() -> {
/*     */           JsonObject object = this.chatEncoder.encode(sender, message);
/*     */           try {
/*     */             JsonObject result = processRequestResponse(object, this.chatEndpoint);
/*     */             return filterText(message, ignoreStrategy, result);
/* 102 */           } catch (Exception e) {
/*     */             LOGGER.warn("Failed to validate message '{}'", message, e);
/*     */             return FilteredText.fullyFiltered(message);
/*     */           } 
/*     */         }executor);
/*     */   }
/*     */   
/*     */   protected FilterMask parseMask(String message, JsonArray removedChars, IgnoreStrategy ignoreStrategy) {
/* 112 */     if (removedChars.isEmpty())
/* 113 */       return FilterMask.PASS_THROUGH; 
/* 114 */     if (ignoreStrategy.shouldIgnore(message, removedChars.size()))
/* 115 */       return FilterMask.FULLY_FILTERED; 
/* 117 */     FilterMask mask = new FilterMask(message.length());
/* 118 */     for (int i = 0; i < removedChars.size(); i++)
/* 119 */       mask.setFiltered(removedChars.get(i).getAsInt()); 
/* 121 */     return mask;
/*     */   }
/*     */   
/*     */   public void close() {
/* 126 */     this.workerPool.shutdownNow();
/*     */   }
/*     */   
/*     */   protected void drainStream(InputStream input) throws IOException {
/* 130 */     byte[] trashcan = new byte[1024];
/* 131 */     while (input.read(trashcan) != -1);
/*     */   }
/*     */   
/*     */   private JsonObject processRequestResponse(JsonObject payload, URL url) throws IOException {
/* 136 */     HttpURLConnection connection = makeRequest(payload, url);
/* 138 */     InputStream is = connection.getInputStream();
/*     */     try {
/* 139 */       if (connection.getResponseCode() == 204) {
/* 140 */         JsonObject jsonObject = new JsonObject();
/* 147 */         if (is != null)
/* 147 */           is.close(); 
/*     */         return jsonObject;
/*     */       } 
/*     */       try {
/*     */         JsonObject jsonObject = LenientJsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
/*     */         drainStream(is);
/*     */         return jsonObject;
/*     */       } finally {
/*     */         drainStream(is);
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (is != null)
/*     */         try {
/*     */           is.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected HttpURLConnection makeRequest(JsonObject payload, URL url) throws IOException {
/* 151 */     HttpURLConnection connection = getURLConnection(url);
/* 153 */     setAuthorizationProperty(connection);
/* 155 */     OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
/*     */     try {
/* 156 */       JsonWriter jsonWriter = new JsonWriter(writer);
/*     */       try {
/* 157 */         Streams.write((JsonElement)payload, jsonWriter);
/* 158 */         jsonWriter.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           jsonWriter.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 159 */       writer.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         writer.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/* 161 */     int responseCode = connection.getResponseCode();
/* 162 */     if (responseCode < 200 || responseCode >= 300)
/* 163 */       throw new RequestFailedException("" + responseCode + " " + responseCode); 
/* 165 */     return connection;
/*     */   }
/*     */   
/*     */   protected int connectionReadTimeout() {
/* 171 */     return 2000;
/*     */   }
/*     */   
/*     */   protected HttpURLConnection getURLConnection(URL url) throws IOException {
/* 175 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 176 */     connection.setConnectTimeout(15000);
/* 177 */     connection.setReadTimeout(connectionReadTimeout());
/* 178 */     connection.setUseCaches(false);
/* 179 */     connection.setDoOutput(true);
/* 180 */     connection.setDoInput(true);
/* 182 */     connection.setRequestMethod("POST");
/* 183 */     connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
/* 184 */     connection.setRequestProperty("Accept", "application/json");
/* 185 */     connection.setRequestProperty("User-Agent", "Minecraft server" + SharedConstants.getCurrentVersion().name());
/* 187 */     return connection;
/*     */   }
/*     */   
/*     */   public TextFilter createContext(GameProfile gameProfile) {
/* 191 */     return new PlayerContext(gameProfile);
/*     */   }
/*     */   
/*     */   protected abstract FilteredText filterText(String paramString, IgnoreStrategy paramIgnoreStrategy, JsonObject paramJsonObject);
/*     */   
/*     */   protected abstract void setAuthorizationProperty(HttpURLConnection paramHttpURLConnection);
/*     */   
/*     */   protected static class RequestFailedException extends RuntimeException {
/*     */     protected RequestFailedException(String message) {
/* 196 */       super(message);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class PlayerContext implements TextFilter {
/*     */     protected final GameProfile profile;
/*     */     
/*     */     protected final Executor streamExecutor;
/*     */     
/*     */     protected PlayerContext(GameProfile profile) {
/* 205 */       this.profile = profile;
/* 206 */       ConsecutiveExecutor streamProcessor = new ConsecutiveExecutor(ServerTextFilter.this.workerPool, "chat stream for " + profile.name());
/* 207 */       Objects.requireNonNull(streamProcessor);
/* 207 */       this.streamExecutor = streamProcessor::schedule;
/*     */     }
/*     */     
/*     */     public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages) {
/* 215 */       List<CompletableFuture<FilteredText>> requests = (List<CompletableFuture<FilteredText>>)messages.stream().map(message -> ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor)).collect(ImmutableList.toImmutableList());
/* 217 */       return Util.sequenceFailFast(requests)
/*     */         
/* 219 */         .exceptionally(e -> ImmutableList.of());
/*     */     }
/*     */     
/*     */     public CompletableFuture<FilteredText> processStreamMessage(String message) {
/* 225 */       return ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IgnoreStrategy {
/*     */     public static final IgnoreStrategy NEVER_IGNORE = (message, removedCharCount) -> false;
/*     */     
/*     */     public static final IgnoreStrategy IGNORE_FULLY_FILTERED;
/*     */     
/*     */     static {
/* 232 */       IGNORE_FULLY_FILTERED = ((message, removedCharCount) -> (message.length() == removedCharCount));
/*     */     }
/*     */     
/*     */     static IgnoreStrategy ignoreOverThreshold(int threshold) {
/* 235 */       return (message, removedCharCount) -> (removedCharCount >= threshold);
/*     */     }
/*     */     
/*     */     static IgnoreStrategy select(int hashesToDrop) {
/* 239 */       switch (hashesToDrop) {
/*     */         case -1:
/*     */         
/*     */         case 0:
/*     */         
/*     */       } 
/* 239 */       return 
/*     */         
/* 242 */         ignoreOverThreshold(hashesToDrop);
/*     */     }
/*     */     
/*     */     boolean shouldIgnore(String param1String, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface MessageEncoder {
/*     */     JsonObject encode(GameProfile param1GameProfile, String param1String);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerTextFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */