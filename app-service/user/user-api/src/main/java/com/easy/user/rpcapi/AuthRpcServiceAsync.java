package com.easy.user.rpcapi;

import com.easy.common.rpcao.AuthRpcAo;
import com.weibo.api.motan.rpc.ResponseFuture;

public interface AuthRpcServiceAsync extends AuthRpcService {
  ResponseFuture authAsync(AuthRpcAo ao);
}
