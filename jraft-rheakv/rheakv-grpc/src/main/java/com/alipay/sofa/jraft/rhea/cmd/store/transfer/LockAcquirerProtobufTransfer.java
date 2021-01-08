/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.jraft.rhea.cmd.store.transfer;

import com.alipay.sofa.jraft.rhea.cmd.store.proto.RheakvRpc;
import com.alipay.sofa.jraft.rhea.util.concurrent.DistributedLock;
import com.alipay.sofa.jraft.rpc.impl.GrpcSerializationTransfer;
import com.google.protobuf.ByteString;

/**
 * @author: baozi
 */
public class LockAcquirerProtobufTransfer implements
                                         GrpcSerializationTransfer<DistributedLock.Acquirer, RheakvRpc.LockAcquirer> {

    @Override
    public DistributedLock.Acquirer protoBufTransJavaBean(RheakvRpc.LockAcquirer lockAcquirer) {
        DistributedLock.Acquirer acquirer = new DistributedLock.Acquirer(lockAcquirer.getId(),
            lockAcquirer.getLeaseMillis());
        acquirer.setLockingTimestamp(lockAcquirer.getLockingTimestamp());
        acquirer.setFencingToken(lockAcquirer.getFencingToken());
        if (!lockAcquirer.getContext().isEmpty()) {
            acquirer.setContext(lockAcquirer.getContext().toByteArray());
        }
        return acquirer;
    }

    @Override
    public RheakvRpc.LockAcquirer javaBeanTransProtobufBean(DistributedLock.Acquirer acquirer) {
        RheakvRpc.LockAcquirer.Builder builder = RheakvRpc.LockAcquirer.newBuilder().setId(acquirer.getId())
            .setLeaseMillis(acquirer.getLeaseMillis()).setLockingTimestamp(acquirer.getLockingTimestamp())
            .setFencingToken(acquirer.getFencingToken());
        if (acquirer.getContext() != null) {
            builder.setContext(ByteString.copyFrom(acquirer.getContext())).build();
        }
        return builder.build();
    }
}