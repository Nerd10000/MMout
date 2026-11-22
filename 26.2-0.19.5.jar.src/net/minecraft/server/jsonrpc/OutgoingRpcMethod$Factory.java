package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.api.MethodInfo;

@FunctionalInterface
public interface Factory<Params, Result> {
  OutgoingRpcMethod<Params, Result> create(MethodInfo<Params, Result> paramMethodInfo, OutgoingRpcMethod.Attributes paramAttributes);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */