/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cassandra.tools.nodetool;

import io.airlift.command.Command;

import java.util.Map;

import org.apache.cassandra.concurrent.Stage;
import org.apache.cassandra.tools.NodeProbe;
import org.apache.cassandra.tools.NodeTool.NodeToolCmd;

@Command(name = "tpstats", description = "Print usage statistics of thread pools")
public class TpStats extends NodeToolCmd
{
    @Override
    public void execute(NodeProbe probe)
    {
        System.out.printf("%-25s%10s%10s%15s%10s%18s%n", "Pool Name", "Active", "Pending", "Completed", "Blocked", "All time blocked");

        for (Stage stage : Stage.jmxEnabledStages())
        {
            System.out.printf("%-25s%10s%10s%15s%10s%18s%n",
                              stage.getJmxName(),
                              probe.getThreadPoolMetric(stage, "ActiveTasks"),
                              probe.getThreadPoolMetric(stage, "PendingTasks"),
                              probe.getThreadPoolMetric(stage, "CompletedTasks"),
                              probe.getThreadPoolMetric(stage, "CurrentlyBlockedTasks"),
                              probe.getThreadPoolMetric(stage, "TotalBlockedTasks"));
        }

        System.out.printf("%n%-20s%10s%n", "Message type", "Dropped");
        for (Map.Entry<String, Integer> entry : probe.getDroppedMessages().entrySet())
            System.out.printf("%-20s%10s%n", entry.getKey(), entry.getValue());
    }
}