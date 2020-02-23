package com.easy.user.rpcapi;

import com.easy.common.rpcao.AuthRpcAo;
import com.weibo.api.motan.rpc.ResponseFuture;

public interface PushAuthRpcServiceAsync extends PushAuthRpcService {
  ResponseFuture authAsync(AuthRpcAo ao);
}
