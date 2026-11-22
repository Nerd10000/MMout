/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.timeout.ReadTimeoutException;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidRequestJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.MethodNotFoundJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.RemoteRpcErrorException;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Connection extends SimpleChannelInboundHandler<JsonElement> {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  44 */   private static final AtomicInteger CONNECTION_ID_COUNTER = new AtomicInteger(0);
/*     */   
/*     */   private final JsonRpcLogger jsonRpcLogger;
/*     */   
/*     */   private final ClientInfo clientInfo;
/*     */   
/*     */   private final ManagementServer managementServer;
/*     */   
/*     */   private final Channel channel;
/*     */   
/*     */   private final MinecraftApi minecraftApi;
/*     */   
/*  52 */   private final AtomicInteger transactionId = new AtomicInteger();
/*     */   
/*  53 */   private final Int2ObjectMap<PendingRpcRequest<?>> pendingRequests = Int2ObjectMaps.synchronize((Int2ObjectMap)new Int2ObjectOpenHashMap());
/*     */   
/*     */   public Connection(Channel channel, ManagementServer managementServer, MinecraftApi minecraftApi, JsonRpcLogger jsonrpcLogger) {
/*  56 */     this.clientInfo = ClientInfo.of(Integer.valueOf(CONNECTION_ID_COUNTER.incrementAndGet()));
/*  57 */     this.managementServer = managementServer;
/*  58 */     this.minecraftApi = minecraftApi;
/*  59 */     this.channel = channel;
/*  60 */     this.jsonRpcLogger = jsonrpcLogger;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  64 */     long time = Util.getMillis();
/*  65 */     this.pendingRequests.int2ObjectEntrySet().removeIf(entry -> {
/*     */           boolean timedOut = ((PendingRpcRequest)entry.getValue()).timedOut(time);
/*     */           if (timedOut)
/*     */             ((PendingRpcRequest)entry.getValue()).resultFuture().completeExceptionally((Throwable)new ReadTimeoutException("RPC method " + String.valueOf(((PendingRpcRequest<Result>)entry.getValue()).method().key().identifier()) + " timed out waiting for response")); 
/*     */           return timedOut;
/*     */         });
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/*  76 */     this.jsonRpcLogger.log(this.clientInfo, "Management connection opened for {}", new Object[] { this.channel.remoteAddress() });
/*  77 */     super.channelActive(ctx);
/*  78 */     this.managementServer.onConnected(this);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/*  83 */     this.jsonRpcLogger.log(this.clientInfo, "Management connection closed for {}", new Object[] { this.channel.remoteAddress() });
/*  84 */     super.channelInactive(ctx);
/*  85 */     this.managementServer.onDisconnected(this);
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/*  90 */     if (cause.getCause() instanceof com.google.gson.JsonParseException) {
/*  91 */       this.channel.writeAndFlush(JsonRPCErrors.PARSE_ERROR.createWithUnknownId(cause.getMessage()));
/*     */       return;
/*     */     } 
/*  94 */     super.exceptionCaught(ctx, cause);
/*  95 */     this.channel.close().awaitUninterruptibly();
/*     */   }
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext channelHandlerContext, JsonElement jsonElement) {
/* 100 */     if (jsonElement.isJsonObject()) {
/* 101 */       JsonObject response = handleJsonObject(jsonElement.getAsJsonObject());
/* 102 */       if (response != null)
/* 103 */         this.channel.writeAndFlush(response); 
/* 105 */     } else if (jsonElement.isJsonArray()) {
/* 106 */       this.channel.writeAndFlush(handleBatchRequest(jsonElement.getAsJsonArray().asList()));
/*     */     } else {
/* 108 */       this.channel.writeAndFlush(JsonRPCErrors.INVALID_REQUEST.createWithUnknownId(null));
/*     */     } 
/*     */   }
/*     */   
/*     */   private JsonArray handleBatchRequest(List<JsonElement> batchRequests) {
/* 113 */     JsonArray batchResponses = new JsonArray();
/* 118 */     Objects.requireNonNull(batchResponses);
/* 118 */     batchRequests.stream().map(batchEntry -> handleJsonObject(batchEntry.getAsJsonObject())).filter(Objects::nonNull).forEach(batchResponses::add);
/* 119 */     return batchResponses;
/*     */   }
/*     */   
/*     */   public void sendNotification(Holder.Reference<? extends OutgoingRpcMethod<Void, ?>> method) {
/* 123 */     sendRequest(method, null, false);
/*     */   }
/*     */   
/*     */   public <Params> void sendNotification(Holder.Reference<? extends OutgoingRpcMethod<Params, ?>> method, Params params) {
/* 127 */     sendRequest(method, params, false);
/*     */   }
/*     */   
/*     */   public <Result> CompletableFuture<Result> sendRequest(Holder.Reference<? extends OutgoingRpcMethod<Void, Result>> method) {
/* 131 */     return sendRequest(method, null, true);
/*     */   }
/*     */   
/*     */   public <Params, Result> CompletableFuture<Result> sendRequest(Holder.Reference<? extends OutgoingRpcMethod<Params, Result>> method, Params params) {
/* 135 */     return sendRequest(method, params, true);
/*     */   }
/*     */   
/*     */   @Contract("_,_,false->null;_,_,true->!null")
/*     */   private <Params, Result> CompletableFuture<Result> sendRequest(Holder.Reference<? extends OutgoingRpcMethod<Params, ? extends Result>> methodHolder, Params params, boolean expectReply) {
/* 140 */     OutgoingRpcMethod<Params, ? extends Result> method = (OutgoingRpcMethod<Params, ? extends Result>)methodHolder.value();
/* 141 */     if (this.minecraftApi.notificationManager().server() == null && !method.attributes().allowPreServerInit())
/* 142 */       return CompletableFuture.failedFuture((Throwable)new InvalidRequestJsonRpcException("Method cannot be dispatched pre server initialization: " + String.valueOf(method))); 
/* 144 */     List<JsonElement> jsonParams = (params != null) ? List.<JsonElement>of(Objects.<JsonElement>requireNonNull(method.encodeParams(params))) : List.<JsonElement>of();
/* 145 */     if (expectReply) {
/* 146 */       CompletableFuture<Result> future = new CompletableFuture<>();
/* 147 */       int id = this.transactionId.incrementAndGet();
/* 148 */       long time = Util.getMillis();
/* 149 */       this.pendingRequests.put(id, new PendingRpcRequest<>(methodHolder, future, time + 5000L));
/* 150 */       this.channel.writeAndFlush(JsonRPCUtils.createRequest(Integer.valueOf(id), methodHolder.key().identifier(), jsonParams));
/* 151 */       return future;
/*     */     } 
/* 153 */     this.channel.writeAndFlush(JsonRPCUtils.createRequest((Integer)null, methodHolder.key().identifier(), jsonParams));
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   JsonObject handleJsonObject(JsonObject jsonObject) {
/*     */     try {
/* 168 */       JsonElement id = JsonRPCUtils.getRequestId(jsonObject);
/* 169 */       String method = JsonRPCUtils.getMethodName(jsonObject);
/* 170 */       JsonElement result = JsonRPCUtils.getResult(jsonObject);
/* 171 */       JsonElement params = JsonRPCUtils.getParams(jsonObject);
/* 172 */       JsonObject error = JsonRPCUtils.getError(jsonObject);
/* 173 */       if (method != null && result == null && error == null) {
/* 174 */         if (id != null && !isValidRequestId(id))
/* 175 */           return JsonRPCErrors.INVALID_REQUEST.createWithUnknownId("Invalid request id - only String, Number and NULL supported"); 
/* 177 */         return handleIncomingRequest(id, method, params);
/*     */       } 
/* 178 */       if (method == null && result != null && error == null && id != null) {
/* 180 */         if (isValidResponseId(id)) {
/* 181 */           handleRequestResponse(id.getAsInt(), result);
/*     */         } else {
/* 183 */           LOGGER.warn("Received respose {} with id {} we did not request", result, id);
/*     */         } 
/* 186 */         return null;
/*     */       } 
/* 187 */       if (method == null && result == null && error != null)
/* 188 */         return handleError(id, error); 
/* 190 */       return JsonRPCErrors.INVALID_REQUEST.createWithoutData((JsonElement)Objects.requireNonNullElse(id, JsonNull.INSTANCE));
/* 192 */     } catch (Exception e) {
/* 193 */       LOGGER.error("Error while handling rpc request", e);
/* 194 */       return JsonRPCErrors.INTERNAL_ERROR.createWithUnknownId("Unknown error handling request - check server logs for stack trace");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isValidRequestId(JsonElement id) {
/* 200 */     return (id.isJsonNull() || GsonHelper.isNumberValue(id) || GsonHelper.isStringValue(id));
/*     */   }
/*     */   
/*     */   private static boolean isValidResponseId(JsonElement id) {
/* 205 */     return GsonHelper.isNumberValue(id);
/*     */   }
/*     */   
/*     */   private JsonObject handleIncomingRequest(JsonElement id, String method, JsonElement params) {
/* 211 */     boolean sendResponse = (id != null);
/*     */     try {
/* 213 */       JsonElement result = dispatchIncomingRequest(method, params);
/* 214 */       if (result == null || !sendResponse)
/* 215 */         return null; 
/* 217 */       return JsonRPCUtils.createSuccessResult(id, result);
/* 218 */     } catch (InvalidParameterJsonRpcException e) {
/* 219 */       LOGGER.debug("Invalid parameter invocation {}: {}, {}", new Object[] { method, params, e.getMessage() });
/* 220 */       return sendResponse ? JsonRPCErrors.INVALID_PARAMS.create(id, e.getMessage()) : null;
/* 221 */     } catch (EncodeJsonRpcException e) {
/* 222 */       LOGGER.error("Failed to encode json rpc response {}: {}", method, e.getMessage());
/* 223 */       return sendResponse ? JsonRPCErrors.INTERNAL_ERROR.create(id, e.getMessage()) : null;
/* 224 */     } catch (InvalidRequestJsonRpcException e) {
/* 225 */       return sendResponse ? JsonRPCErrors.INVALID_REQUEST.create(id, e.getMessage()) : null;
/* 226 */     } catch (MethodNotFoundJsonRpcException e) {
/* 227 */       return sendResponse ? JsonRPCErrors.METHOD_NOT_FOUND.create(id, e.getMessage()) : null;
/* 228 */     } catch (Exception e) {
/* 229 */       LOGGER.error("Error while dispatching rpc method {}", method, e);
/* 230 */       return sendResponse ? JsonRPCErrors.INTERNAL_ERROR.createWithoutData(id) : null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonElement dispatchIncomingRequest(String method, JsonElement params) {
/* 235 */     Identifier identifier = Identifier.tryParse(method);
/* 236 */     if (identifier == null)
/* 237 */       throw new InvalidRequestJsonRpcException("Failed to parse method value: " + method); 
/* 240 */     Optional<IncomingRpcMethod<?, ?>> incomingRpcMethod = BuiltInRegistries.INCOMING_RPC_METHOD.getOptional(identifier);
/* 241 */     if (incomingRpcMethod.isEmpty())
/* 242 */       throw new MethodNotFoundJsonRpcException("Method not found: " + method); 
/* 245 */     IncomingRpcMethod.Attributes attributes = ((IncomingRpcMethod)incomingRpcMethod.get()).attributes();
/* 246 */     NotificationManager notificationManager = this.minecraftApi.notificationManager();
/* 249 */     if (notificationManager != null && notificationManager.server() == null && !attributes.allowPreServerInit())
/* 250 */       throw new InvalidRequestJsonRpcException("Method cannot be dispatched pre server initialization: " + method); 
/* 253 */     if (attributes.runOnMainThread())
/*     */       try {
/* 256 */         return this.minecraftApi.submit(() -> ((IncomingRpcMethod)incomingRpcMethod.get()).apply(this.minecraftApi, params, this.clientInfo)).join();
/* 257 */       } catch (CompletionException e) {
/* 258 */         Throwable throwable = e.getCause();
/* 258 */         if (throwable instanceof RuntimeException) {
/* 258 */           RuntimeException re = (RuntimeException)throwable;
/* 259 */           throw re;
/*     */         } 
/* 261 */         throw e;
/*     */       }  
/* 264 */     return ((IncomingRpcMethod)incomingRpcMethod.get()).apply(this.minecraftApi, params, this.clientInfo);
/*     */   }
/*     */   
/*     */   private void handleRequestResponse(int id, JsonElement result) {
/* 269 */     PendingRpcRequest<?> request = (PendingRpcRequest)this.pendingRequests.remove(id);
/* 270 */     if (request == null) {
/* 271 */       LOGGER.warn("Received unknown response (id: {}): {}", Integer.valueOf(id), result);
/*     */     } else {
/* 273 */       request.accept(result);
/*     */     } 
/*     */   }
/*     */   
/*     */   private JsonObject handleError(JsonElement id, JsonObject error) {
/* 279 */     if (id != null && isValidResponseId(id)) {
/* 280 */       PendingRpcRequest<?> request = (PendingRpcRequest)this.pendingRequests.remove(id.getAsInt());
/* 281 */       if (request != null)
/* 282 */         request.resultFuture().completeExceptionally((Throwable)new RemoteRpcErrorException(id, error)); 
/*     */     } 
/* 285 */     LOGGER.error("Received error (id: {}): {}", id, error);
/* 286 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\Connection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */