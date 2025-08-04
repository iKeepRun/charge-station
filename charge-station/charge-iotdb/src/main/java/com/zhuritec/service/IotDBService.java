package com.zhuritec.service;

import org.apache.iotdb.isession.pool.SessionDataSetWrapper;

import java.util.List;

public interface IotDBService {


    SessionDataSetWrapper executeQueryStatement(String sql);
    SessionDataSetWrapper executeRawDataQuery(
            List<String> paths, long startTime, long endTime, long timeOut);
}
