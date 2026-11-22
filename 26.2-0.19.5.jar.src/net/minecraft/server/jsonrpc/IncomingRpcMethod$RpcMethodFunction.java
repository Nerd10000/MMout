package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;

@FunctionalInterface
public interface RpcMethodFunction<Params, Result> {
  Result apply(MinecraftApi paramMinecraftApi, Params paramParams, ClientInfo paramClientInfo);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$RpcMethodFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */